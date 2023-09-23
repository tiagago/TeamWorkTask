package br.pucminas.teamworktask.componentes.topAlert


import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentTopAlertViewBinding
import java.util.*


class TopAlertView : DialogFragment() {
    private var _binding: FragmentTopAlertViewBinding? = null
    private val binding get() = _binding!!
    var topAlertMessageObject: TopAlertMessageObject = TopAlertMessageObject()
    var mTimer: Int = 5000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTopAlertViewBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TopAlertDialog)
        if (dialog.window != null) {
            dialog.window!!.requestFeature(1)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(true)
        }
        val timer = Timer()

        mTimer = if (topAlertMessageObject.message.length <= 100) {
            5000
        } else {
            10000
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (dialog != null) {
                    try {
                        dismiss()
                    } catch (var2: Exception) {
                        Log.e(TAG, "dismiss ERROR :$var2")
                    }
                }
                timer.cancel()
            }
        }, mTimer.toLong())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.TopAlertWindowSlide
    }

    override fun onResume() {
        super.onResume()

        setUISettings()
        val window = dialog!!.window
        if (window != null) {
            window.clearFlags(2)
            window.setFlags(512, 512)
            window.setFlags(8, 8)
            window.setFlags(262144, 262144)
            window.setLayout(-1, -1)
            window.setBackgroundDrawable(ColorDrawable(0))
            window.setGravity(48)
        }

    }

    private fun setUISettings() {
        setIconFromType()
        setTextUISettings(topAlertMessageObject.message, binding.messageTv)
        setTextUISettings(topAlertMessageObject.title, binding.titleTv)
        setBackgroundColorFromType()
    }

    private fun setTextUISettings(message: String?, textView: TextView) {
        if (message == null || message.isEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = message
        }
    }

    private fun setIconFromType() {
        when (topAlertMessageObject.alertType) {
            TopAlertType.OTHER -> binding.topInfoIcon.visibility = View.GONE
            TopAlertType.INFORMATION -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_info)
            TopAlertType.ERROR -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_error)
            TopAlertType.WARNING -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_warning)
            TopAlertType.SUCCESS -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_success)
            else -> binding.topInfoIcon.visibility = View.GONE
        }
    }

    private fun setBackgroundColorFromType() {
        when (topAlertMessageObject.alertType) {
            TopAlertType.OTHER -> {}
            TopAlertType.INFORMATION -> setBackgroundColorOnLayout(R.color.teal_700)
            TopAlertType.ERROR -> setBackgroundColorOnLayout(R.color.colorError)
            TopAlertType.WARNING -> setBackgroundColorOnLayout(R.color.colorWarning)
            TopAlertType.SUCCESS -> setBackgroundColorOnLayout(R.color.colorSuccess)
            else -> {}
        }
    }

    private fun setBackgroundColorOnLayout(backgroundColor: Int) {
        binding.layoutTopViewContent.backgroundTintList =
            this.resources.getColorStateList(backgroundColor, null)
    }

    companion object {
        val TAG: String = TopAlertView::class.java.simpleName
    }
}