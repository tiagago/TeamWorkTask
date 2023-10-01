package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.TagRequest
import br.pucminas.teamworktask.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TagViewModel constructor(private val repository: Repository)  : GenericViewModel() {
    val tagResponse = MutableLiveData<TagResponse>()
    val tagsResponse = MutableLiveData<TagsResponse>()

    fun criarTag(tagRequest: TagRequest) {
        executeService(repository.criarTag(tagRequest))
    }

    fun editarTag(tagRequest: TagRequest) {
        executeGenericService(repository.editarTag(tagRequest))
    }

    fun obterTagsPorProjeto(projetoId: Int) {
        executeListService(repository.obterTagsPorProjeto(projetoId))
    }

    private fun executeService(response: Call<TagResponse>){
        response.enqueue(object : Callback<TagResponse> {
            override fun onResponse(call: Call<TagResponse>, response: Response<TagResponse>) {
                if(response.body() != null){
                    tagResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<TagResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    private fun executeListService(response: Call<TagsResponse>){
        response.enqueue(object : Callback<TagsResponse> {
            override fun onResponse(call: Call<TagsResponse>, response: Response<TagsResponse>) {
                if(response.body() != null){
                    tagsResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<TagsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}