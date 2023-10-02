package br.pucminas.teamworktask.ui.privada.projeto

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.LayoutProjetoAssociarDialogBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.ProjetoRequest
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.BaseFragmentCallback
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.utils.FormatterUtils
import br.pucminas.teamworktask.utils.PermissionUtils
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoUsuarioViewModel
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback

class ProjetoAssociarDialog(private val projetos: List<Projeto>,
                            private val usuario: Usuario,
                            private val callback: BaseFragmentCallback) : DialogFragment() {
    private var _binding: LayoutProjetoAssociarDialogBinding? = null
    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!
    private val RECORD_REQUEST_CODE = 101
    private lateinit var codeScanner: CodeScanner
    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    lateinit var projetoUsuarioViewModel: ProjetoUsuarioViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LayoutProjetoAssociarDialogBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDetalhesProjeto(false)
        configurarViewModels()
        configurarListeners()
        configurarCamera()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /********************************************
     **** Controle de Visibilidade das Views ****
     ********************************************/

    private fun configurarListeners(){
        binding.apply {
            projetoAssociarQrcodeIv.setOnClickListener {
                if(PermissionUtils.checkCameraPermission(requireContext())){
                    qrClicked()
                } else {
                    pedirPermissaoCamera()
                }
            }

            projetoAssociarPesquisarBt.setOnClickListener {
                validarCodigo()
            }
        }
    }

    private fun qrClicked() {
        showObterGrupo(false)
        showCamera(true)
        codeScanner.startPreview()
    }

    fun showObterGrupo(isToShow: Boolean){
        "projeto_associar_titulo_tv,projeto_associar_codigo_til,projeto_associar_qrcode_iv,projeto_associar_pesquisar_bt"

        binding.apply {
            projetoAssociarTituloTv.visibility = if(isToShow) View.VISIBLE else View.GONE
            projetoAssociarCodigoTil.visibility = if(isToShow) View.VISIBLE else View.GONE
            projetoAssociarQrcodeIv.visibility = if(isToShow) View.VISIBLE else View.GONE
            projetoAssociarPesquisarBt.visibility = if(isToShow) View.VISIBLE else View.GONE
        }
    }

    fun showDetalhesProjeto(isToShow: Boolean){
        binding.apply {
            if(isToShow){
                projetoAssociarTituloTv.visibility = View.VISIBLE
            }
            projetoCard.apply {
                projetoCardArrowIv.visibility = View.GONE
                projetoCardCl.visibility = if(isToShow) View.VISIBLE else View.GONE

            }
            projetoAssociarJuntarseBt.visibility = if(isToShow) View.VISIBLE else View.GONE
        }
    }

    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/

    private fun configurarViewModels() {
        configurarProjetoViewModels()
        configurarProjetoUsuarioViewModels()
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.apply {
            projetosResponse.observe(viewLifecycleOwner) {

                if (it.success) {
                    if(it.projetos == null || it.projetos!!.isEmpty()){
                        callback.showWarningMessage(getString(R.string.projeto_associar_projeto_invalido_warning))
                    } else {
                        showObterGrupo(false)
                        showDetalhesProjeto(true)
                        processarProjeto(it.projetos!![0])
                    }
                } else {
                    callback.showErrorMessage(callback.retornoErroServicoReturn(it))
                }
                callback.showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    callback.showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                callback.showLoading(false)
            }
        }
    }

    private fun configurarProjetoUsuarioViewModels() {
        projetoUsuarioViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoUsuarioViewModel::class.java
            )
        projetoUsuarioViewModel.apply {
            genericResponse.observe(viewLifecycleOwner) {
                if (it != null && it.success) {
                    callback.showSuccessMessage(getString(R.string.projeto_associar_sucesso))
                    dismiss()
                } else {
                    callback.showErrorMessage(callback.retornoErroServicoReturn(it))
                }
                callback.showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    callback.showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                callback.showLoading(false)
            }
        }
    }

    /********************************
     **** Validações da requisição ****
     ********************************/

    private fun validarCodigo() {
        binding.apply {
            var achouProblema = false
            val codigo = projetoAssociarCodigoTie.text.toString()
            if(codigo.isEmpty() || getString(R.string.projeto_associar_codigo_projeto_label) == codigo){
                achouProblema = true
                projetoAssociarCodigoTil.error = getString(R.string.generico_vazio_erro, getString(R.string.projeto_associar_codigo_projeto_label))
            } else {
                projetoAssociarCodigoTil.error = null

                projetos.forEach {
                    if(it.codigo == codigo){
                        achouProblema = true
                        callback.showWarningMessage(getString(R.string.projeto_associar_ja_associado_warning))
                    }
                }
            }

            if(!achouProblema){
                callback.showLoading(true)
                projetoViewModel.obterProjetoPorCodigo(codigo)
            }
        }
    }

    /*********************************
     ******* Processar Projeto *******
     *********************************/
    fun processarProjeto(projeto: Projeto){
        binding.apply {
            projetoCard.apply {
                projetoCardNomeTv.text = projeto.nome
                projetoCardDescricaoTv.text = projeto.descricao
                projetoCardDataCriacaoTv.text = FormatterUtils.formatDateToString(projeto.dataCriacao)

                projetoAssociarJuntarseBt.setOnClickListener {
                    var projetoRequest = ProjetoRequest()
                    projetoRequest.projeto = projeto
                    projetoRequest.usuario = usuario
                    projetoUsuarioViewModel.associarUsuarioProjeto(projetoRequest)
                }
            }
        }
    }

    /********************************
     **** Configuração da Camera ****
     ********************************/

    private fun configurarCamera() {
        val scannerView = binding.projetoAssociarScannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                showObterGrupo(true)
                showCamera(false)
                binding.projetoAssociarCodigoTie.setText(it.text)
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    fun showCamera(isToShow: Boolean){
        binding.projetoAssociarScannerView.visibility = if(isToShow) View.VISIBLE else View.GONE
    }

    private fun pedirPermissaoCamera() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    (activity as GenericActivity).showAlert(TopAlertMessageObject(TopAlertType.ERROR, "Erro ao obter permissao."))
                } else {
                    qrClicked()
                }
            }
        }
    }
}