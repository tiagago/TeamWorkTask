package br.pucminas.teamworktask.ui.privada.tags

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.LayoutTagCriarDialogBinding
import br.pucminas.teamworktask.models.CorEnum
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.request.TagRequest
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.TagViewModel

class TagCriarDialog(private val tagDialogInterface: TagDialogInterface, private val usuario: Usuario, private val projetoId: Int, private val tagEditar: Tag? = null) : DialogFragment() {
    private var _binding: LayoutTagCriarDialogBinding? = null
    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!
    private val retrofitService = RetrofitService.getInstance()
    lateinit var viewModel: TagViewModel
    private var corSelecionada: CorEnum? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LayoutTagCriarDialogBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(tagEditar != null){
            binding.tagCriarTv.text = getString(R.string.tag_editar_titulo)
            binding.tagNomeTie.setText(tagEditar.nome)
            corSelecionada = CorEnum.getCorEnumByHexaCor(tagEditar.cor)
            if(corSelecionada != null){
                binding.tagCorActv.setText(corSelecionada.toString())
            }
        }

        configurarViewModels()
        carregarCorArrayAdapter()
        configurarListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }

    private fun carregarCorArrayAdapter() {
        binding.apply {
            tagCorActv.setAdapter(TagCorAdapter(requireContext(),tagCorActv.id, CorEnum.values().toList()))
        }
    }

    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TagViewModel::class.java
            )

        viewModel.apply {
            tagResponse.observe(viewLifecycleOwner) {
                showLoading(false)
                if(it?.tag != null && it.success){
                    tagDialogInterface.showMensagemSucesso(getString(R.string.tag_salvar_sucesso))
                    dismiss()
                } else {
                    tagDialogInterface.showMensagemErro(it.message)
                }
            }

            genericResponse.observe(viewLifecycleOwner) {
                showLoading(false)
                if(it.success){
                    tagDialogInterface.showMensagemSucesso(getString(R.string.tag_editar_sucesso))
                    dismiss()
                } else {
                    tagDialogInterface.showMensagemErro(it.message)
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                showLoading(false)
                tagDialogInterface.showMensagemErro(it)
            }
        }
    }

    private fun configurarListeners(){
        binding.apply {
            tagSalvarBt.setOnClickListener {
                salvarTag()
            }

            tagCancelarBt.setOnClickListener {
                dismiss()
            }

            tagCorActv.setOnItemClickListener { adapterView, view, position, l ->
                corSelecionada = CorEnum.values().toList().get(position)
            }
        }
    }

    private fun salvarTag() {
        binding.apply {
            var achouProblema = false
            val nome: String = tagNomeTie.text.toString()

            if(nome.isBlank() || nome == getString(R.string.tags_nome_label)){
                tagNomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tags_nome_label))
                achouProblema = true
            } else {
                tagNomeTil.error = null
            }

            if(corSelecionada == null){
                tagCorTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tags_cor_label))
                achouProblema = true
            } else {
                tagCorTil.error = null
            }

            if(!achouProblema){
                showLoading(true)
                if(tagEditar != null){
                    tagEditar.nome = nome
                    tagEditar.cor = corSelecionada?.hexaCor.toString()
                    var tagRequest = TagRequest()
                    tagRequest.tag = tagEditar
                    tagRequest.usuario = usuario

                    viewModel.editarTag(tagRequest)
                } else {
                    criarNovaTag()
                }
            }
        }
    }

    private fun showLoading(isShow : Boolean){
        if(activity is GenericActivity){
            (activity as GenericActivity).showLoading(isShow)
        }
    }
    private fun criarNovaTag() {
        var novaTag = Tag()
        novaTag.nome = binding.tagNomeTie.text.toString()
        novaTag.cor = corSelecionada?.hexaCor.toString()
        var projeto = Projeto()
        projeto.id = Integer(projetoId)
        novaTag.projeto = projeto

        var tagRequest = TagRequest()
        tagRequest.tag = novaTag
        tagRequest.usuario = usuario

        viewModel.criarTag(tagRequest)
    }

}