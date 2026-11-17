package com.fitapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitapp.data.local.entity.Exercicio
import com.fitapp.data.local.entity.Treino

@Dao
interface TreinoDao {
    @Insert suspend fun inserirTreino(treino: Treino): Long
    @Insert suspend fun inserirExercicios(exercicios: List<Exercicio>)
    @Query("SELECT * FROM treino WHERE idAluno = :idAluno ORDER BY dataCriacao DESC")
    suspend fun listarPorAluno(idAluno: Long): List<Treino>
    @Query("SELECT * FROM exercicio WHERE idTreino = :idTreino")
    suspend fun listarExercicios(idTreino: Long): List<Exercicio>
}
