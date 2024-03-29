package br.pucminas.teamworktask.viewmodels

import br.pucminas.teamworktask.repositories.Repository
import br.pucminas.teamworktask.request.ProjetoRequest


class ProjetoUsuarioViewModel constructor(private val repository: Repository)  : GenericViewModel() {
    fun desassociarUsuarioProjeto(idProjeto: Int, idUsuario: Int) {
        executeGenericService(repository.desassociarUsuarioProjeto(idProjeto, idUsuario))
    }

    fun associarUsuarioProjeto(projetoRequest: ProjetoRequest) {
        executeGenericService(repository.associarUsuarioProjeto(projetoRequest))
    }
}