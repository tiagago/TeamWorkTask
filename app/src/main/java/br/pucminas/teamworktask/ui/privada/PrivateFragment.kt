package br.pucminas.teamworktask.ui.privada

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.GenericResponse
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.ui.GenericFragment

open class PrivateFragment : GenericFragment() {

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

    override fun onResume() {
        super.onResume()
        atualizarTopLayout()
    }

    fun atualizarTopLayout(){
        if (activity is PrivateActivity) {
            (activity as PrivateActivity).atualizarTopLaout(obterIcone(), obterTitulo())
        }
    }

    open fun obterIcone() : Int = 0

    open fun obterTitulo() : String = ""
}