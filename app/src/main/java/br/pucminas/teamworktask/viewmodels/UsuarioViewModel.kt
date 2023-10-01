package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.GenericRequest
import br.pucminas.teamworktask.response.UsuarioResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsuarioViewModel constructor(private val repository: Repository)  : GenericViewModel() {
    val usuarioResponse = MutableLiveData<UsuarioResponse>()

    fun doLogin(login: String, senha: String) {
        executeService(repository.doLogin(login, senha))
    }

    fun criarUsuario(genericRequest: GenericRequest) {
        executeService(repository.criarUsuario(genericRequest))
    }

    fun obterUsuarioComProjetos(idUsuario: Int) {
        executeService(repository.obterUsuarioComProjetos(idUsuario))
    }

    fun executeService(response: Call<UsuarioResponse>){
        response.enqueue(object : Callback<UsuarioResponse> {
            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                if(response.body() != null){
                    usuarioResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

}