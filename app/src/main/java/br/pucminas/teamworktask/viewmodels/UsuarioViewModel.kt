package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.repositories.UsuarioRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioViewModel constructor(private val repository: UsuarioRepository)  : ViewModel() {

    val usuario = MutableLiveData<Usuario>()
    val errorMessage = MutableLiveData<String>()

    fun doLogin(usuarioRequest: Usuario) {

        val response = repository.doLogin(usuarioRequest)
        response.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                usuario.postValue(response.body())
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}