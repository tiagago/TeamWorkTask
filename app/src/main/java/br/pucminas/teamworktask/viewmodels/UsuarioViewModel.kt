package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.UsuarioRepository
import br.pucminas.teamworktask.response.UsuarioResponse
import br.pucminas.teamworktask.utils.ViewModelUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsuarioViewModel constructor(private val repository: UsuarioRepository)  : ViewModel() {

    val usuarioResponse = MutableLiveData<UsuarioResponse>()
    val errorMessage = MutableLiveData<String>()

    fun doLogin(email: String, senha: String) {

        val response = repository.doLogin(email, senha)
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

    fun criarUsuario(usuario: Usuario) {
        val response = repository.criarUsuario(usuario)
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