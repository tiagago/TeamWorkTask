package br.pucminas.teamworktask.ui.privada.notificacao

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentNotificacaoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NotificacaoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificacaoFragment : Fragment() {
    private var _binding: FragmentNotificacaoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificacaoBinding.inflate(inflater, container, false)


        return binding.root
    }
}