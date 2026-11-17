package com.fitapp.ui.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fitapp.repository.UsuarioRepository
import com.fitapp.utils.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CadastroUiState(
    val carregando: Boolean = false,
    val mensagemErro: String? = null,
    val sucesso: Boolean = false,
    val mensagemSucesso: String? = null
)

/** ViewModel do cadastro de professor e de aluno. */
class CadastroViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    fun cadastrarProfessor(nome: String, email: String, senha: String) {
        _uiState.value = CadastroUiState(carregando = true)
        viewModelScope.launch {
            when (val r = usuarioRepository.cadastrarProfessor(nome, email, senha)) {
                is Resultado.Sucesso -> _uiState.value = CadastroUiState(
                    sucesso = true,
                    mensagemSucesso = "Conta criada! Seu codigo de vinculacao e: ${r.dado.codigoVinculacao}. Guarde e compartilhe com seus alunos."
                )
                is Resultado.Erro -> _uiState.value = CadastroUiState(mensagemErro = r.mensagem)
            }
        }
    }

    fun cadastrarAluno(nome: String, email: String, senha: String, codigo: String) {
        _uiState.value = CadastroUiState(carregando = true)
        viewModelScope.launch {
            when (val r = usuarioRepository.cadastrarAluno(nome, email, senha, codigo)) {
                is Resultado.Sucesso -> _uiState.value = CadastroUiState(
                    sucesso = true, mensagemSucesso = "Conta criada! Bem-vindo(a), ${r.dado.nome}. Agora e so entrar."
                )
                is Resultado.Erro -> _uiState.value = CadastroUiState(mensagemErro = r.mensagem)
            }
        }
    }

    fun limpar() { _uiState.value = CadastroUiState() }
}

class CadastroViewModelFactory(private val usuarioRepository: UsuarioRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CadastroViewModel(usuarioRepository) as T
}
