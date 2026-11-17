package com.fitapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitapp.data.local.entity.Desempenho

@Dao
interface DesempenhoDao {
    @Insert suspend fun inserir(desempenho: Desempenho): Long
    @Query("SELECT * FROM desempenho WHERE idAluno = :idAluno ORDER BY data DESC")
    suspend fun listarPorAluno(idAluno: Long): List<Desempenho>
    @Query("SELECT COUNT(*) FROM desempenho WHERE idAluno = :idAluno AND data BETWEEN :inicio AND :fim")
    suspend fun contarNoPeriodo(idAluno: Long, inicio: Long, fim: Long): Int
}
