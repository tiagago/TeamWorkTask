package br.pucminas.teamworktask.ui.privada.tags

import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject

interface TagDialogInterface {
    fun showMensagemSucesso(mensagem: String)
    fun showMensagemErro(mensagem: String)
}