package br.pucminas.teamworktask.ui.privada.tarefa

import br.pucminas.teamworktask.models.Tag
import br.pucminas.teamworktask.models.Tarefa

interface TarefaItemListOnClickInterface {
    fun onClickEditarTarefa(tarefa: Tarefa)
    fun onClickExcluirTarefa(tarefa: Tarefa)
}