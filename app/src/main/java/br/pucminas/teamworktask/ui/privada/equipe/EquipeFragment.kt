package br.pucminas.teamworktask.ui.privada.equipe

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentEquipeBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.ui.privada.projeto.ProjetoAssociarDialog
import br.pucminas.teamworktask.ui.privada.projeto.ProjetoCadastroFragment
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.PROJETO_ID
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EquipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipeFragment : GenericFragment() {
    private var _binding: FragmentEquipeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    private var projeto: Projeto? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEquipeBinding.inflate(inflater, container, false)

        configurarProjetoViewModels()
        chamarServicos()
        configurarFloatingButton()
        return binding.root
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.projetoResponse.observe(viewLifecycleOwner) {

            if (it?.projeto != null && it.success) {
                projeto = it.projeto
            } else {
                showErrorMessage(retornoErroServicoReturn(it))
            }
            showLoading(false)
        }

        projetoViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                showErrorMessage(it)
                projetoViewModel.errorMessage.postValue(null)
            }
            showLoading(false)
        }
    }

    private fun chamarServicos() {
        showLoading(true)
        val projetoSelecionadoId = SharedPreferenceUtils.obterPreferenciaInt(
            requireContext(),
            PROJETO_ID
        )
        projetoViewModel.obterProjetoComParticipantes(projetoSelecionadoId)
    }

    @SuppressLint("StringFormatMatches")
    fun configurarFloatingButton() {
        binding.equipeMainFab.setOnClickListener {
            onAddButtonClicked()
        }
        binding.equipeCompartilharFab.setOnClickListener {
            compartilharCodigo()
        }
        binding.equipeEmailFab.setOnClickListener {
            enviarEmail()
        }
    }



    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.equipeCompartilharFab.visibility = if (clicked) View.GONE else View.VISIBLE
        binding.equipeCompartilharTv.visibility = if (clicked) View.GONE else View.VISIBLE
        binding.equipeEmailFab.visibility = if (clicked) View.GONE else View.VISIBLE
        binding.equipeEmailTv.visibility = if (clicked) View.GONE else View.VISIBLE
    }

    private fun setAnimation() {
        binding.equipeCompartilharFab.startAnimation(if (clicked) toBottom else fromBottom)
        binding.equipeCompartilharTv.startAnimation(if (clicked) toBottom else fromBottom)
        binding.equipeEmailFab.startAnimation(if (clicked) toBottom else fromBottom)
        binding.equipeEmailTv.startAnimation(if (clicked) toBottom else fromBottom)
        binding.equipeMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
    }

    private fun compartilharCodigo(){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, projeto?.codigo);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.equipe_enviar_compartilhar_titulo_atalho)))
    }

    private fun enviarEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:") // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.equipe_enviar_email_titulo,
            projeto?.nome ?: ""))
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.equipe_enviar_email_doby,
            projeto?.nome ?: "", projeto?.codigo ?: "", projeto?.codigo ?: ""))
        startActivity(Intent.createChooser(emailIntent, getString(R.string.equipe_enviar_email_titulo_atalho)))
    }
}