package com.fitapp.ui.aluno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fitapp.data.local.entity.Aluno
import com.fitapp.repository.DesempenhoRepository
import com.fitapp.repository.TreinoRepository
import com.fitapp.utils.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlunoUiState(
    val carregando: Boolean = false,
    val treinos: List<TreinoRepository.TreinoComExercicios> = emptyList(),
    val frequencia: Int = 0,
    val mensagem: String? = null
)

/** Tela do aluno: ve os treinos prescritos e marca como realizado. */
class AlunoViewModel(
    private val treinoRepository: TreinoRepository,
    private val desempenhoRepository: DesempenhoRepository,
    val aluno: Aluno
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlunoUiState(carregando = true))
    val uiState: StateFlow<AlunoUiState> = _uiState.asStateFlow()

    init { carregar() }

    fun carregar() {
        _uiState.value = _uiState.value.copy(carregando = true)
        viewModelScope.launch {
            val treinos = when (val r = treinoRepository.listarTreinosDoAluno(aluno.idAluno)) {
                is Resultado.Sucesso -> r.dado
                is Resultado.Erro -> emptyList()
            }
            val freq = when (val f = desempenhoRepository.frequenciaRecente(aluno.idAluno)) {
                is Resultado.Sucesso -> f.dado
                is Resultado.Erro -> 0
            }
            _uiState.value = AlunoUiState(treinos = treinos, frequencia = freq)
        }
    }

    fun marcarComoRealizado(idTreino: Long, onResultado: (String) -> Unit) {
        viewModelScope.launch {
            when (val r = desempenhoRepository.registrarTreinoRealizado(aluno.idAluno, idTreino)) {
                is Resultado.Sucesso -> { onResultado("Treino registrado!"); carregar() }
                is Resultado.Erro -> onResultado(r.mensagem)
            }
        }
    }
}

class AlunoViewModelFactory(
    private val treinoRepository: TreinoRepository,
    private val desempenhoRepository: DesempenhoRepository,
    private val aluno: Aluno
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AlunoViewModel(treinoRepository, desempenhoRepository, aluno) as T
}
