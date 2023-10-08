package br.pucminas.teamworktask.models

import java.util.Date

class Tarefa  {
    lateinit var id : Integer
    lateinit var nome : String
    lateinit var descricao : String
    lateinit var dataCriacao : Date
    var dataEntrega : Date? = null
    lateinit var prioridade : Integer
    lateinit var status : Integer
    lateinit var projeto : Projeto
    var usuario : Usuario? = null
    var tag : Tag? = null

}