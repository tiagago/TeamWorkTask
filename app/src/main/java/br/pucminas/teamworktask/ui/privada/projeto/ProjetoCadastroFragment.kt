package br.pucminas.teamworktask.ui.privada.projeto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentDashboardBinding
import br.pucminas.teamworktask.databinding.FragmentProjetoCadastroBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.utils.FormatterUtils
import br.pucminas.teamworktask.utils.GeradorCodigo
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProjetoCadastroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjetoCadastroFragment : GenericFragment() {
    private var _binding: FragmentProjetoCadastroBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var projeto: Projeto = Projeto()
    var nomeCriador: String? = null
    private val retrofitService = RetrofitService.getInstance()
    lateinit var viewModel: ProjetoViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProjetoCadastroBinding.inflate(inflater, container, false)

        projeto.dataCriacao = Date()
        projeto.codigo = GeradorCodigo.geraCodigoProjeto()
        projeto.usuario = obterUsuarioPreference()

        configurarViewModels()
        configurarTextViews()
        prepararListeners()

        return binding.root
    }

    fun configurarTextViews(){
        binding.projetoCadastroCriadoEmTv.text = getString(R.string.projeto_cadastro_criado_em_label, FormatterUtils.formatDateToString(projeto.dataCriacao))
        binding.projetoCadastroCriadoPorTv.text = getString(R.string.projeto_cadastro_criado_por_label, projeto.usuario.nomeExibicao)
        binding.projetoCadastroCodigoTv.text = getString(R.string.projeto_cadastro_codigo_label, projeto.codigo)
    }

    fun prepararListeners(){
        binding.projetoCadastroCancelarBt.setOnClickListener {
            onBackPressed()
        }

        binding.projetoCadastroSalvarBt.setOnClickListener {
            salvarProjeto()
        }
    }

    private fun salvarProjeto() {
        binding.apply {

            var achouProblema = false
            val nome: String = projetoCadastroNomeTie.text.toString()
            val descricao: String = projetoCadastroDescricaoTie.text.toString()

            if(nome.isBlank() || nome == getString(R.string.projeto_cadastro_nome_label)){
                projetoCadastroNomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.projeto_cadastro_nome_label))
                achouProblema = true
            } else {
                projetoCadastroNomeTil.error = null
            }

            if(descricao.isBlank() || descricao == getString(R.string.projeto_cadastro_descricao_label)){
                projetoCadastroDescricaoTil.error = getString(R.string.generico_vazio_erro, getString(R.string.projeto_cadastro_descricao_label))
                achouProblema = true
            } else {
                projetoCadastroDescricaoTil.error = null
            }

            if(!achouProblema){
                projeto.nome = nome
                projeto.descricao = descricao

                viewModel.criarProjeto(projeto)
            }
        }
    }

    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        viewModel.projetoResponse.observe(viewLifecycleOwner) {
            if(it?.projeto != null && it.success){
                onBackPressed(TopAlertMessageObject(TopAlertType.SUCCESS, getString(R.string.projeto_cadastro_sucesso)))
            } else {
                retornoErroServico(it)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            showErrorGenericServer()
        }
    }
}