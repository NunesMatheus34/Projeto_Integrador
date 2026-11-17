package com.fitapp.utils

/** Validacoes simples de entrada, reutilizadas pelos repositorios. */
object Validador {
    private val REGEX_EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    fun emailValido(email: String): Boolean = REGEX_EMAIL.matches(email.trim())
    fun nomeValido(nome: String): Boolean = nome.trim().length >= 2
    fun senhaValida(senha: String): Boolean = senha.length >= 6
}
