package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.TagRequest
import br.pucminas.teamworktask.request.TarefaRequest
import br.pucminas.teamworktask.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TarefaViewModel constructor(private val repository: Repository)  : GenericViewModel() {
    val tarefaResponse = MutableLiveData<TarefaResponse>()
    val tarefasResponse = MutableLiveData<TarefasResponse>()

    fun criarTarefa(tarefaRequest: TarefaRequest) {
        executeService(repository.criarTarefa(tarefaRequest))
    }

    fun editarTarefa(tarefaRequest: TarefaRequest) {
        executeGenericService(repository.editarTarefa(tarefaRequest))
    }

    fun deletarTarefa(tarefaId: Int, nomeTarefa: String, idUsuario: Int, idProjeto: Int) {
        executeGenericService(repository.deletarTarefa(tarefaId, nomeTarefa, idUsuario, idProjeto))
    }

    fun obterTarefasPorProjetoStatus(projetoId: Int, idStatus: Int) {
        executeListService(repository.obterTarefasPorProjetoStatus(projetoId, idStatus))
    }

    private fun executeService(response: Call<TarefaResponse>){
        response.enqueue(object : Callback<TarefaResponse> {
            override fun onResponse(call: Call<TarefaResponse>, response: Response<TarefaResponse>) {
                if(response.body() != null){
                    tarefaResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<TarefaResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    private fun executeListService(response: Call<TarefasResponse>){
        response.enqueue(object : Callback<TarefasResponse> {
            override fun onResponse(call: Call<TarefasResponse>, response: Response<TarefasResponse>) {
                if(response.body() != null){
                    tarefasResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<TarefasResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}