package com.fitapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor
import com.fitapp.repository.UsuarioRepository
import com.fitapp.utils.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val carregando: Boolean = false,
    val mensagemErro: String? = null
)

/** Faz a ponte entre a tela de login e as regras de negocio (MVVM). */
class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun entrar(
        email: String,
        senha: String,
        onProfessor: (Professor) -> Unit,
        onAluno: (Aluno) -> Unit
    ) {
        if (email.isBlank() || senha.isBlank()) {
            _uiState.value = LoginUiState(mensagemErro = "Preencha e-mail e senha.")
            return
        }
        _uiState.value = LoginUiState(carregando = true)
        viewModelScope.launch {
            when (val aluno = usuarioRepository.autenticarAluno(email, senha)) {
                is Resultado.Sucesso -> { _uiState.value = LoginUiState(); onAluno(aluno.dado) }
                is Resultado.Erro -> {
                    when (val prof = usuarioRepository.autenticarProfessor(email, senha)) {
                        is Resultado.Sucesso -> { _uiState.value = LoginUiState(); onProfessor(prof.dado) }
                        is Resultado.Erro -> _uiState.value = LoginUiState(mensagemErro = "E-mail ou senha incorretos.")
                    }
                }
            }
        }
    }
}

class LoginViewModelFactory(private val usuarioRepository: UsuarioRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = LoginViewModel(usuarioRepository) as T
}
