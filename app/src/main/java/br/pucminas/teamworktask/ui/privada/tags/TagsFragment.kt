package br.pucminas.teamworktask.ui.privada.tags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.databinding.FragmentTagsBinding
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.GenericFragment
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.TagViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [TagsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TagsFragment : GenericFragment(), TagItemListOnClickInterface, TagDialogInterface{
    private var _binding: FragmentTagsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val retrofitService = RetrofitService.getInstance()
    lateinit var tagViewModel: TagViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTagsBinding.inflate(inflater, container, false)

        configurarViewModels()
        chamarServicos()
        configurarFloatingButton()
        return binding.root
    }

    private fun configurarViewModels() {
        tagViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TagViewModel::class.java
            )

        tagViewModel.apply {
            tagsResponse.observe(viewLifecycleOwner) {
                if (it?.tags != null && it.success) {
                    exibirTags(it.tags)
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
                showLoading(false)
            }

            errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    showListaVaziaMensagem(false)
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                showLoading(false)
            }
        }
    }

    private fun chamarServicos() {
        val projetoSelecionadoId = SharedPreferenceUtils.obterPreferenciaInt(
            requireContext(),
            SharedPreferenceUtils.PROJETO_ID
        )
        showLoading(true)
        tagViewModel.obterTagsPorProjeto(projetoSelecionadoId)
    }

    private fun exibirTags(tags: List<Tag>?) {
        binding.apply {
            if(tags == null || tags.isEmpty()){
                showListaVaziaMensagem(true)
            } else {
                showListaVaziaMensagem(false)
                tagListaRv.apply {
                    adapter = TagListAdapter(context, tags,  this@TagsFragment)
                }
            }
        }
    }

    private fun showListaVaziaMensagem(isShow: Boolean){
        binding.tabListaVaziaGrupo.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    fun configurarFloatingButton() {
        val projetoSelecionadoId = SharedPreferenceUtils.obterPreferenciaInt(
            requireContext(),
            SharedPreferenceUtils.PROJETO_ID
        )
        binding.apply {
            tagMainFab.setOnClickListener {
                onAddButtonClicked()
            }
            tagNovoFab.setOnClickListener {
                TagCriarDialog(this@TagsFragment, obterUsuarioPreference(),projetoSelecionadoId).show(parentFragmentManager, "")
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
            tagNovoFab.visibility = if (clicked) View.GONE else View.VISIBLE
        }
    }

    private fun setAnimation() {
        binding.apply {
            tagNovoFab.startAnimation(if (clicked) toBottom else fromBottom)
            tagMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
        }
    }

    // IMPLEMENT INTERFACE METHODS CALLBACK
    override fun onClickEditarTag(tag: Tag) {
        val projetoSelecionadoId = SharedPreferenceUtils.obterPreferenciaInt(
            requireContext(),
            SharedPreferenceUtils.PROJETO_ID
        )
        TagCriarDialog(this@TagsFragment, obterUsuarioPreference(), projetoSelecionadoId, tag).show(parentFragmentManager, "")
    }

    override fun showMensagemSucesso(mensagem: String) {
        showSuccessMessage(mensagem)
        chamarServicos()
    }

    override fun showMensagemErro(mensagem: String) {
        showErrorMessage(mensagem)
    }

}