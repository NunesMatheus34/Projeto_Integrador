package com.fitapp.utils

/** Resultado de uma operacao de negocio: sucesso (com dado) ou erro (com mensagem). */
sealed class Resultado<out T> {
    data class Sucesso<out T>(val dado: T) : Resultado<T>()
    data class Erro(val mensagem: String) : Resultado<Nothing>()
    val ehSucesso: Boolean get() = this is Sucesso
}
