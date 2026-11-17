package com.fitapp.ui.cadastro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    viewModel: CadastroViewModel,
    onVoltarParaLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var ehProfessor by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Criar conta", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        // seletor de perfil
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(selected = !ehProfessor, onClick = { ehProfessor = false },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)) { Text("Sou aluno") }
            SegmentedButton(selected = ehProfessor, onClick = { ehProfessor = true },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)) { Text("Sou professor") }
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = nome, onValueChange = { nome = it },
            label = { Text("Nome completo") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("E-mail") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = senha, onValueChange = { senha = it },
            label = { Text("Senha (min. 6)") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

        if (!ehProfessor) {
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = codigo, onValueChange = { codigo = it },
                label = { Text("Codigo do professor") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (ehProfessor) viewModel.cadastrarProfessor(nome, email, senha)
                else viewModel.cadastrarAluno(nome, email, senha, codigo)
            },
            enabled = !uiState.carregando, modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text(if (uiState.carregando) "Cadastrando..." else "Cadastrar") }

        TextButton(onClick = onVoltarParaLogin) { Text("Ja tem conta? Entrar") }

        if (uiState.carregando) { Spacer(Modifier.height(8.dp)); CircularProgressIndicator() }
        uiState.mensagemErro?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.sucesso) {
            Spacer(Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(uiState.mensagemSucesso ?: "Cadastro realizado!",
                    modifier = Modifier.padding(16.dp))
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onVoltarParaLogin, modifier = Modifier.fillMaxWidth()) { Text("Ir para o login") }
        }
    }
}
