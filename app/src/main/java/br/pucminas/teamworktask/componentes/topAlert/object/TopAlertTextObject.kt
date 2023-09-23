package br.pucminas.teamworktask.componentes.topAlert.`object`

import android.graphics.Typeface
import java.io.Serializable


class TopAlertTextObject : Serializable {
    var textContent: String
    var textColor: Int? = null
    var textSize: Int? = null
    var textStyle: Int? = null
    var textMaxLines: Int? = null
    var textFont: Typeface? = null
    var textAppearanceStyle: Int? = null

    constructor(textContent: String) {
        this.textContent = textContent
    }

    constructor(
        textContent: String,
        textColor: Int?,
        textSize: Int?,
        textStyle: Int?,
        textMaxLines: Int?,
        textFont: Typeface?,
        textAppearanceStyle: Int?
    ) {
        this.textContent = textContent
        this.textColor = textColor
        this.textSize = textSize
        this.textStyle = textStyle
        this.textMaxLines = textMaxLines
        this.textFont = textFont
        this.textAppearanceStyle = textAppearanceStyle
    }

    override fun toString(): String {
        return ""
    }
}