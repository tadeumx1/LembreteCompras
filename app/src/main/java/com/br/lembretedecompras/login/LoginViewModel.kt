package com.br.lembretedecompras.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.br.lembretedecompras.models.RequestState
import com.br.lembretedecompras.models.Usuario
import com.br.repository.UsuarioRepository

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val usuarioRepository = UsuarioRepository(application)

    val loginState = MutableLiveData<RequestState<Boolean>>()
    val usuarioLogadoState = MutableLiveData<RequestState<String>>()

    fun logar(usuario: Usuario) {
        if(usuario.email.isEmpty()) {
            loginState.value = RequestState.Error(Throwable("Favor informar o e-mail"))
        } else {
            loginState.value = usuarioRepository.logar(usuario).value
        }
    }

    fun getUsuarioLogado() {
        usuarioLogadoState.value = usuarioRepository.getUsuarioLogado().value
    }
}