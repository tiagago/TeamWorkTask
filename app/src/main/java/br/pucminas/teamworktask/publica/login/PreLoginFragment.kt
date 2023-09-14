package br.pucminas.teamworktask.publica.login

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
import br.pucminas.teamworktask.publica.PublicActivity

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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreLoginBinding.inflate(inflater, container, false)

        prepararListeners()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun prepararListeners(){
        configurarBotaoBiometria()
        configurarBotaoUsuarioCadastro()
        configurarBotaoEntrar()
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
                (activity as PublicActivity).changeFragment(UsuarioCadastroFragment.newInstance())
            }
        }
    }

    fun configurarBotaoEntrar(){
        binding.loginEntrarBt.setOnClickListener {
            if(activity is PublicActivity){
                (activity as PublicActivity).doLogin()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PreLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PreLoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
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