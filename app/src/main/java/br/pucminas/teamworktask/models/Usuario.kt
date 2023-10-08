package br.pucminas.teamworktask.models
class Usuario  {
    lateinit var id : Integer
    lateinit var login : String
    lateinit var nomeExibicao : String
    lateinit var senha : String
    lateinit var participa : List<Projeto>

    override fun toString(): String {
        return nomeExibicao
    }
}