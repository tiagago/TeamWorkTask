package br.pucminas.teamworktask.repositories

import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.request.GenericRequest
import br.pucminas.teamworktask.request.ProjetoRequest
import br.pucminas.teamworktask.request.RetrofitService
import br.pucminas.teamworktask.request.TagRequest
import retrofit2.http.Query

class Repository constructor(private val retrofitService: RetrofitService) {


    /*****************************
     **** Chamadas do Usuário ****
     *****************************/
    fun doLogin(login: String, senha: String) = retrofitService.doLogin(login, senha)

    fun criarUsuario(genericRequest: GenericRequest) = retrofitService.criarUsuario(genericRequest)

    fun obterUsuarioComProjetos(idUsuario: Int) = retrofitService.obterUsuarioComProjetos(idUsuario)

    /*****************************
     **** Chamadas do Projeto ****
     *****************************/

    fun criarProjeto(projetoRequest: ProjetoRequest) = retrofitService.criarProjeto(projetoRequest)

    fun editarProjeto(projetoRequest: ProjetoRequest) = retrofitService.editarProjeto(projetoRequest)

    fun oberMeusProjetos(idUsuario: Int) = retrofitService.oberMeusProjetos(idUsuario)

    fun obterProjetoComParticipantes(idProjeto: Int) = retrofitService.obterProjetoComParticipantes(idProjeto)

    fun obterProjetoPorCodigo(codigo: String) = retrofitService.obterProjetoPorCodigo(codigo)

    /*******************************
     **** Chamadas para as Tags ****
     *******************************/
    fun criarTag(tagRequest: TagRequest) = retrofitService.criarTag(tagRequest)

    fun editarTag(tagRequest: TagRequest) = retrofitService.editarTag(tagRequest)

    fun obterTagsPorProjeto(projetoId: Int) = retrofitService.obterTagsPorProjeto(projetoId)

    // Chamadas associacao usuario no projeto.
    fun desassociarUsuarioProjeto(projetoRequest: ProjetoRequest) = retrofitService.desassociarUsuarioProjeto(projetoRequest)
    fun associarUsuarioProjeto(projetoRequest: ProjetoRequest) = retrofitService.associarUsuarioProjeto(projetoRequest)
}