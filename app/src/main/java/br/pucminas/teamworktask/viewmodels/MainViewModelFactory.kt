package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.pucminas.teamworktask.repositories.UsuarioRepository


class MainViewModelFactory constructor(private val repository: UsuarioRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            UsuarioViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}