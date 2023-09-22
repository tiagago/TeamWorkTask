package br.pucminas.teamworktask.repositories

import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.request.RetrofitService

class UsuarioRepository constructor(private val retrofitService: RetrofitService) {

    fun doLogin(email: String, senha: String) = retrofitService.doLogin(email, senha)

    fun criarUsuario(usuario: Usuario) = retrofitService.criarUsuario(usuario)

}