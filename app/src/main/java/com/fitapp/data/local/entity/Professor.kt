package com.fitapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "professor",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["codigoVinculacao"], unique = true)
    ]
)
data class Professor(
    @PrimaryKey(autoGenerate = true) val idProfessor: Long = 0,
    val nome: String,
    val email: String,
    val senhaHash: String,
    val codigoVinculacao: String
)
