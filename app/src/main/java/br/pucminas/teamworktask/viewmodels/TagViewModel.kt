package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.response.*
import br.pucminas.teamworktask.utils.ViewModelUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TagViewModel constructor(private val repository: Repository)  : ViewModel() {
    val tagResponse = MutableLiveData<TagResponse>()
    val tagsResponse = MutableLiveData<TagsResponse>()
    val genericResponse = MutableLiveData<GenericResponse>()
    val errorMessage = MutableLiveData<String>()

    fun criarProjeto(tag: Tag) {
        executeService(repository.criarTag(tag))
    }

    fun obterTagsPorProjeto(projetoId: Int) {
        executeListService(repository.obterTagsPorProjeto(projetoId))
    }

    fun editarTag(tag: Tag) {
        executeGenericervice(repository.editarTag(tag))
    }

    private fun executeService(response: Call<TagResponse>){
        response.enqueue(object : Callback<TagResponse> {
            override fun onResponse(call: Call<TagResponse>, response: Response<TagResponse>) {
                if(response.body() != null){
                    tagResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(ViewModelUtils.tratarJsonRespostaErro(response.errorBody()))
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
                    errorMessage.postValue(ViewModelUtils.tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<TagsResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    private fun executeGenericervice(response: Call<GenericResponse>){
        response.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if(response.body() != null){
                    genericResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(ViewModelUtils.tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}