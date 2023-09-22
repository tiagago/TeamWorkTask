package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.UsuarioResponse
import br.pucminas.teamworktask.utils.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @GET("/api/login/")
    fun doLogin(@Query(value="email", encoded=true) email: String, @Query(value="senha", encoded=true) senha: String): Call<UsuarioResponse>

    @POST("/api/usuario/")
    fun criarUsuario(@Body usuario: Usuario): Call<UsuarioResponse>

    companion object {

        private val retrofitService: RetrofitService by lazy {

            val retrofit = Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)

        }

        fun getInstance() : RetrofitService {
            return retrofitService
        }
    }

}