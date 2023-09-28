package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.response.GenericResponse
import br.pucminas.teamworktask.utils.ViewModelUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjetoUsuarioViewModel constructor(private val repository: Repository)  : ViewModel() {
    val genericResponse = MutableLiveData<GenericResponse>()
    val errorMessage = MutableLiveData<String>()

    fun desassociarUsuarioProjeto(idProjeto: Int, idUsuario: Int) {
        executeService(repository.desassociarUsuarioProjeto(idProjeto, idUsuario))
    }

    private fun executeService(response: Call<GenericResponse>){
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