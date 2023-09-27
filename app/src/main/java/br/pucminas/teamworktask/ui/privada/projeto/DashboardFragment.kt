package br.pucminas.teamworktask.ui.privada.projeto

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentDashboardBinding
import br.pucminas.teamworktask.databinding.LayoutSeletorProjetoDialogBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.ui.privada.PrivateActivity
import br.pucminas.teamworktask.ui.publica.PublicActivity
import br.pucminas.teamworktask.utils.FormatterUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.PROJETO_ID
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferenciaInt
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.obterPreferenciaInt
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : GenericFragment(), ProjetoSeletorOnClickInterface {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    private var meusProjetos = ArrayList<Projeto>()
    private var outrosProjetos = ArrayList<Projeto>()
    private var projetos = ArrayList<Projeto>()
    var contServicos = 0
    var erroServico = ""
    var isMyProjeto = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        configurarViewModels()
        configurarTitulo()
        configurarLogoutButton()
        configurarFloatingButton()
        chamarServicos()

        return binding.root
    }

    private fun configurarTitulo() {
        var usuario = obterUsuarioPreference()
        binding.dashboardTituloTv.text = getString(R.string.dashboard_title, usuario.nomeExibicao)
    }

    private fun configurarLogoutButton() {
        binding.dashboardLogoutIv.setOnClickListener {
            if (activity is PrivateActivity) {
                (activity as PrivateActivity).chamarPopupLogout()
            }
        }
    }

    fun configurarFloatingButton() {
        binding.dashboardMainFab.setOnClickListener {
            onAddButtonClicked()
        }
        binding.dashboardEditarFab.setOnClickListener {

        }
        binding.dashboardNovoFab.setOnClickListener {
            changeFragment(ProjetoCadastroFragment())
        }
        binding.dashboardParticiparFab.setOnClickListener {
            ProjetoAssociarDialog().show(parentFragmentManager, "")
        }
    }

    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.dashboardNovoFab.visibility = if (clicked) View.GONE else View.VISIBLE
        binding.dashboardNovoTv.visibility = if (clicked) View.GONE else View.VISIBLE
        if(isMyProjeto){
            binding.dashboardEditarFab.visibility = if (clicked) View.GONE else View.VISIBLE
            binding.dashboardEditarTv.visibility = if (clicked) View.GONE else View.VISIBLE
        }
        binding.dashboardParticiparFab.visibility = if (clicked) View.GONE else View.VISIBLE
        binding.dashboardParticiparTv.visibility = if (clicked) View.GONE else View.VISIBLE
    }

    private fun setAnimation() {
        binding.dashboardNovoFab.startAnimation(if (clicked) toBottom else fromBottom)
        binding.dashboardNovoTv.startAnimation(if (clicked) toBottom else fromBottom)
        if(isMyProjeto){
            binding.dashboardEditarFab.startAnimation(if (clicked) toBottom else fromBottom)
            binding.dashboardEditarTv.startAnimation(if (clicked) toBottom else fromBottom)
        }
        binding.dashboardParticiparFab.startAnimation(if (clicked) toBottom else fromBottom)
        binding.dashboardParticiparTv.startAnimation(if (clicked) toBottom else fromBottom)
        binding.dashboardMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
    }

    private fun configurarViewModels() {
        configurarProjetoViewModels()
        configurarUsuarioViewModels()
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.projetosResponse.observe(viewLifecycleOwner) {
            contServicos--
            if (it?.projetos != null && it.projetos!!.isNotEmpty() && it.success) {
                meusProjetos.addAll(it.projetos!!)
            } else {
                erroServico = retornoErroServicoReturn(it)
            }
            processarProjetos()
        }

        projetoViewModel.errorMessage.observe(viewLifecycleOwner) {
            contServicos--
            if (it != null) {
                erroServico = it
                usuarioViewModel.errorMessage.postValue(null)
            }
            processarProjetos()
        }
    }

    private fun configurarUsuarioViewModels() {
        usuarioViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                UsuarioViewModel::class.java
            )

        usuarioViewModel.usuarioResponse.observe(viewLifecycleOwner) {
            contServicos--
            if (it?.usuario != null && it.usuario!!.participa.isNotEmpty() && it.success) {
                outrosProjetos.addAll(it.usuario!!.participa)
            } else {
                erroServico = retornoErroServicoReturn(it)
            }
            processarProjetos()
        }

        usuarioViewModel.errorMessage.observe(viewLifecycleOwner) {
            contServicos--
            if (it != null) {
                erroServico = it
                usuarioViewModel.errorMessage.postValue(null)
            }
            processarProjetos()
        }
    }

    private fun chamarServicos() {
        showLoading(true)
        contServicos = 2
        val usuario = obterUsuarioPreference()
        usuarioViewModel.obterUsuarioComProjetos(usuario.id.toInt())
        projetoViewModel.oberMeusProjetos(usuario.id.toInt())
    }

    fun processarProjetos(){

        if(contServicos > 0){
            return
        }

        projetos.clear()
        projetos.addAll(meusProjetos)
        projetos.addAll(outrosProjetos)

        if(projetos.isNotEmpty()){
            val projetoSelecionadoId = obterPreferenciaInt(requireContext(), PROJETO_ID)
            var projetoSelecionado = projetos[0]
            if(projetoSelecionadoId > 0){
                projetos.forEach{
                    if(it.id.toInt() == projetoSelecionadoId){
                        projetoSelecionado = it
                    }
                }
            }

            selecaoProjeto(projetoSelecionado)
        } else {
            binding.dashboardCard.projetoCard.visibility = View.GONE
            showBottomNavigator(false)
        }

        showLoading(false)
    }

    fun selecaoProjeto(projeto: Projeto){
        guardarPreferenciaInt(requireContext(), PROJETO_ID, projeto.id.toInt())

        binding.dashboardCard.projetoCard.visibility = View.VISIBLE
        binding.dashboardCard.projetoCardNomeTv.text = projeto.nome
        binding.dashboardCard.projetoCardDescricaoTv.text = projeto.descricao
        binding.dashboardCard.projetoCardDataCriacaoTv.text = FormatterUtils.formatDateToString(projeto.dataCriacao)

        val usuario = obterUsuarioPreference()
        isMyProjeto = projeto.criador == usuario.id
        (activity as PrivateActivity).changeBottomNavigationLayout(projeto.criador == usuario.id)
        binding.dashboardCard.projetoCard.setOnClickListener {
            ProjetoSeletorDialog(projetos, projeto, this).show(parentFragmentManager, "")
        }
    }

    fun showBottomNavigator(isVisible: Boolean){
        (activity as PrivateActivity).showBottomNavigator(isVisible)
    }

    override fun onClickProjeto(projeto: Projeto) {
        selecaoProjeto(projeto)
    }

}