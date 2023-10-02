package br.pucminas.teamworktask.ui

import br.pucminas.teamworktask.response.GenericResponse

interface BaseFragmentCallback {
    fun retornoErroServico(response: GenericResponse?)

    fun retornoErroServicoReturn(response: GenericResponse?): String
    fun showErrorGenericServer()
    fun showErrorMessage(message: String)
    fun showWarningMessage(message: String)
    fun showSuccessMessage(message: String)
    fun showLoading(isLoading: Boolean)
}