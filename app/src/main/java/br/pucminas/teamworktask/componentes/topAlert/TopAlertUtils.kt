package br.pucminas.teamworktask.componentes.topAlert

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.Typeface
import android.os.Build.VERSION
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertTextObject

internal object TopAlertUtils {
    fun setTextUISettings(
        topAlertTextObject: TopAlertTextObject?,
        textView: TextView,
        context: Context) {
        if (topAlertTextObject == null) {
            textView.visibility = View.GONE
        } else {
            textView.text = topAlertTextObject.textContent
            if (VERSION.SDK_INT >= 23) {
                if (topAlertTextObject.textAppearanceStyle != null) {
                    textView.setTextAppearance(topAlertTextObject.textAppearanceStyle!!)
                }
                if (topAlertTextObject.textColor != null) {
                    textView.setTextColor(
                        context.resources.getColor(
                            topAlertTextObject.textColor!!,
                            null as Theme?
                        )
                    )
                }
            } else {
                if (topAlertTextObject.textAppearanceStyle != null) {
                    textView.setTextAppearance(context, topAlertTextObject.textAppearanceStyle!!)
                }
                if (topAlertTextObject.textColor != null) {
                    textView.setTextColor(
                        ContextCompat.getColor(
                            context,
                            topAlertTextObject.textColor!!
                        )
                    )
                }
            }
            if (topAlertTextObject.textFont != null) {
                textView.typeface = topAlertTextObject.textFont
            }
            if (topAlertTextObject.textMaxLines != null) {
                textView.maxLines = topAlertTextObject.textMaxLines!!
            }
            if (topAlertTextObject.textStyle != null) {
                textView.setTypeface(null as Typeface?, topAlertTextObject.textStyle!!)
            }
            if (topAlertTextObject.textSize != null) {
                textView.setTextSize(1, topAlertTextObject.textSize as Float)
            }
        }
    }

}