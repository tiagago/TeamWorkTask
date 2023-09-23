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
import br.pucminas.teamworktask.repositories.UsuarioRepository
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUsuarioCadastroBinding.inflate(inflater, container, false)

        configurarViewModels()
        prepararListeners()
        return binding.root
    }

    fun prepararListeners(){
        configurarBotaoCancelar()
        configurarBotaoSalvar()
    }

    private fun configurarBotaoCancelar() {
        binding.usuarioCadastroCancelarBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun configurarBotaoSalvar() {
        binding.usuarioCadastroSalvarBt.setOnClickListener {
            salvarUsuario()
        }
    }

    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(UsuarioRepository(retrofitService))).get(
                UsuarioViewModel::class.java
            )

        viewModel.usuarioResponse.observe(viewLifecycleOwner) {
            if(it?.usuario != null && it.success){
                onBackPressed(TopAlertMessageObject(TopAlertType.SUCCESS, getString(R.string.usuario_cadastro_sucesso)))
            } else {
                retornoErroServico(it)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            showErrorGenericServer()
        }
    }

    private fun salvarUsuario() {
        binding.apply {

            var achouProblema = false
            val nome: String = usuarioCadastroNomeTie.text.toString()
            val email: String = usuarioCadastroEmailTie.text.toString()
            val senha: String = usuarioCadastroSenhaTie.text.toString()
            val senhaRepetida: String = usuarioCadastroRepetirSenhaTie.text.toString()

            if(nome.isBlank() || nome == getString(R.string.usuario_cadastro_nome_label)){
                usuarioCadastroNomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_nome_label))
                achouProblema = true
            } else {
                usuarioCadastroNomeTil.error = null
            }

            if(email.isBlank() || nome == getString(R.string.usuario_cadastro_email_label)){
                usuarioCadastroEmailTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_email_label))
                achouProblema = true
            } else {
                usuarioCadastroEmailTil.error = null
            }

            if(senha.isBlank() || nome == getString(R.string.usuario_cadastro_senha_label)){
                usuarioCadastroSenhaTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_senha_label))
                achouProblema = true
            } else {
                usuarioCadastroSenhaTil.error = null
            }

            if(senhaRepetida.isBlank() || nome == getString(R.string.usuario_cadastro_repetir_senha_label)){
                usuarioCadastroRepetirSenhaTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_repetir_senha_label))
                achouProblema = true
            } else {
                usuarioCadastroRepetirSenhaTil.error = null
            }

            if(!achouProblema){
                var usuario = Usuario()

                usuario.email = email
                usuario.nomeExibicao = nome
                usuario.senha = senha
                viewModel.criarUsuario(usuario)
            }
        }
    }
}