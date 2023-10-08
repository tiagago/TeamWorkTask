package br.pucminas.teamworktask.response

import br.pucminas.teamworktask.models.Tarefa

class TarefasResponse: GenericResponse(){
    var tarefas: List<Tarefa>? = null
}