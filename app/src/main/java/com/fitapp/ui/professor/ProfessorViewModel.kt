package com.fitapp.ui.professor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor
import com.fitapp.repository.TreinoRepository
import com.fitapp.repository.UsuarioRepository
import com.fitapp.utils.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfessorUiState(
    val carregando: Boolean = false,
    val alunos: List<Aluno> = emptyList(),
    val mensagem: String? = null
)

/** Tela do professor: lista seus alunos e permite criar treinos. */
class ProfessorViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val treinoRepository: TreinoRepository,
    val professor: Professor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfessorUiState(carregando = true))
    val uiState: StateFlow<ProfessorUiState> = _uiState.asStateFlow()

    init { carregarAlunos() }

    fun carregarAlunos() {
        _uiState.value = _uiState.value.copy(carregando = true)
        viewModelScope.launch {
            when (val r = usuarioRepository.listarAlunos(professor.idProfessor)) {
                is Resultado.Sucesso -> _uiState.value = ProfessorUiState(alunos = r.dado)
                is Resultado.Erro -> _uiState.value = ProfessorUiState(mensagem = r.mensagem)
            }
        }
    }

    fun criarTreino(
        aluno: Aluno,
        descricao: String,
        exercicios: List<TreinoRepository.ExercicioNovo>,
        onResultado: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            when (val r = treinoRepository.criarTreino(professor.idProfessor, aluno.idAluno, descricao, exercicios)) {
                is Resultado.Sucesso -> onResultado(true, "Treino criado para ${aluno.nome}!")
                is Resultado.Erro -> onResultado(false, r.mensagem)
            }
        }
    }
}

class ProfessorViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val treinoRepository: TreinoRepository,
    private val professor: Professor
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfessorViewModel(usuarioRepository, treinoRepository, professor) as T
}
