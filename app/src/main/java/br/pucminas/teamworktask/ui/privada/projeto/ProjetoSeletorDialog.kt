package br.pucminas.teamworktask.ui.privada.projeto

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import br.pucminas.teamworktask.databinding.LayoutSeletorProjetoDialogBinding
import br.pucminas.teamworktask.models.Projeto

class ProjetoSeletorDialog(private val listaProjetos: List<Projeto>, private val projetoSelecionado: Projeto, private val projetoSeletorOnClickInterface: ProjetoSeletorOnClickInterface) :
    DialogFragment(), ProjetoSeletorOnClickInterface {
    private var _binding: LayoutSeletorProjetoDialogBinding? = null
    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LayoutSeletorProjetoDialogBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            projetoCardRv.apply {
                adapter = ProjetoSeletorAdapter(context, listaProjetos, projetoSelecionado, this@ProjetoSeletorDialog)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    }

    override fun onClickProjeto(projeto: Projeto) {
        projetoSeletorOnClickInterface.onClickProjeto(projeto)
        dismiss()
    }
}