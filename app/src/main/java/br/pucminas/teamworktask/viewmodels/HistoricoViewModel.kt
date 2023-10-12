package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.TagRequest
import br.pucminas.teamworktask.request.TarefaRequest
import br.pucminas.teamworktask.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoricoViewModel constructor(private val repository: Repository)  : GenericViewModel() {

    val historicosResponse = MutableLiveData<HistoricosResponse>()

    fun obterTarefasPorProjetoStatus(projetoId: Int) {
        executeListService(repository.obterHistoricoPorProjeto(projetoId))
    }

    private fun executeListService(response: Call<HistoricosResponse>){
        response.enqueue(object : Callback<HistoricosResponse> {
            override fun onResponse(call: Call<HistoricosResponse>, response: Response<HistoricosResponse>) {
                if(response.body() != null){
                    historicosResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<HistoricosResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}