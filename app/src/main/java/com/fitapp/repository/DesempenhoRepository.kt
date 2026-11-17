package com.fitapp.repository

import com.fitapp.data.local.dao.DesempenhoDao
import com.fitapp.data.local.entity.Desempenho
import com.fitapp.utils.Resultado

/** Registro de desempenho e frequencia. */
class DesempenhoRepository(private val desempenhoDao: DesempenhoDao) {
    private val umDiaMs = 24L * 60 * 60 * 1000

    suspend fun registrarTreinoRealizado(
        idAluno: Long, idTreino: Long, resultado: String = "Concluido", data: Long = System.currentTimeMillis()
    ): Resultado<Long> = try {
        Resultado.Sucesso(desempenhoDao.inserir(
            Desempenho(data = data, resultado = resultado, idTreino = idTreino, idAluno = idAluno)))
    } catch (e: Exception) { Resultado.Erro("Nao foi possivel registrar o treino.") }

    suspend fun frequenciaRecente(idAluno: Long, dias: Int = 30): Resultado<Int> = try {
        val fim = System.currentTimeMillis()
        Resultado.Sucesso(desempenhoDao.contarNoPeriodo(idAluno, fim - dias * umDiaMs, fim))
    } catch (e: Exception) { Resultado.Erro("Nao foi possivel calcular a frequencia.") }

    suspend fun historico(idAluno: Long): Resultado<List<Desempenho>> = try {
        Resultado.Sucesso(desempenhoDao.listarPorAluno(idAluno))
    } catch (e: Exception) { Resultado.Erro("Nao foi possivel carregar o historico.") }
}
