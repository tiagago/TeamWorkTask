package br.pucminas.teamworktask.ui

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.GenericResponse

open class GenericFragment : Fragment() {

    var clicked = false

    val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_anim
        )
    }
    val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_anim
        )
    }
    val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }


    fun retornoErroServico(response: GenericResponse?){
        if(response != null && response.message.isNotBlank()){
            showErrorMessage(response.message)
        } else {
            showErrorGenericServer()
        }
    }

    fun retornoErroServicoReturn(response: GenericResponse?) : String{
        return if(response != null && response.message.isNotBlank()){
            response.message
        } else {
            getString(R.string.generico_erro_servico)
        }
    }

    fun showErrorGenericServer(){
        showErrorMessage(getString(R.string.generico_erro_servico))
    }

    fun showErrorMessage(message: String, titulo: String? = null){
        showAlertMessage(TopAlertMessageObject(TopAlertType.ERROR, message, titulo))
    }

    fun showWarningMessage(message: String, titulo: String? = null){
        showAlertMessage(TopAlertMessageObject(TopAlertType.WARNING, message, titulo))
    }

    fun showSuccessMessage(message: String, titulo: String? = null){
        showAlertMessage(TopAlertMessageObject(TopAlertType.SUCCESS, message, titulo))
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

    fun showLoading(isLoading: Boolean) {
        if (activity is GenericActivity) {
            (activity as GenericActivity).showLoading(isLoading)
        }
    }
}