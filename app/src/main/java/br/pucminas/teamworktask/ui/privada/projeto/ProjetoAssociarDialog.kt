package br.pucminas.teamworktask.ui.privada.projeto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.LayoutProjetoAssociarDialogBinding
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.utils.PermissionUtils
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback

class ProjetoAssociarDialog : DialogFragment() {
    private var _binding: LayoutProjetoAssociarDialogBinding? = null
    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!
    private val RECORD_REQUEST_CODE = 101
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LayoutProjetoAssociarDialogBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    private fun configurarListeners(){
        binding.projetoAssociarQrcodeIv.setOnClickListener {
            if(PermissionUtils.checkCameraPermission(requireContext())){
                qrClicked()
            } else {
                pedirPermissaoCamera()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarListeners()
        configurarCamera()
    }

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

    private fun qrClicked() {
        showObterGrupo(false)
        showCamera(true)
        codeScanner.startPreview()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun showCamera(isToShow: Boolean){
        binding.projetoAssociarScannerView.visibility = if(isToShow) View.VISIBLE else View.GONE
    }

    fun showObterGrupo(isToShow: Boolean){
        binding.projetoAssociarObterGrupo.visibility = if(isToShow) View.VISIBLE else View.GONE
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