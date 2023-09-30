package br.pucminas.teamworktask.repositories

import br.pucminas.teamworktask.models.Projeto
import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.request.RetrofitService
import retrofit2.http.Query

class Repository constructor(private val retrofitService: RetrofitService) {

    // Chamadas do usuário.
    fun doLogin(email: String, senha: String) = retrofitService.doLogin(email, senha)

    fun criarUsuario(usuario: Usuario) = retrofitService.criarUsuario(usuario)

    fun obterUsuarioComProjetos(idUsuario: Int) = retrofitService.obterUsuarioComProjetos(idUsuario)


    // Chamadas do projeto.
    fun criarProjeto(projeto: Projeto) = retrofitService.criarProjeto(projeto)

    fun oberMeusProjetos(idUsuario: Int) = retrofitService.oberMeusProjetos(idUsuario)

    fun obterProjetoComParticipantes(idProjeto: Int) = retrofitService.obterProjetoComParticipantes(idProjeto)

    // Chamadas para as Tags
    fun criarTag(tag: Tag) = retrofitService.criarTag(tag)

    fun editarTag(tag: Tag) = retrofitService.editarTag(tag)

    fun obterTagsPorProjeto(projetoId: Int) = retrofitService.obterTagsPorProjeto(projetoId)

    // Chamadas associacao usuario no projeto.
    fun desassociarUsuarioProjeto(idProjeto: Int, idUsuario: Int) = retrofitService.desassociarUsuarioProjeto(idProjeto, idUsuario)
}