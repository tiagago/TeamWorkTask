package br.pucminas.teamworktask.publica.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentDashboardBinding
import br.pucminas.teamworktask.databinding.FragmentPreLoginBinding
import br.pucminas.teamworktask.databinding.FragmentUsuarioCadastroBinding

/**
 * A simple [Fragment] subclass.
 * Use the [UsuarioCadastroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsuarioCadastroFragment : Fragment() {
    private var _binding: FragmentUsuarioCadastroBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUsuarioCadastroBinding.inflate(inflater, container, false)

        prepararListeners()
        return binding.root
    }

    fun prepararListeners(){
        configurarBotaoCancelar()
        configurarBotaoSalvar()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment UsuarioCadastro.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            UsuarioCadastroFragment().apply {

            }
    }


    private fun configurarBotaoCancelar() {
        binding.usuarioCadastroCancelarBt.setOnClickListener {

        }
    }

    private fun configurarBotaoSalvar() {
        binding.usuarioCadastroSalvarBt.setOnClickListener {
            salvarUsuario()
        }
    }

    private fun salvarUsuario() {
        binding.apply {

            var achouProblema = false
            val nome: String = usuarioCadastroNomeTie.text.toString()
            val sobrenome: String = usuarioCadastroSobrenomeTie.text.toString()
            val email: String = usuarioCadastroEmailTie.text.toString()
            val senha: String = usuarioCadastroSenhaTie.text.toString()
            val senhaRepetida: String = usuarioCadastroRepetirSenhaTie.text.toString()

            if(nome.isBlank() || nome == getString(R.string.usuario_cadastro_nome_label)){
                usuarioCadastroNomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_nome_label))
                achouProblema = true
            } else {
                usuarioCadastroNomeTil.error = null
            }

            if(sobrenome.isBlank() || nome == getString(R.string.usuario_cadastro_sobrenome_label)){
                usuarioCadastroSobrenomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.usuario_cadastro_sobrenome_label))
                achouProblema = true
            } else {
                usuarioCadastroSobrenomeTil.error = null
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

            }
        }
    }
}