package br.pucminas.teamworktask.models

import java.util.Date

class Projeto()  {
    lateinit var id : Integer
    lateinit var nome : String
    lateinit var descricao : String
    lateinit var codigo : String
    lateinit var dataCriacao : Date
    lateinit var usuario : Usuario
    lateinit var criador : Integer
    lateinit var associados : List<Usuario>

    constructor(id: Integer) : this() {
        this.id = id
    }
}