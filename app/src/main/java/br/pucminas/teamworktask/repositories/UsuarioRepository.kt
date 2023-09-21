package br.pucminas.teamworktask.repositories

import br.pucminas.teamworktask.models.Usuario
import br.pucminas.teamworktask.request.RetrofitService

class UsuarioRepository constructor(private val retrofitService: RetrofitService) {

    fun doLogin(usuarioRequest: Usuario) = retrofitService.doLogin(usuarioRequest.email, usuarioRequest.senha)

}