package br.pucminas.teamworktask.privada.ui.equipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentEquipeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EquipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipeFragment : Fragment() {
    private var _binding: FragmentEquipeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEquipeBinding.inflate(inflater, container, false)


        return binding.root
    }
}