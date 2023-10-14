package br.pucminas.teamworktask.ui.publica

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentUsuarioCadastroBinding
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.GenericRequest
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [UsuarioCadastroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsuarioCadastroFragment : GenericFragment() {
    private var _binding: FragmentUsuarioCadastroBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val retrofitService = RetrofitService.getInstance()
    lateinit var viewModel: UsuarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUsuarioCadastroBinding.inflate(inflater, container, false)

        configurarViewModels()
        prepararListeners()
        return binding.root
    }


    /*********************************
     **** Controle Listeners View ****
     *********************************/
    fun prepararListeners() {
        binding.apply {
            usuarioCadastroCancelarBt.setOnClickListener {
                onBackPressed()
            }
            usuarioCadastroSalvarBt.setOnClickListener {
                salvarUsuario()
            }
        }
    }

    /***************************************
     **** Validações para salvar/editar ****
     ***************************************/
    private fun salvarUsuario() {
        binding.apply {

            var achouProblema = false
            val nome: String = usuarioCadastroNomeTie.text.toString()
            val login: String = usuarioCadastroLoginTie.text.toString()
            val senha: String = usuarioCadastroSenhaTie.text.toString()
            val senhaRepetida: String = usuarioCadastroRepetirSenhaTie.text.toString()

            if (nome.isBlank() || nome == getString(R.string.usuario_cadastro_nome_label)) {
                usuarioCadastroNomeTil.error = getString(
                    R.string.generico_vazio_erro,
                    getString(R.string.usuario_cadastro_nome_label)
                )
                achouProblema = true
            } else {
                usuarioCadastroNomeTil.error = null
            }

            if (login.isBlank() || login == getString(R.string.usuario_cadastro_login_label)) {
                usuarioCadastroLoginTil.error = getString(
                    R.string.generico_vazio_erro,
                    getString(R.string.usuario_cadastro_login_label)
                )
                achouProblema = true
            } else {
                usuarioCadastroLoginTil.error = null
            }

            if (senha.isBlank() || senha == getString(R.string.usuario_cadastro_senha_label)) {
                usuarioCadastroSenhaTil.error = getString(
                    R.string.generico_vazio_erro,
                    getString(R.string.usuario_cadastro_senha_label)
                )
                achouProblema = true
            } else {
                usuarioCadastroSenhaTil.error = null
            }

            if (senhaRepetida.isBlank() || senhaRepetida == getString(R.string.usuario_cadastro_repetir_senha_label)) {
                usuarioCadastroRepetirSenhaTil.error = getString(
                    R.string.generico_vazio_erro,
                    getString(R.string.usuario_cadastro_repetir_senha_label)
                )
                achouProblema = true
            } else {
                usuarioCadastroRepetirSenhaTil.error = null
            }

            if (!achouProblema) {
                showLoading(true)
                var usuario = Usuario()

                usuario.login = login
                usuario.nomeExibicao = nome
                usuario.senha = senha

                var genericRequest = GenericRequest()
                genericRequest.usuario = usuario

                viewModel.criarUsuario(genericRequest)
            }
        }
    }

    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/
    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                UsuarioViewModel::class.java
            )

        viewModel.apply {
            usuarioResponse.observe(viewLifecycleOwner) {
                showLoading(false)
                if (it?.usuario != null && it.success) {
                    onBackPressed(
                        TopAlertMessageObject(
                            TopAlertType.SUCCESS,
                            getString(R.string.usuario_cadastro_sucesso)
                        )
                    )
                } else {
                    retornoErroServico(it)
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                showLoading(false)
                showErrorGenericServer()
            }
        }
    }
}