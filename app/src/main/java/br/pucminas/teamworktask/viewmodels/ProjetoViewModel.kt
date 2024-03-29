package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.ProjetoRequest
import br.pucminas.teamworktask.response.ProjetoResponse
import br.pucminas.teamworktask.response.ProjetosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjetoViewModel constructor(private val repository: Repository) : GenericViewModel() {
    val projetoResponse = MutableLiveData<ProjetoResponse>()
    val projetosResponse = MutableLiveData<ProjetosResponse>()

    fun criarProjeto(projetoRequest: ProjetoRequest) {
        executeService(repository.criarProjeto(projetoRequest))
    }

    fun editarProjeto(projetoRequest: ProjetoRequest) {
        executeGenericService(repository.editarProjeto(projetoRequest))
    }

    fun oberMeusProjetos(idUsuario: Int) {
        executeListService(repository.oberMeusProjetos(idUsuario))
    }

    fun obterProjetoComParticipantes(idProjeto: Int) {
        executeService(repository.obterProjetoComParticipantes(idProjeto))
    }

    fun obterProjetoPorCodigo(codigo: String) {
        executeListService(repository.obterProjetoPorCodigo(codigo))
    }

    private fun executeService(response: Call<ProjetoResponse>){
        response.enqueue(object : Callback<ProjetoResponse> {
            override fun onResponse(call: Call<ProjetoResponse>, response: Response<ProjetoResponse>) {
                if(response.body() != null){
                    projetoResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<ProjetoResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    private fun executeListService(response: Call<ProjetosResponse>){
        response.enqueue(object : Callback<ProjetosResponse> {
            override fun onResponse(call: Call<ProjetosResponse>, response: Response<ProjetosResponse>) {
                if(response.body() != null){
                    projetosResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<ProjetosResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}