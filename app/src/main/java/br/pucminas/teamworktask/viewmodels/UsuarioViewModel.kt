package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.response.UsuarioResponse
import br.pucminas.teamworktask.utils.ViewModelUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsuarioViewModel constructor(private val repository: Repository)  : ViewModel() {
    val usuarioResponse = MutableLiveData<UsuarioResponse>()
    val errorMessage = MutableLiveData<String>()

    fun doLogin(email: String, senha: String) {
        executeService(repository.doLogin(email, senha))
    }

    fun criarUsuario(usuario: Usuario) {
        executeService(repository.criarUsuario(usuario))
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
                    errorMessage.postValue(ViewModelUtils.tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

}