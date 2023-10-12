package br.pucminas.teamworktask.ui.privada.tarefa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.R
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertMessageObject
import br.pucminas.teamworktask.componentes.topAlert.`object`.TopAlertType
import br.pucminas.teamworktask.databinding.FragmentTarefasBinding
import br.pucminas.teamworktask.models.Tarefa
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.ui.privada.PrivateFragment
import br.pucminas.teamworktask.ui.privada.tags.TagCriarDialog
import br.pucminas.teamworktask.utils.SharedPreferenceUtils
import br.pucminas.teamworktask.viewmodels.MainViewModelFactory
import br.pucminas.teamworktask.viewmodels.TarefaViewModel
import com.google.android.material.tabs.TabLayout

/**
 * A simple [Fragment] subclass.
 * Use the [TarefasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TarefasFragment : PrivateFragment(), TarefaItemListOnClickInterface {
    private var _binding: FragmentTarefasBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val retrofitService = RetrofitService.getInstance()
    lateinit var tarefaViewModel: TarefaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTarefasBinding.inflate(inflater, container, false)

        configurarFloatingButton()
        configurarViewModels()
        configurarTabView()
        chamarServico()
        return binding.root
    }

    override fun obterIcone(): Int {
        return R.drawable.ic_tarefas
    }
    override fun obterTitulo(): String {
        return getString(R.string.tarefas_title)
    }

    private fun atualizarListaTarefas(tarefas: List<Tarefa>) {
        binding.apply {
            tarefaListaRv.apply {
                adapter = TarefaListAdapter(requireContext(), tarefas, this@TarefasFragment)
            }
        }
    }

    /*****************************************
     **** Configuração do floating Button ****
     *****************************************/

    fun configurarFloatingButton() {
        binding.apply {
            tarefaMainFab.setOnClickListener {
                onAddButtonClicked()
            }
            tarefaNovoFab.setOnClickListener {
                changeFragment(TarefaCadastroFragment())
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
            tarefaNovoFab.visibility = if (clicked) View.GONE else View.VISIBLE
        }
    }

    private fun setAnimation() {
        binding.apply {
            tarefaNovoFab.startAnimation(if (clicked) toBottom else fromBottom)
            tarefaMainFab.startAnimation(if (clicked) rotateClose else rotateOpen)
        }
    }


    /********************************************
     **** Controle de Visibilidade das Views ****
     ********************************************/
    fun configurarTabView(){
        binding.apply {
            tarafaTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    chamarServico()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }
    }


    /******************************
     **** Chamadas de Serviços ****
     ******************************/

    private fun chamarServico() {
        showLoading(true)
        tarefaViewModel.obterTarefasPorProjetoStatus(obterProjetoSelecionado(), binding.tarafaTl.selectedTabPosition)
    }


    /**************************************
     **** Configurações dos ViewModels ****
     **************************************/
    private fun configurarViewModels() {
        tarefaViewModel =
            ViewModelProvider(this, MainViewModelFactory(Repository(retrofitService))).get(
                TarefaViewModel::class.java
            )

        tarefaViewModel.apply {
            tarefasResponse.observe(viewLifecycleOwner){
                showLoading(false)
                if(it.success){
                    if(it.tarefas != null && it.tarefas!!.isNotEmpty()){
                        atualizarListaTarefas(it.tarefas!!)
                    } else {
                        atualizarListaTarefas(ArrayList())
                    }
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                    atualizarListaTarefas(ArrayList())
                }

            }

            genericResponse.observe(viewLifecycleOwner){
                showLoading(false)
                if(it.success){
                    showSuccessMessage("Tarefa Excluida com sucesso!")
                    chamarServico()
                } else {
                    showErrorMessage(retornoErroServicoReturn(it))
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                showLoading(false)
                if (it != null) {
                    showErrorMessage(it)
                    errorMessage.postValue(null)
                    atualizarListaTarefas(ArrayList())
                }
            }
        }
    }

    /******************************************
     **** Callbacks do adapter das Tarefas ****
     ******************************************/

    override fun onClickEditarTarefa(tarefa: Tarefa) {
        changeFragment(TarefaCadastroFragment(tarefa))
    }

    override fun onClickExcluirTarefa(tarefa: Tarefa) {
        showLoading(true)
        tarefaViewModel.deletarTarefa(tarefa.id.toInt())
    }

}