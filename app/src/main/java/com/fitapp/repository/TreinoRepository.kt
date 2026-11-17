package com.fitapp.repository

import com.fitapp.data.local.dao.AlunoDao
import com.fitapp.data.local.dao.TreinoDao
import com.fitapp.data.local.entity.Exercicio
import com.fitapp.data.local.entity.Treino
import com.fitapp.utils.Resultado

/** Criacao e listagem de treinos. Valida vinculo professor-aluno. */
class TreinoRepository(
    private val treinoDao: TreinoDao,
    private val alunoDao: AlunoDao
) {
    data class ExercicioNovo(val nome: String, val series: Int, val repeticoes: Int)
    data class TreinoComExercicios(val treino: Treino, val exercicios: List<Exercicio>)

    suspend fun criarTreino(
        idProfessor: Long, idAluno: Long, descricao: String, exercicios: List<ExercicioNovo>
    ): Resultado<Long> {
        if (descricao.isBlank() || exercicios.isEmpty())
            return Resultado.Erro("O treino precisa de uma descricao e ao menos um exercicio.")
        if (exercicios.any { it.nome.isBlank() || it.series <= 0 || it.repeticoes <= 0 })
            return Resultado.Erro("Cada exercicio precisa de nome, series e repeticoes maiores que zero.")
        return try {
            val aluno = alunoDao.buscarPorId(idAluno) ?: return Resultado.Erro("Aluno nao encontrado.")
            if (aluno.idProfessor != idProfessor)
                return Resultado.Erro("Voce nao e o professor responsavel por este aluno.")
            val idTreino = treinoDao.inserirTreino(
                Treino(descricao = descricao.trim(), dataCriacao = System.currentTimeMillis(), idAluno = idAluno)
            )
            treinoDao.inserirExercicios(
                exercicios.map { Exercicio(nome = it.nome.trim(), series = it.series, repeticoes = it.repeticoes, idTreino = idTreino) }
            )
            Resultado.Sucesso(idTreino)
        } catch (e: Exception) { Resultado.Erro("Nao foi possivel criar o treino. Tente novamente.") }
    }

    suspend fun listarTreinosDoAluno(idAluno: Long): Resultado<List<TreinoComExercicios>> = try {
        val lista = treinoDao.listarPorAluno(idAluno).map { t ->
            TreinoComExercicios(t, treinoDao.listarExercicios(t.idTreino))
        }
        Resultado.Sucesso(lista)
    } catch (e: Exception) { Resultado.Erro("Nao foi possivel carregar os treinos.") }
}
