package br.pucminas.teamworktask.ui.privada.tags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentTagsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TagsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TagsFragment : Fragment() {
    private var _binding: FragmentTagsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTagsBinding.inflate(inflater, container, false)


        return binding.root
    }


}