package br.pucminas.teamworktask.ui

import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.loadingDialog.LoadingDialog
import br.pucminas.teamworktask.componentes.topAlert.TopAlertView
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class GenericActivity : AppCompatActivity() {

    private val loadingDialog: LoadingDialog  by lazy(mode = LazyThreadSafetyMode.NONE) {
        LoadingDialog()
    }

    private val alert: TopAlertView by lazy(mode = LazyThreadSafetyMode.NONE) {
        TopAlertView()
    }


    fun showAlert(topAlertMessageObject: TopAlertMessageObject?){
        if(topAlertMessageObject == null) {
            return
        }
        alert.topAlertMessageObject = topAlertMessageObject
        alert.show(supportFragmentManager, TopAlertView.TAG)
    }

    // Declare the onBackPressed method when the back button is pressed this method will call
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 1) {
            chamarPopupLogout()
        } else {
            supportFragmentManager.popBackStack();
        }
    }

    fun changeFragment(fragment: Fragment, topAlertMessageObject: TopAlertMessageObject? = null){
        supportFragmentManager.beginTransaction()
            .replace(obterFramelayoutID() , fragment)
            .setReorderingAllowed(true)
            .addToBackStack(fragment.javaClass.simpleName).commit()
        showAlert(topAlertMessageObject)
    }

    open fun obterFramelayoutID(): Int = -1

    fun chamarPopupLogout() {
        // Create the object of AlertDialog Builder class
        val builder = MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)

        // Set the message show for the Alert time
        builder.setMessage(obterMensagemLogout())

        // Set Alert Title
        builder.setTitle(obterTituloLogout())

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(R.string.generico_confirmar) {
            // When the user click yes button then app will close
                dialog, which -> executarLogout()
        }

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton(R.string.generico_negar) {
            // If user click no then dialog box is canceled.
                dialog, which ->
            dialog.cancel()
        }

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }

    fun onBackPressed(topAlertMessageObject: TopAlertMessageObject){
        supportFragmentManager.popBackStack()
        showAlert(topAlertMessageObject)
    }

    open fun obterMensagemLogout(): String = ""

    open fun obterTituloLogout(): String = ""

    open fun executarLogout(){}

    fun showLoading(isLoading: Boolean) {
        loadingDialog.let {
            if (isLoading && !loadingDialog.isVisible && (loadingDialog.dialog == null  || !loadingDialog.dialog!!.isShowing)) {
                loadingDialog.show(supportFragmentManager, "")
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    fun obterUsuarioPreference(): Usuario {
        var usuario = Usuario()
        usuario.id = Integer(
            SharedPreferenceUtils.obterPreferenciaInt(
                this,
                SharedPreferenceUtils.USUARIO_ID
            )
        )
        usuario.login =
            SharedPreferenceUtils.obterPreferencia(this, SharedPreferenceUtils.USUARIO_LOGIN)
        usuario.senha =
            SharedPreferenceUtils.obterPreferencia(this, SharedPreferenceUtils.USUARIO_SENHA)
        usuario.nomeExibicao =
            SharedPreferenceUtils.obterPreferencia(this, SharedPreferenceUtils.USUARIO_NOME)

        return usuario
    }
}