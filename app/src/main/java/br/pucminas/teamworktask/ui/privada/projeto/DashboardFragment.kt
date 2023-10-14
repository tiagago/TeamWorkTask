package br.pucminas.teamworktask.ui.privada.projeto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentDashboardBinding
import br.pucminas.teamworktask.models.Historico
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.privada.PrivateActivity
import br.pucminas.teamworktask.ui.privada.PrivateFragment
import br.pucminas.teamworktask.utils.FormatterUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.PROJETO_ID
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.guardarPreferenciaInt
import br.pucminas.teamworktask.viewmodels.HistoricoViewModel
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : PrivateFragment(), ProjetoSeletorOnClickInterface, ProjetoAssociarCallback {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var historicoViewModel: HistoricoViewModel
    private var meusProjetos = ArrayList<Projeto>()
    private var outrosProjetos = ArrayList<Projeto>()
    private var projetos = ArrayList<Projeto>()
    private var projetoSelecionado = Projeto()
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
        configurarFloatingButton()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        chamarServicos()
    }

    override fun obterIcone(): Int {
        return R.drawable.ic_user
    }
    override fun obterTitulo(): String {
        var usuario = obterUsuarioPreference()
        return getString(R.string.dashboard_title, usuario.nomeExibicao)
    }

    /*****************************************
     **** Configuração do floating Button ****
     *****************************************/

    fun configurarFloatingButton() {
        binding.dashboardMainFab.setOnClickListener {
            onAddButtonClicked()
        }
        binding.dashboardEditarFab.setOnClickListener {
            changeFragment(ProjetoCadastroFragment(projetoSelecionado))
        }
        binding.dashboardNovoFab.setOnClickListener {
            changeFragment(ProjetoCadastroFragment())
        }
        binding.dashboardParticiparFab.setOnClickListener {
            ProjetoAssociarDialog(projetos, obterUsuarioPreference(), this, this).show(parentFragmentManager, "")
        }
    }

    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.dashboardNovoFab.visibility = if (clicked) View.GONE else View.VISIBLE
        if(isMyProjeto){
            binding.dashboardEditarFab.visibility = if (clicked) View.GONE else View.VISIBLE
        }
        binding.dashboardParticiparFab.visibility = if (clicked) View.GONE else View.VISIBLE
    }

    private fun setAnimation() {
        binding.dashboardNovoFab.startAnimation(if (clicked) toBottom else fromBottom)
        if(isMyProjeto){
            binding.dashboardEditarFab.startAnimation(if (clicked) toBottom else fromBottom)
        }
        binding.dashboardParticiparFab.startAnimation(if (clicked) toBottom else fromBottom)
        binding.dashboardMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
    }

    /******************************
     **** Chamadas de Serviços ****
     ******************************/

    private fun chamarServicos() {
        showLoading(true)
        contServicos = 2
        val usuario = obterUsuarioPreference()
        usuarioViewModel.obterUsuarioComProjetos(usuario.id.toInt())
        projetoViewModel.oberMeusProjetos(usuario.id.toInt())
    }

    private fun chamarServicoHistorico() {
        showLoading(true)
        historicoViewModel.obterTarefasPorProjetoStatus(projetoSelecionado.id.toInt())
    }

    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/
    private fun configurarViewModels() {
        configurarProjetoViewModels()
        configurarUsuarioViewModels()
        configurarHistoricoViewModels()
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.apply {
            projetosResponse.observe(viewLifecycleOwner) {
                contServicos--
                if (it?.projetos != null && it.projetos!!.isNotEmpty() && it.success) {
                    meusProjetos = ArrayList()
                    meusProjetos.clear()
                    meusProjetos.addAll(it.projetos!!)
                } else {
                    erroServico = retornoErroServicoReturn(it)
                }
                processarProjetos()
            }

            errorMessage.observe(viewLifecycleOwner) {
                contServicos--
                if (it != null) {
                    erroServico = it
                    errorMessage.postValue(null)
                }
                processarProjetos()
            }
        }
    }

    private fun configurarUsuarioViewModels() {
        usuarioViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                UsuarioViewModel::class.java
            )

        usuarioViewModel.apply {
            usuarioResponse.observe(viewLifecycleOwner) {
                contServicos--
                if (it?.usuario != null && it.usuario!!.participa.isNotEmpty() && it.success) {
                    outrosProjetos = ArrayList()
                    outrosProjetos.clear()
                    outrosProjetos.addAll(it.usuario!!.participa)
                } else {
                    erroServico = retornoErroServicoReturn(it)
                }
                processarProjetos()
            }

            errorMessage.observe(viewLifecycleOwner) {
                contServicos--
                if (it != null) {
                    erroServico = it
                    errorMessage.postValue(null)
                }
                processarProjetos()
            }
        }
    }

    private fun configurarHistoricoViewModels() {
        historicoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                HistoricoViewModel::class.java
            )

        historicoViewModel.apply {
            historicosResponse.observe(viewLifecycleOwner) {
                processarHistorico(if (it?.historicos != null && it.historicos!!.isNotEmpty() && it.success)
                    it.historicos
                else
                    null
                )

                showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    erroServico = it
                    errorMessage.postValue(null)
                }
                showLoading(false)
            }
        }
    }

    /************************************
     **** Processar retorno Serviços ****
     ************************************/

    fun processarProjetos(){

        if(contServicos > 0){
            return
        }

        projetos = ArrayList()
        projetos.clear()
        projetos.addAll(meusProjetos)
        projetos.addAll(outrosProjetos)

        if(projetos.isNotEmpty()){
            val projetoSelecionadoId = obterProjetoSelecionado()
            projetoSelecionado = projetos[0]
            if(projetoSelecionadoId > 0){
                projetos.forEach{
                    if(it.id.toInt() == projetoSelecionadoId){
                        projetoSelecionado = it
                    }
                }
            }

            selecaoProjeto()
            showBottomNavigator(true)
            showInformacoesProjeto(true)
        } else {
            binding.dashboardCard.projetoCard.visibility = View.GONE
            showBottomNavigator(false)
            showInformacoesProjeto(false)
        }

        showLoading(false)
    }

    fun processarHistorico(historicos: List<Historico>?){
        if (!historicos.isNullOrEmpty()){
            binding.dashboardListaRv.adapter = HistoricoAdapter(requireContext(), historicos)
            showListaVaziaMensagem(false)
        } else {
            showListaVaziaMensagem(true)
        }
    }

    private fun showListaVaziaMensagem(isShow: Boolean){
        binding.dashboardListaVaziaGrupo.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    /*******************************
     **** Card Projeto Selector ****
     *******************************/

    fun selecaoProjeto(){
        guardarPreferenciaInt(requireContext(), PROJETO_ID, projetoSelecionado.id.toInt())
        binding.apply {
            dashboardCard.projetoCard.visibility = View.VISIBLE
            dashboardCard.projetoCardNomeTv.text = projetoSelecionado.nome
            dashboardCard.projetoCardDescricaoTv.text = projetoSelecionado.descricao
            dashboardCard.projetoCardDataCriacaoTv.text = FormatterUtils.formatDateToString(projetoSelecionado.dataCriacao)

            val usuario = obterUsuarioPreference()
            isMyProjeto = projetoSelecionado.criador == usuario.id
            dashboardCard.projetoCard.setOnClickListener {
                chamarSeletorProjeto()
            }
            chamarServicoHistorico()
        }
    }

    fun chamarSeletorProjeto() {
        ProjetoSeletorDialog(projetos, projetoSelecionado, this).show(parentFragmentManager, "")
    }

    fun showBottomNavigator(isVisible: Boolean){
        (activity as PrivateActivity).showBottomNavigator(isVisible)
    }

    fun showInformacoesProjeto(isShowProjeto: Boolean){
        binding.apply {
            dashboardProjetoVaziaGrupo.visibility = if(isShowProjeto) View.GONE else View.VISIBLE
            dashboardProjetoGrupo.visibility = if(isShowProjeto) View.VISIBLE else View.GONE
        }
    }

    /**************************************
     **** Override Interfaces callback ****
     **************************************/

    override fun onClickProjeto(projeto: Projeto) {
        projetoSelecionado = projeto
        selecaoProjeto()
    }

    override fun onCloseDialog() {
        chamarServicos()
    }

}