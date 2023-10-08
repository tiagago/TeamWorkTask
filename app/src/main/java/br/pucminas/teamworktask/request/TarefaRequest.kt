package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Tarefa

class TarefaRequest: GenericRequest() {
    var tarefa: Tarefa? = null
}