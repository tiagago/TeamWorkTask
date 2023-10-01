package br.pucminas.teamworktask.ui.privada.tarefa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentTarefasBinding
import br.pucminas.teamworktask.ui.privada.PrivateFragment

/**
 * A simple [Fragment] subclass.
 * Use the [TarefasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TarefasFragment : PrivateFragment() {
    private var _binding: FragmentTarefasBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTarefasBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun obterIcone(): Int {
        return R.drawable.ic_tarefas
    }
    override fun obterTitulo(): String {
        return getString(R.string.tarefas_title)
    }
}