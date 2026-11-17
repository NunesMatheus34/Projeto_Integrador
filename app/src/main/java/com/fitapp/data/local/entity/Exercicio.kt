package com.fitapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicio",
    foreignKeys = [ForeignKey(
        entity = Treino::class,
        parentColumns = ["idTreino"],
        childColumns = ["idTreino"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["idTreino"])]
)
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val idExercicio: Long = 0,
    val nome: String,
    val series: Int,
    val repeticoes: Int,
    val idTreino: Long
)
