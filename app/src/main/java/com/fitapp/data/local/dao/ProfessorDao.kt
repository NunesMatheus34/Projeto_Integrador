package com.fitapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitapp.data.local.entity.Professor

@Dao
interface ProfessorDao {
    @Insert suspend fun inserir(professor: Professor): Long
    @Query("SELECT * FROM professor WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Professor?
    @Query("SELECT * FROM professor WHERE codigoVinculacao = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(codigo: String): Professor?
    @Query("SELECT EXISTS(SELECT 1 FROM professor WHERE codigoVinculacao = :codigo)")
    suspend fun codigoExiste(codigo: String): Boolean
}
