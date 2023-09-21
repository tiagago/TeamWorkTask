package br.pucminas.teamworktask.publica.ui

import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentPreLoginBinding
import br.pucminas.teamworktask.utils.PermissionUtils
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.os.CancellationSignal
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.UsuarioRepository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PreLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreLoginFragment : Fragment() {
    private var _binding: FragmentPreLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var cancellationSignal: CancellationSignal? = null
    private val retrofitService = RetrofitService.getInstance()
    lateinit var viewModel: UsuarioViewModel

            @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreLoginBinding.inflate(inflater, container, false)

        prepararListeners()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun prepararListeners(){
        configurarViewModels()
        configurarBotaoBiometria()
        configurarBotaoUsuarioCadastro()
        configurarBotaoEntrar()
    }

    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(UsuarioRepository(retrofitService))).get(
                UsuarioViewModel::class.java
            )

        viewModel.usuario.observe(viewLifecycleOwner) {
            if (activity is PublicActivity) {
                (activity as PublicActivity).doLogin()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun configurarBotaoBiometria(){
        if(PermissionUtils.checkBiometricSupport(requireContext())){
            binding.loginFingerprintIv.visibility = View.VISIBLE;
            binding.loginFingerprintIv.setOnClickListener {
                authenticateUser();
            }
        } else {
            binding.loginFingerprintIv.visibility = View.GONE;
        }
    }

    fun configurarBotaoUsuarioCadastro(){
        binding.loginAddUserIv.setOnClickListener {
            if(activity is PublicActivity){
                (activity as PublicActivity).changeFragment(UsuarioCadastroFragment())
            }
        }
    }

    fun configurarBotaoEntrar(){
        binding.loginEntrarBt.setOnClickListener {
            var achouProblema = false
            val email: String = binding.loginUsuarioTie.text.toString()
            val senha: String = binding.loginSenhaTie.text.toString()

            if(email.isBlank() || email == getString(R.string.pre_login_usuario_label)){
                binding.loginUsuarioTil.error = getString(R.string.generico_vazio_erro, getString(R.string.pre_login_usuario_label))
                achouProblema = true
            } else {
                binding.loginUsuarioTil.error = null
            }

            if(senha.isBlank() || senha == getString(R.string.pre_login_senha_label)){
                binding.loginSenhaTil.error = getString(R.string.generico_vazio_erro, getString(R.string.pre_login_senha_label))
                achouProblema = true
            } else {
                binding.loginSenhaTil.error = null
            }

            if(!achouProblema){
                viewModel.doLogin(Usuario(0, email, "", senha))
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                notifyUser("Authentication error: $errString")
            }

            override fun onAuthenticationHelp(helpCode: Int,
                                              helpString: CharSequence) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                notifyUser("Authentication Failed")
            }

            override fun onAuthenticationSucceeded(result:
                                                   BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                notifyUser("Authentication Succeeded")

            }
        }
    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticateUser() {
        val biometricPrompt = BiometricPrompt.Builder(context)
            .setTitle(getString(R.string.pre_login_biometria_titulo))
            .setSubtitle(getString(R.string.pre_login_biometria_subtitulo))
            .setDescription(getString(R.string.pre_login_biometria_descricao))
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL).build()

        biometricPrompt.authenticate(getCancellationSignal(), requireActivity().mainExecutor,
            authenticationCallback)
    }

    private fun getCancellationSignal(): CancellationSignal {

        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Cancelled via signal")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun notifyUser(message: String) {
        Toast.makeText(context,
            message,
            Toast.LENGTH_LONG).show()
    }
}