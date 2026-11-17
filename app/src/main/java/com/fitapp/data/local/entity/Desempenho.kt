package com.fitapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "desempenho",
    foreignKeys = [ForeignKey(
        entity = Aluno::class,
        parentColumns = ["idAluno"],
        childColumns = ["idAluno"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["idAluno"])]
)
data class Desempenho(
    @PrimaryKey(autoGenerate = true) val idDesempenho: Long = 0,
    val data: Long,
    val resultado: String,
    val idTreino: Long,
    val idAluno: Long
)
