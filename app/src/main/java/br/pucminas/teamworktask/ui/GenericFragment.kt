package br.pucminas.teamworktask.ui

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.GenericResponse

open class GenericFragment : Fragment(), BaseFragmentCallback {
    override fun retornoErroServico(response: GenericResponse?){
        if(response != null && response.message.isNotBlank()){
            showErrorMessage(response.message)
        } else {
            showErrorGenericServer()
        }
    }

    override fun retornoErroServicoReturn(response: GenericResponse?) : String{
        return if(response != null && response.message.isNotBlank()){
            response.message
        } else {
            getString(R.string.generico_erro_servico)
        }
    }

    override fun showErrorGenericServer(){
        showErrorMessage(getString(R.string.generico_erro_servico))
    }

    override fun showErrorMessage(message: String){
        showAlertMessage(TopAlertMessageObject(TopAlertType.ERROR, message))
    }

    override fun showWarningMessage(message: String){
        showAlertMessage(TopAlertMessageObject(TopAlertType.WARNING, message))
    }

    override fun showSuccessMessage(message: String){
        showAlertMessage(TopAlertMessageObject(TopAlertType.SUCCESS, message))
    }

    fun showAlertMessage(topAlertMessageObject: TopAlertMessageObject){
        if (activity is GenericActivity) {
            (activity as GenericActivity).showAlert(topAlertMessageObject)
        }
    }

    fun obterUsuarioPreference(): Usuario {
        return (activity as GenericActivity).obterUsuarioPreference()
    }

    fun onBackPressed(){
        if (activity is GenericActivity) {
            (activity as GenericActivity).onBackPressed()
        }
    }

    fun onBackPressed(topAlertMessageObject: TopAlertMessageObject){
        if (activity is GenericActivity) {
            (activity as GenericActivity).onBackPressed(topAlertMessageObject)
        }
    }

    fun changeFragment(fragment: Fragment, topAlertMessageObject: TopAlertMessageObject? = null){
        if(activity is GenericActivity){
            (activity as GenericActivity).changeFragment(fragment, topAlertMessageObject)
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (activity is GenericActivity) {
            (activity as GenericActivity).showLoading(isLoading)
        }
    }
}