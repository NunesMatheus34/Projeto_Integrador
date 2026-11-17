package com.fitapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "treino",
    foreignKeys = [ForeignKey(
        entity = Aluno::class,
        parentColumns = ["idAluno"],
        childColumns = ["idAluno"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["idAluno"])]
)
data class Treino(
    @PrimaryKey(autoGenerate = true) val idTreino: Long = 0,
    val descricao: String,
    val dataCriacao: Long,
    val idAluno: Long
)
