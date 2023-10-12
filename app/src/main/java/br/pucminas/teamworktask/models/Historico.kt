package br.pucminas.teamworktask.models

import java.util.Date

class Historico {
    lateinit var id : Integer
    lateinit var descricao : String
    lateinit var dataCriacao : Date
    lateinit var projeto: Projeto
    lateinit var usuario: Usuario
}