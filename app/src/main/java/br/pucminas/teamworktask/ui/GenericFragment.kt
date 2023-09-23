package br.pucminas.teamworktask.ui

import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.response.GenericResponse

open class GenericFragment : Fragment() {

    fun retornoErroServico(response: GenericResponse?){
        if(response != null && response.message.isNotBlank()){
            showErrorMessage(response.message)
        } else {
            showErrorMessage(getString(R.string.generico_erro_servico))
        }
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