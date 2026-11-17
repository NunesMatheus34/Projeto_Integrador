package com.fitapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitapp.data.local.dao.AlunoDao
import com.fitapp.data.local.dao.DesempenhoDao
import com.fitapp.data.local.dao.ProfessorDao
import com.fitapp.data.local.dao.TreinoDao
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Desempenho
import com.fitapp.data.local.entity.Exercicio
import com.fitapp.data.local.entity.Professor
import com.fitapp.data.local.entity.Treino

/**
 * Banco de dados local do FitApp (Room / SQLite).
 * Fonte de verdade da aplicacao no modo offline-first.
 */
@Database(
    entities = [Professor::class, Aluno::class, Treino::class, Exercicio::class, Desempenho::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun professorDao(): ProfessorDao
    abstract fun alunoDao(): AlunoDao
    abstract fun treinoDao(): TreinoDao
    abstract fun desempenhoDao(): DesempenhoDao

    companion object {
        @Volatile private var INSTANCIA: AppDatabase? = null
        fun obter(context: Context): AppDatabase =
            INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "fitapp.db"
                ).build().also { INSTANCIA = it }
            }
    }
}
