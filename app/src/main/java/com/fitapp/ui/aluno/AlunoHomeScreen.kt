package com.fitapp.ui.aluno

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlunoHomeScreen(
    viewModel: AlunoViewModel,
    onSair: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aluno = viewModel.aluno
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { viewModel.carregar() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ola, ${aluno.nome}") },
                actions = { TextButton(onClick = onSair) { Text("Sair") } }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Sua frequencia (ultimos 30 dias)", style = MaterialTheme.typography.labelMedium)
                    Text("${uiState.frequencia} treino(s) realizado(s)",
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Meus treinos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            if (uiState.carregando) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.treinos.isEmpty()) {
                Text("Voce ainda nao tem treinos. Peca ao seu professor para montar sua ficha.",
                    style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.treinos) { item ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text(item.treino.descricao, fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                                item.exercicios.forEach { ex ->
                                    Text("- ${ex.nome}: ${ex.series} x ${ex.repeticoes}",
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        viewModel.marcarComoRealizado(item.treino.idTreino) { msg ->
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("Marcar como realizado") }
                            }
                        }
                    }
                }
            }
        }
    }
}
