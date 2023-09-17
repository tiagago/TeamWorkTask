package br.pucminas.teamworktask.ui.privada.projeto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentDashboardBinding



/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim) }
    private var clicked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)


        binding.dashboardMainFab.setOnClickListener {
            onAddButtonClicked()
        }

        return binding.root
    }

    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.dashboardNovoFab.visibility =  if(clicked) View.GONE else View.VISIBLE
        binding.dashboardEditarFab.visibility =  if(clicked) View.GONE else View.VISIBLE
        binding.dashboardParticiparFab.visibility =  if(clicked) View.GONE else View.VISIBLE
        binding.dashboardNovoTv.visibility =  if(clicked) View.GONE else View.VISIBLE
        binding.dashboardEditarTv.visibility =  if(clicked) View.GONE else View.VISIBLE
        binding.dashboardParticiparTv.visibility =  if(clicked) View.GONE else View.VISIBLE
    }

    private fun setAnimation() {
        binding.dashboardNovoFab.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardEditarFab.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardParticiparFab.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardNovoTv.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardEditarTv.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardParticiparTv.startAnimation(if(clicked) toBottom else fromBottom)
        binding.dashboardMainFab.startAnimation(if(clicked) rotateClose else rotateOpen)
    }


    fun configurarFloatingButton(){
        //binding.floatingActionButton.
    }

}