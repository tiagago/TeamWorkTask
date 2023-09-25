package br.pucminas.teamworktask.response

import br.pucminas.teamworktask.models.Projeto

class ProjetosResponse: GenericResponse(){
    var projetos: List<Projeto>? = null
}