package br.pucminas.teamworktask.ui.privada.tarefa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentTarefaCadastroBinding
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Tarefa
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.models.enums.PrioridadeEnum
import br.pucminas.teamworktask.models.enums.StatusEnum
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.request.TarefaRequest
import br.pucminas.teamworktask.ui.privada.PrivateFragment
import br.pucminas.teamworktask.utils.FormatterUtils
import br.pucminas.teamworktask.utils.GeradorCodigo
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.ProjetoViewModel
import br.pucminas.teamworktask.viewmodels.TagViewModel
import br.pucminas.teamworktask.viewmodels.TarefaViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TarefaCadastroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TarefaCadastroFragment(var editarTarefa: Tarefa? = null) : PrivateFragment() {
    private var _binding: FragmentTarefaCadastroBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var projeto: Projeto = Projeto()
    var tags: ArrayList<Tag> = ArrayList()
    var listaParticipantes: ArrayList<Usuario> = ArrayList()
    private val retrofitService = RetrofitService.getInstance()
    lateinit var projetoViewModel: ProjetoViewModel
    lateinit var tagViewModel: TagViewModel
    lateinit var tarefaViewModel: TarefaViewModel
    var contServicos = 0

    var prioridadeSelecionada: PrioridadeEnum? = null
    var statusSelecionada: StatusEnum? = null
    var responsavelSelecionado: Usuario? = null
    var tagSelecionada: Tag? = null
    var dataEntregaSelecionada: Date? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTarefaCadastroBinding.inflate(inflater, container, false)

        if(editarTarefa != null) {
            projeto.id = editarTarefa!!.id
            projeto.dataCriacao = editarTarefa!!.dataCriacao

        } else {
            projeto.dataCriacao = Date()
            projeto.codigo = GeradorCodigo.geraCodigoProjeto()
        }

        projeto.usuario = obterUsuarioPreference()

        configurarViewModels()
        chamarServicosIniciais()
        configurarTextViews()
        prepararListeners()
        return binding.root
    }

    fun configurarTextViews(){
        binding.apply {
            if(editarTarefa != null){
                //projetoCadastroNomeTie.setText(editarTarefa!!.nome)
                //projetoCadastroDescricaoTie.setText(editarTarefa!!.descricao)
            }

        }
    }

    override fun obterIcone(): Int {
        return R.drawable.ic_tarefa_novo
    }
    override fun obterTitulo(): String {
        return getString(R.string.tarefa_cadastro_titulo)
    }

    /*********************************
     **** Controle Listeners View ****
     *********************************/

    fun prepararListeners(){
        binding.apply {
            tarefaCadastroDataEntregaTie.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.addOnPositiveButtonClickListener {
                    var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(it);
                    tarefaCadastroDataEntregaTie.setText(FormatterUtils.formatDateToString(calendar.time))
                    dataEntregaSelecionada = calendar.time
                }

                datePicker.show(parentFragmentManager, "")
            }

            tarefaCadastroPrioridadeActv.setAdapter(ArrayAdapter(requireContext(), R.layout.select_dialog_material, PrioridadeEnum.values()))
            tarefaCadastroPrioridadeActv.setOnItemClickListener { _, _, position, _ ->
                prioridadeSelecionada = PrioridadeEnum.values()[position]
            }


            tarefaCadastroStatusActv.setAdapter(ArrayAdapter(requireContext(), R.layout.select_dialog_material, StatusEnum.values()))
            tarefaCadastroStatusActv.setOnItemClickListener { _, _, position, _ ->
                statusSelecionada = StatusEnum.values()[position]
            }

            tarefaCadastroCancelarBt.setOnClickListener {
                onBackPressed()
            }

            tarefaCadastroSalvarBt.setOnClickListener {
                salvarTarefa()
            }
        }
    }

    /******************************
     **** Chamadas de Serviços ****
     ******************************/

    private fun chamarServicosIniciais() {
        showLoading(true)
        contServicos = 2
        tagViewModel.obterTagsPorProjeto(obterProjetoSelecionado())
        projetoViewModel.obterProjetoComParticipantes(obterProjetoSelecionado())
    }

    fun processarRetornosServico() {
        if (contServicos > 0) {
            return
        }

        binding.apply {
            listaParticipantes.add(projeto.usuario)
            listaParticipantes.addAll(projeto.associados)
            tarefaCadastroResponsavelActv.setAdapter(ArrayAdapter(requireContext(), R.layout.select_dialog_material, listaParticipantes))
            tarefaCadastroResponsavelActv.setOnItemClickListener { _, _, position, _ ->
                responsavelSelecionado = listaParticipantes[position]
            }

            if(tags.isNotEmpty()){
                tarefaCadastroTagActv.setAdapter(ArrayAdapter(requireContext(), R.layout.select_dialog_material, tags))
                tarefaCadastroTagActv.setOnItemClickListener { _, _, position, _ ->
                    tagSelecionada = tags[position]
                }
            }
        }

        showLoading(false)
    }


    /***************************************
     **** Validações para salvar/editar ****
     ***************************************/
    private fun salvarTarefa() {
        binding.apply {

            var achouProblema = false
            val nome: String = tarefaCadastroNomeTie.text.toString()
            val descricao: String = tarefaCadastroDescricaoTie.text.toString()

            if(nome.isBlank() || nome == getString(R.string.tarefa_cadastro_nome_label)){
                tarefaCadastroNomeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tarefa_cadastro_nome_label))
                achouProblema = true
            } else {
                tarefaCadastroNomeTil.error = null
            }

            if(descricao.isBlank() || descricao == getString(R.string.tarefa_cadastro_descricao_label)){
                tarefaCadastroDescricaoTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tarefa_cadastro_descricao_label))
                achouProblema = true
            } else {
                tarefaCadastroDescricaoTil.error = null
            }

            if(dataEntregaSelecionada == null) {
                tarefaCadastroDataEntregaTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tarefa_cadastro_data_entrega_label))
                achouProblema = true
            } else {
                tarefaCadastroDataEntregaTil.error = null
            }

            if(prioridadeSelecionada == null){
                tarefaCadastroPrioridadeTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tarefa_cadastro_prioridade_label))
                achouProblema = true
            } else {
                tarefaCadastroPrioridadeTil.error = null
            }

            if(statusSelecionada == null){
                tarefaCadastroStatusTil.error = getString(R.string.generico_vazio_erro, getString(R.string.tarefa_cadastro_status_label))
                achouProblema = true
            } else {
                tarefaCadastroStatusTil.error = null
            }

            if(!achouProblema){
                var tarefaRequest = TarefaRequest()
                var tarefa = Tarefa()
                tarefa.nome = nome
                tarefa.descricao = descricao
                tarefa.prioridade = Integer(prioridadeSelecionada!!.id)
                tarefa.status = Integer(statusSelecionada!!.id)
                tarefa.usuario = responsavelSelecionado
                tarefa.tag = tagSelecionada
                tarefa.dataEntrega = dataEntregaSelecionada
                tarefa.projeto = Projeto(Integer(obterProjetoSelecionado()))
                tarefaRequest.tarefa = tarefa
                tarefaRequest.usuario = obterUsuarioPreference()

                showLoading(true)
                tarefaViewModel.criarTarefa(tarefaRequest)
            }
        }
    }


    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/

    private fun configurarViewModels() {
        configurarProjetoViewModels()
        configurarTagViewModels()
        configurarTarefaViewModels()
    }

    private fun configurarProjetoViewModels() {
        projetoViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                ProjetoViewModel::class.java
            )

        projetoViewModel.apply {
            projetoResponse.observe(viewLifecycleOwner) {
                contServicos--
                if (it?.projeto != null && it.success) {
                    projeto = it.projeto!!
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
                processarRetornosServico()
            }

            errorMessage.observe(viewLifecycleOwner) {
                contServicos--
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                processarRetornosServico()
            }
        }
    }


    private fun configurarTagViewModels() {
        tagViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TagViewModel::class.java
            )

        tagViewModel.apply {
            tagsResponse.observe(viewLifecycleOwner) {
                contServicos--
                if (it?.tags != null && it.success) {
                    tags.addAll(it.tags!!)
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
                processarRetornosServico()
            }

            errorMessage.observe(viewLifecycleOwner) {
                contServicos--
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
                processarRetornosServico()
            }
        }
    }

    private fun configurarTarefaViewModels() {
        tarefaViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TarefaViewModel::class.java
            )

        tarefaViewModel.apply {
            tarefaResponse.observe(viewLifecycleOwner){
                showLoading(false)
                if(it.success){
                    onBackPressed(TopAlertMessageObject(TopAlertType.SUCCESS, getString(R.string.tarefa_cadastro_sucesso)))
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
            }

            genericResponse.observe(viewLifecycleOwner) {
                showLoading(false)
                if(it != null && it.success){
                    onBackPressed(TopAlertMessageObject(TopAlertType.SUCCESS, getString(R.string.tarefa_editar_sucesso)))
                } else {
                    retornoErroServico(it)
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                showLoading(false)
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                }
            }
        }

    }
}