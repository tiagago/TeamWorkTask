package br.pucminas.teamworktask.componentes.topAlert.`object`

import android.graphics.Typeface
import java.io.Serializable


class TopAlertMessageObject : Serializable {
    var title: String? = null
    var message: String = ""
    var alertType: TopAlertType = TopAlertType.OTHER

    constructor(alertType: TopAlertType,message: String, title: String? = null) {
        this.title = title
        this.message = message
        this.alertType = alertType
    }

    constructor()
}