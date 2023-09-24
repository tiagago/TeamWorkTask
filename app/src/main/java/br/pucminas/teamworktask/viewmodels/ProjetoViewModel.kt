package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.response.ProjetoResponse
import br.pucminas.teamworktask.utils.ViewModelUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjetoViewModel constructor(private val repository: Repository)  : ViewModel() {
    val projetoResponse = MutableLiveData<ProjetoResponse>()
    val errorMessage = MutableLiveData<String>()

    fun criarProjeto(projeto: Projeto) {
        executeService(repository.criarProjeto(projeto))

    }

    fun executeService(response: Call<ProjetoResponse>){
        response.enqueue(object : Callback<ProjetoResponse> {
            override fun onResponse(call: Call<ProjetoResponse>, response: Response<ProjetoResponse>) {
                if(response.body() != null){
                    projetoResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(ViewModelUtils.tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<ProjetoResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}