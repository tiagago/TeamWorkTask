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
import br.pucminas.teamworktask.databinding.LayoutTagCriarDialogBinding
import br.pucminas.teamworktask.models.enums.CorEnum
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.request.TagRequest
import br.pucminas.teamworktask.ui.BaseFragmentCallback
import br.pucminas.teamworktask.ui.GenericActivity
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.TagViewModel

class TagCriarDialog(private val callback: BaseFragmentCallback, private val usuario: Usuario, private val projetoId: Int, private val tagEditar: Tag? = null) : DialogFragment() {
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

        processarTag()
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

    /*****************************
     ******* Processar TAG *******
     *****************************/
    fun processarTag(){
        if(tagEditar == null) {
            return
        }
        binding.apply {
            tagCriarTv.text = getString(R.string.tag_editar_titulo)
            tagNomeTie.setText(tagEditar.nome)

            var listaCoresSelecionadas = CorEnum.entries.filter { it.hexaCor == tagEditar.cor }

            if(listaCoresSelecionadas.isNotEmpty()){
                corSelecionada = listaCoresSelecionadas[0]
                tagCorActv.setText(corSelecionada.toString())
            }
        }
    }

    /*********************************
     **** Controle Listeners View ****
     *********************************/

    private fun carregarCorArrayAdapter() {
        binding.apply {
            tagCorActv.setAdapter(TagCorAdapter(requireContext(),tagCorActv.id, CorEnum.entries))
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

    /***************************************
     **** Validações para salvar/editar ****
     ***************************************/
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
                callback.showLoading(true)

                var novaTag = tagEditar ?: Tag()
                novaTag.nome = binding.tagNomeTie.text.toString()
                novaTag.cor = corSelecionada?.hexaCor.toString()
                var projeto = Projeto()
                projeto.id = Integer(projetoId)
                novaTag.projeto = projeto

                var tagRequest = TagRequest()
                tagRequest.tag = novaTag
                tagRequest.usuario = usuario

                if(tagEditar != null){
                    viewModel.editarTag(tagRequest)
                } else {
                    viewModel.criarTag(tagRequest)
                }
            }
        }
    }

    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/
    private fun configurarViewModels() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TagViewModel::class.java
            )

        viewModel.apply {
            tagResponse.observe(viewLifecycleOwner) {
                callback.showLoading(false)
                if(it?.tag != null && it.success){
                    callback.showSuccessMessage(getString(R.string.tag_salvar_sucesso))
                    dismiss()
                } else {
                    callback.showErrorMessage(it.message)
                }
            }

            genericResponse.observe(viewLifecycleOwner) {
                callback.showLoading(false)
                if(it.success){
                    callback.showSuccessMessage(getString(R.string.tag_editar_sucesso))
                    dismiss()
                } else {
                    callback.showErrorMessage(it.message)
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                callback.showLoading(false)
                callback.showErrorMessage(it)
            }
        }
    }

}