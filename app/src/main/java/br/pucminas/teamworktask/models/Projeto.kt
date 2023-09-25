package br.pucminas.teamworktask.models

import java.util.Date

class Projeto  {
    lateinit var id : Integer
    lateinit var nome : String
    lateinit var descricao : String
    lateinit var codigo : String
    lateinit var dataCriacao : Date
    lateinit var usuario : Usuario
    lateinit var criador : Integer
}