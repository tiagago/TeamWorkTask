package br.pucminas.teamworktask.ui.privada.equipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.databinding.FragmentEquipeBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.ProjetoRequest
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.privada.PrivateFragment
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import br.pucminas.teamworktask.utils.SharedPreferenceUtils.Companion.PROJETO_ID
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoUsuarioViewModel
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [EquipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipeFragment : PrivateFragment(), EquipeParticipanteOnClickInterface {
    private var _binding: FragmentEquipeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    lateinit var projetoUsuarioViewModel: ProjetoUsuarioViewModel
    private var projeto: Projeto? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEquipeBinding.inflate(inflater, container, false)

        configurarViewModels()
        chamarServicos()
        configurarFloatingButton()
        return binding.root
    }

    override fun obterIcone(): Int {
        return R.drawable.ic_equipe
    }
    override fun obterTitulo(): String {
        return getString(R.string.equipe_title)
    }

    private fun configurarViewModels() {
        configurarProjetoViewModels()
        configurarProjetoUsuarioViewModels()
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.apply {
            projetoResponse.observe(viewLifecycleOwner) {

                if (it?.projeto != null && it.success) {
                    projeto = it.projeto

                    exibirParticipantes()
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                    binding.equipeMainFab.visibility = View.GONE
                }
                showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                showLoading(false)
                binding.equipeMainFab.visibility = View.GONE
            }
        }
    }

    private fun configurarProjetoUsuarioViewModels() {
        projetoUsuarioViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoUsuarioViewModel::class.java
            )
        projetoUsuarioViewModel.apply {
            genericResponse.observe(viewLifecycleOwner) {

                if (it != null && it.success) {
                    showSuccessMessage(getString(R.string.equipe_remover_usuario_sucesso))
                    chamarServicos()
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
                showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                showLoading(false)
            }
        }
    }

    private fun exibirParticipantes() {
        binding.apply {
            equipeParticipanteRv.apply {
                var listaParticipantes: ArrayList<Usuario> = ArrayList()
                var isProprietario = false
                projeto?.let {
                    var usuario = obterUsuarioPreference()
                    listaParticipantes.add(it.usuario)
                    listaParticipantes.addAll(it.associados)
                    isProprietario = it.usuario.id == usuario.id
                }

                equipeMainFab.visibility = if(isProprietario) View.VISIBLE else View.GONE

                adapter = EquipeParticipantesAdapter(context, listaParticipantes, isProprietario, this@EquipeFragment)
            }
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

    private fun chamarProjetoUsuarioServicos(usuario: Usuario) {
        showLoading(true)
        val projetoSelecionadoId = SharedPreferenceUtils.obterPreferenciaInt(
            requireContext(),
            PROJETO_ID
        )

        var projetoRequest = ProjetoRequest()
        var projeto = Projeto()
        projeto.id = Integer(projetoSelecionadoId)
        projetoRequest.usuario = usuario
        projetoRequest.projeto = projeto

        projetoUsuarioViewModel.desassociarUsuarioProjeto(projetoRequest)
    }

    fun configurarFloatingButton() {
        binding.apply {
            equipeMainFab.setOnClickListener {
                onAddButtonClicked()
            }
            equipeCompartilharFab.setOnClickListener {
                compartilharCodigo()
            }
            equipeEmailFab.setOnClickListener {
                enviarEmail()
            }
        }
    }

    private fun onAddButtonClicked() {
        setVisibility()
        setAnimation()
        clicked = !clicked
    }

    private fun setVisibility() {
        binding.apply {
            equipeCompartilharFab.visibility = if (clicked) View.GONE else View.VISIBLE
            equipeEmailFab.visibility = if (clicked) View.GONE else View.VISIBLE
        }
    }

    private fun setAnimation() {
        binding.apply {
            equipeCompartilharFab.startAnimation(if (clicked) toBottom else fromBottom)
            equipeEmailFab.startAnimation(if (clicked) toBottom else fromBottom)
            equipeMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
        }
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

    override fun onClickExcluirParticipante(usuario: Usuario) {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            // Set the message show for the Alert time
            setTitle(getString(R.string.equipe_remover_usuario_title))

            // Set Alert Title
            setMessage(getString(R.string.equipe_remover_usuario_descricao, usuario.nomeExibicao))

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            setCancelable(false)

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            setPositiveButton(R.string.generico_confirmar) {
                // When the user click yes button then app will close
                    dialog, which -> chamarProjetoUsuarioServicos(usuario)
            }

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            setNegativeButton(R.string.generico_negar) {
                // If user click no then dialog box is canceled.
                    dialog, which ->
                dialog.cancel()
            }
        }

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }
}