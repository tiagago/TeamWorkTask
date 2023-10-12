package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.response.*
import br.pucminas.teamworktask.utils.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {
    /*****************************
     **** Chamadas do Usuário ****
     *****************************/
    @GET("/api/usuario/doLogin/")
    fun doLogin(@Query(value="login", encoded=true) login: String, @Query(value="senha", encoded=true) senha: String): Call<UsuarioResponse>

    @GET("/api/usuario/obterPorID/")
    fun obterUsuarioComProjetos(@Query("idUsuario") id: Int): Call<UsuarioResponse>

    @POST("/api/usuario/")
    fun criarUsuario(@Body genericRequest: GenericRequest): Call<UsuarioResponse>

    /*****************************
     **** Chamadas do Projeto ****
     *****************************/
    @POST("/api/projeto/")
    fun criarProjeto(@Body projetoRequest: ProjetoRequest): Call<ProjetoResponse>

    @PUT("/api/projeto/")
    fun editarProjeto(@Body projetoRequest: ProjetoRequest): Call<GenericResponse>

    @GET("/api/projeto/obterMeusProjetos/")
    fun oberMeusProjetos(@Query("idUsuario") id: Int): Call<ProjetosResponse>

    @GET("/api/projeto/obterProjetoComParticipantes/")
    fun obterProjetoComParticipantes(@Query("idProjeto") idProjeto: Int): Call<ProjetoResponse>

    @GET("/api/projeto/obterProjetoPorCodigo/")
    fun obterProjetoPorCodigo(@Query("codigo") codigo: String): Call<ProjetosResponse>

    /*******************************
     **** Chamadas para as Tags ****
     *******************************/
    @POST("/api/tag/")
    fun criarTag(@Body tagRequest: TagRequest): Call<TagResponse>

    @PUT("/api/tag/")
    fun editarTag(@Body tagRequest: TagRequest): Call<GenericResponse>

    @GET("/api/tag/obterTagsPorProjeto/")
    fun obterTagsPorProjeto(@Query("idProjeto") id: Int): Call<TagsResponse>

    /**********************************
     **** Chamadas para as Tarefas ****
     **********************************/

    @POST("/api/tarefa/")
    fun criarTarefa(@Body tarefaRequest: TarefaRequest): Call<TarefaResponse>

    @PUT("/api/tarefa/")
    fun editarTarefa(@Body tarefaRequest: TarefaRequest): Call<GenericResponse>

    @DELETE("/api/tarefa/")
    fun deletarTarefa(@Query("id") tarefaId: Int): Call<GenericResponse>

    @GET("/api/tarefa/obterTarefasPorProjetoStatus/")
    fun obterTarefasPorProjetoStatus(@Query("idProjeto") id: Int, @Query("idStatus") idStatus: Int): Call<TarefasResponse>

    /************************************************
     **** Chamadas associacao usuario no projeto ****
     ************************************************/
    @DELETE("/api/projetoUsuario/")
    fun desassociarUsuarioProjeto(@Query("idProjeto") idProjeto: Int, @Query("idUsuario") idUsuario: Int): Call<GenericResponse>

    @POST("/api/projetoUsuario/")
    fun associarUsuarioProjeto(@Body projetoRequest: ProjetoRequest): Call<GenericResponse>

    /***************************************
     **** Chamadas associacao historico ****
     ***************************************/

    @GET("/api/historico/obterHistoricoPorProjeto/")
    fun obterHistoricoPorProjeto(@Query("idProjeto") projetoId: Int): Call<HistoricosResponse>


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