package com.fitapp.ui.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitapp.data.local.entity.Aluno
import com.fitapp.repository.TreinoRepository

private data class ExercicioForm(var nome: String = "", var series: String = "", var reps: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarTreinoScreen(
    viewModel: ProfessorViewModel,
    aluno: Aluno,
    onConcluir: () -> Unit,
    onVoltar: () -> Unit
) {
    var descricao by remember { mutableStateOf("") }
    val exercicios = remember { mutableStateListOf(ExercicioForm()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var erro by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo treino") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("Voltar") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            Text("Aluno: ${aluno.nome}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = descricao, onValueChange = { descricao = it },
                label = { Text("Descricao do treino (ex: Treino A - Peito)") },
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Text("Exercicios", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            exercicios.forEachIndexed { i, ex ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        OutlinedTextField(value = ex.nome,
                            onValueChange = { exercicios[i] = ex.copy(nome = it) },
                            label = { Text("Nome do exercicio") }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = ex.series,
                                onValueChange = { exercicios[i] = ex.copy(series = it.filter { c -> c.isDigit() }) },
                                label = { Text("Series") }, modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                            OutlinedTextField(value = ex.reps,
                                onValueChange = { exercicios[i] = ex.copy(reps = it.filter { c -> c.isDigit() }) },
                                label = { Text("Repeticoes") }, modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                        }
                    }
                }
            }
            TextButton(onClick = { exercicios.add(ExercicioForm()) }) { Text("+ Adicionar exercicio") }
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    val lista = exercicios.map {
                        TreinoRepository.ExercicioNovo(
                            nome = it.nome,
                            series = it.series.toIntOrNull() ?: 0,
                            repeticoes = it.reps.toIntOrNull() ?: 0
                        )
                    }
                    viewModel.criarTreino(aluno, descricao, lista) { ok, msg ->
                        erro = !ok; mensagem = msg
                        if (ok) onConcluir()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Salvar treino") }

            mensagem?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = if (erro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            }
        }
    }
}
