package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Projeto

class ProjetoRequest: GenericRequest() {
    var projeto: Projeto? = null
}