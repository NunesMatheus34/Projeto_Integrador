package com.fitapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onProfessor: (Professor) -> Unit,
    onAluno: (Aluno) -> Unit,
    onIrParaCadastro: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("FitApp", style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text("Entrar na sua conta", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("E-mail") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = senha, onValueChange = { senha = it },
            label = { Text("Senha") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
        Spacer(Modifier.height(20.dp))

        Button(onClick = { viewModel.entrar(email, senha, onProfessor, onAluno) },
            enabled = !uiState.carregando, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(if (uiState.carregando) "Entrando..." else "Entrar")
        }
        TextButton(onClick = onIrParaCadastro) { Text("Nao tem conta? Cadastre-se") }

        if (uiState.carregando) { Spacer(Modifier.height(8.dp)); CircularProgressIndicator() }
        uiState.mensagemErro?.let {
            Spacer(Modifier.height(8.dp)); Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
