package com.fitapp.utils

import java.security.MessageDigest

/**
 * Hash de senha. A senha nunca e guardada em texto puro (apenas o hash SHA-256).
 * Medida provisoria do modo offline; a autenticacao de producao sera via Firebase (planejado).
 */
object SegurancaSenha {
    fun hash(senha: String): String =
        MessageDigest.getInstance("SHA-256").digest(senha.toByteArray())
            .joinToString("") { "%02x".format(it) }
    fun confere(senha: String, hashArmazenado: String): Boolean = hash(senha) == hashArmazenado
}
