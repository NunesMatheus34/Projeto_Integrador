package com.fitapp.utils

import kotlin.random.Random

/** Gera o codigo de vinculacao exclusivo do professor. 6 caracteres, sem ambiguos. */
object GeradorCodigo {
    private const val ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    fun gerar(): String = buildString { repeat(6) { append(ALFABETO[Random.nextInt(ALFABETO.length)]) } }
}
