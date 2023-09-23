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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertTextObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentTopAlertViewBinding
import java.util.*


class TopAlertView : DialogFragment() {
    private var _binding: FragmentTopAlertViewBinding? = null
    private val binding get() = _binding!!

    var mTopAlertType: TopAlertType? = null
    var mTitleInfo: TopAlertTextObject? = null
    var mMessageInfo: TopAlertTextObject? = null
    var mIconDrawable: Drawable? = null
    var mIconVisible: Boolean = true
    var mTimer: Int? = null
    var mBackgroundColor: Int? = null
    private var dialog: Dialog? = null
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTopAlertType = TopAlertType.fromId(this.requireArguments().getInt("type"))
        mMessageInfo = this.requireArguments().getSerializable("message") as TopAlertTextObject?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTopAlertViewBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = Dialog(requireContext(), R.style.TopAlertDialog)
        if (dialog!!.window != null) {
            dialog!!.window!!.requestFeature(1)
        }
        return dialog!!
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog!!.setCancelable(true)
            dialog!!.setCanceledOnTouchOutside(true)
        }
        val t = Timer()
        if (mTimer == null) {
            if (mMessageInfo != null && mMessageInfo!!.textContent != null && mMessageInfo!!.textContent.length <= 100) {
                mTimer = 5000
            } else {
                mTimer = 10000
            }
        }
        t.schedule(object : TimerTask() {
            override fun run() {
                if (dialog != null) {
                    try {
                        dismiss()
                    } catch (var2: Exception) {
                        Log.e(TAG, "dismiss ERROR :$var2")
                    }
                }
                t.cancel()
            }
        }, mTimer!!.toLong())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDialog()!!.window!!.attributes.windowAnimations = R.style.TopAlertWindowSlide
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
        if (mIconDrawable != null) {
            binding.topInfoIcon.background = mIconDrawable
        } else {
            setIconFromType()
        }
        TopAlertUtils.setTextUISettings(mMessageInfo, binding.messageTv, requireContext())
        TopAlertUtils.setTextUISettings(mTitleInfo, binding.titleTv, requireContext())
        if (!mIconVisible) {
            binding.topInfoIcon.visibility = View.GONE
        }
        if (mBackgroundColor != null) {
            setBackgroundColorOnLayout(mBackgroundColor!!)
        } else {
            setBackgroundColorFromType()
        }
    }

    private fun setIconFromType() {
        when (mTopAlertType) {
            TopAlertType.OTHER -> binding.topInfoIcon.visibility = View.GONE
            TopAlertType.INFORMATION -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_info)
            TopAlertType.ERROR -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_error)
            TopAlertType.WARNING -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_warning)
            TopAlertType.SUCCESS -> binding.topInfoIcon.background = this.resources.getDrawable(R.drawable.ic_success)
            else -> binding.topInfoIcon.visibility = View.GONE
        }
    }

    private fun setBackgroundColorFromType() {
        when (mTopAlertType) {
            TopAlertType.OTHER -> {}
            TopAlertType.INFORMATION -> setBackgroundColorOnLayout(R.color.teal_700)
            TopAlertType.ERROR -> setBackgroundColorOnLayout(R.color.colorError)
            TopAlertType.WARNING -> setBackgroundColorOnLayout(R.color.colorWarning)
            TopAlertType.SUCCESS -> setBackgroundColorOnLayout(R.color.colorSuccess)
            else -> {}
        }
    }

    private fun setBackgroundColorOnLayout(backgroundColor: Int) {
        if (VERSION.SDK_INT >= 23) {
            binding.layoutTopViewContent.setBackgroundColor(this.resources.getColor(backgroundColor, null))
        } else {
            binding.layoutTopViewContent.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    backgroundColor
                )
            )
        }
    }



    companion object {
        val TAG = TopAlertView::class.java.simpleName
        fun createTopAlert(type: Int, message: TopAlertTextObject): TopAlertView {
            val topInfoFragment = TopAlertView()
            val args = Bundle()
            args.putInt("type", type)
            args.putSerializable("message", message)
            topInfoFragment.arguments = args
            return topInfoFragment
        }
    }
}