package com.fitapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitapp.data.local.entity.Aluno

@Dao
interface AlunoDao {
    @Insert suspend fun inserir(aluno: Aluno): Long
    @Query("SELECT * FROM aluno WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Aluno?
    @Query("SELECT * FROM aluno WHERE idAluno = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): Aluno?
    @Query("SELECT * FROM aluno WHERE idProfessor = :idProfessor ORDER BY nome")
    suspend fun listarPorProfessor(idProfessor: Long): List<Aluno>
}
