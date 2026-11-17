package com.fitapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "aluno",
    foreignKeys = [ForeignKey(
        entity = Professor::class,
        parentColumns = ["idProfessor"],
        childColumns = ["idProfessor"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["email"], unique = true), Index(value = ["idProfessor"])]
)
data class Aluno(
    @PrimaryKey(autoGenerate = true) val idAluno: Long = 0,
    val nome: String,
    val email: String,
    val senhaHash: String,
    val idProfessor: Long
)
