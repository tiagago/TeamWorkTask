package br.pucminas.teamworktask.ui.publica

import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentPreLoginBinding
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.utils.PermissionUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_ID
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_LOGIN
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_NOME
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.USUARIO_SENHA
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.obterPreferencia
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.obterPreferenciaInt
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.UsuarioViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PreLoginFragment] factory method to
 * create an instance of this fragment.
 */
class PreLoginFragment : GenericFragment() {
    private var _binding: FragmentPreLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var cancellationSignal: CancellationSignal? = null
    private val retrofitService = RetrofitService.getInstance()
    lateinit var viewModel: UsuarioViewModel

            @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreLoginBinding.inflate(inflater, container, false)
        prepararListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun prepararListeners(){
        carregarDadosPreference()
        configurarViewModels()
        configurarBotaoUsuarioCadastro()
        configurarBotaoEntrar()
    }

    /*********************************
     **** Controle Listeners View ****
     *********************************/
    @RequiresApi(Build.VERSION_CODES.P)
    private fun carregarDadosPreference() {
        binding.apply {
            val login : String = obterPreferencia(requireContext(), USUARIO_LOGIN)
            if(login.isNotBlank()){
                loginUsuarioTie.setText(login)
                loginFingerprintIv.visibility = View.VISIBLE
                configurarBotaoBiometria()
            } else {
                loginFingerprintIv.visibility = View.GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun configurarBotaoBiometria(){
        binding.apply {

        }
        if(PermissionUtils.checkBiometricSupport(requireContext())){
            binding.loginFingerprintIv.visibility = View.VISIBLE
            binding.loginFingerprintIv.setOnClickListener {
                authenticateUser()
            }
        } else {
            binding.loginFingerprintIv.visibility = View.GONE
        }
    }

    fun configurarBotaoUsuarioCadastro(){
        binding.loginAddUserIv.setOnClickListener {
            changeFragment(UsuarioCadastroFragment())
        }
    }

    /***************************************
     **** Validações para efetuar login ****
     ***************************************/
    fun configurarBotaoEntrar(){
        binding.loginEntrarBt.setOnClickListener {
            var achouProblema = false
            val login: String = binding.loginUsuarioTie.text.toString()
            val senha: String = binding.loginSenhaTie.text.toString()

            if(login.isBlank() || login == getString(R.string.pre_login_usuario_label)){
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
                showLoading(true)
                viewModel.doLogin(login, senha)
            }
        }
    }

    /************************************
     **** Configurações da Biometria ****
     ************************************/
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationHelp(helpCode: Int,
                                              helpString: CharSequence) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationSucceeded(result:
                                                   BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showLoading(true)
                viewModel.doLogin(obterPreferencia(requireContext(), USUARIO_LOGIN), obterPreferencia(requireContext(), USUARIO_SENHA))
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
            showWarningMessage("Cancelled via signal")
        }
        return cancellationSignal as CancellationSignal
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
                if(it?.usuario != null && it.success){
                    if (activity is PublicActivity) {
                        (activity as PublicActivity).doLogin(it.usuario!!)
                    }
                } else {
                    retornoErroServico(it)
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                showLoading(false)
                if(it != null){
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
            }
        }
    }

}