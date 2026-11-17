package com.fitapp.ui.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitapp.data.local.entity.Aluno

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessorHomeScreen(
    viewModel: ProfessorViewModel,
    onCriarTreino: (Aluno) -> Unit,
    onSair: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val professor = viewModel.professor
    LaunchedEffect(Unit) { viewModel.carregarAlunos() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ola, ${professor.nome}") },
                actions = { TextButton(onClick = onSair) { Text("Sair") } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Seu codigo de vinculacao", style = MaterialTheme.typography.labelMedium)
                    Text(professor.codigoVinculacao, style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold)
                    Text("Compartilhe com seus alunos para que eles se cadastrem.",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Meus alunos (${uiState.alunos.size})", style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            if (uiState.carregando) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.alunos.isEmpty()) {
                Text("Nenhum aluno cadastrado ainda. Assim que um aluno usar seu codigo, ele aparece aqui.",
                    style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.alunos) { aluno ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(
                                Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(aluno.nome, fontWeight = FontWeight.Bold)
                                    Text(aluno.email, style = MaterialTheme.typography.bodySmall)
                                }
                                Button(onClick = { onCriarTreino(aluno) }) { Text("Criar treino") }
                            }
                        }
                    }
                }
            }
            uiState.mensagem?.let { Spacer(Modifier.height(8.dp)); Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
