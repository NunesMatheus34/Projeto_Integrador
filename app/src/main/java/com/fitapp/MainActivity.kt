package com.fitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fitapp.data.local.AppDatabase
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor
import com.fitapp.repository.DesempenhoRepository
import com.fitapp.repository.TreinoRepository
import com.fitapp.repository.UsuarioRepository
import com.fitapp.ui.aluno.AlunoHomeScreen
import com.fitapp.ui.aluno.AlunoViewModel
import com.fitapp.ui.aluno.AlunoViewModelFactory
import com.fitapp.ui.cadastro.CadastroScreen
import com.fitapp.ui.cadastro.CadastroViewModel
import com.fitapp.ui.cadastro.CadastroViewModelFactory
import com.fitapp.ui.login.LoginScreen
import com.fitapp.ui.login.LoginViewModel
import com.fitapp.ui.login.LoginViewModelFactory
import com.fitapp.ui.professor.CriarTreinoScreen
import com.fitapp.ui.professor.ProfessorHomeScreen
import com.fitapp.ui.professor.ProfessorViewModel
import com.fitapp.ui.professor.ProfessorViewModelFactory
import com.fitapp.ui.theme.FitAppTheme

/** Estados de navegacao da aplicacao. */
sealed interface Tela {
    data object Login : Tela
    data object Cadastro : Tela
    data class HomeProfessor(val professor: Professor) : Tela
    data class CriarTreino(val professor: Professor, val aluno: Aluno) : Tela
    data class HomeAluno(val aluno: Aluno) : Tela
}

/**
 * Monta a cadeia de dependencias (Banco -> Repositorios) e controla a navegacao
 * entre as telas conforme o padrao MVVM.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.obter(applicationContext)
        val usuarioRepo = UsuarioRepository(db.professorDao(), db.alunoDao())
        val treinoRepo = TreinoRepository(db.treinoDao(), db.alunoDao())
        val desempenhoRepo = DesempenhoRepository(db.desempenhoDao())

        setContent {
            FitAppTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppNavegacao(usuarioRepo, treinoRepo, desempenhoRepo)
                }
            }
        }
    }
}

@Composable
private fun AppNavegacao(
    usuarioRepo: UsuarioRepository,
    treinoRepo: TreinoRepository,
    desempenhoRepo: DesempenhoRepository
) {
    var tela by remember { mutableStateOf<Tela>(Tela.Login) }

    when (val t = tela) {
        is Tela.Login -> {
            val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(usuarioRepo))
            LoginScreen(
                viewModel = vm,
                onProfessor = { tela = Tela.HomeProfessor(it) },
                onAluno = { tela = Tela.HomeAluno(it) },
                onIrParaCadastro = { tela = Tela.Cadastro }
            )
        }
        is Tela.Cadastro -> {
            val vm: CadastroViewModel = viewModel(factory = CadastroViewModelFactory(usuarioRepo))
            CadastroScreen(viewModel = vm, onVoltarParaLogin = { tela = Tela.Login })
        }
        is Tela.HomeProfessor -> {
            val vm: ProfessorViewModel = viewModel(
                key = "prof-${t.professor.idProfessor}",
                factory = ProfessorViewModelFactory(usuarioRepo, treinoRepo, t.professor)
            )
            ProfessorHomeScreen(
                viewModel = vm,
                onCriarTreino = { aluno -> tela = Tela.CriarTreino(t.professor, aluno) },
                onSair = { tela = Tela.Login }
            )
        }
        is Tela.CriarTreino -> {
            val vm: ProfessorViewModel = viewModel(
                key = "prof-${t.professor.idProfessor}",
                factory = ProfessorViewModelFactory(usuarioRepo, treinoRepo, t.professor)
            )
            CriarTreinoScreen(
                viewModel = vm,
                aluno = t.aluno,
                onConcluir = { tela = Tela.HomeProfessor(t.professor) },
                onVoltar = { tela = Tela.HomeProfessor(t.professor) }
            )
        }
        is Tela.HomeAluno -> {
            val vm: AlunoViewModel = viewModel(
                key = "aluno-${t.aluno.idAluno}",
                factory = AlunoViewModelFactory(treinoRepo, desempenhoRepo, t.aluno)
            )
            AlunoHomeScreen(viewModel = vm, onSair = { tela = Tela.Login })
        }
    }
}
