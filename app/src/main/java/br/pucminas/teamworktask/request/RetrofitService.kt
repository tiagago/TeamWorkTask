package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.ProjetoResponse
import br.pucminas.teamworktask.response.ProjetosResponse
import br.pucminas.teamworktask.response.UsuarioResponse
import br.pucminas.teamworktask.utils.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    // Chamadas do usuário.
    @GET("/api/usuario/doLogin/")
    fun doLogin(@Query(value="email", encoded=true) email: String, @Query(value="senha", encoded=true) senha: String): Call<UsuarioResponse>

    @GET("/api/usuario/obterPorID/{id}")
    fun obterUsuarioComProjetos(@Path("id") id: Int): Call<UsuarioResponse>

    @POST("/api/usuario/")
    fun criarUsuario(@Body usuario: Usuario): Call<UsuarioResponse>


    // Chamadas do projeto.
    @POST("/api/projeto/")
    fun criarProjeto(@Body projeto: Projeto): Call<ProjetoResponse>

    @GET("/api/projeto/obterMeusProjetos/{id}")
    fun oberMeusProjetos(@Path("id") id: Int): Call<ProjetosResponse>

    @GET("/api/projeto/obterProjetoComParticipantes/{idProjeto}")
    fun obterProjetoComParticipantes(@Path("idProjeto") idProjeto: Int): Call<ProjetoResponse>

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