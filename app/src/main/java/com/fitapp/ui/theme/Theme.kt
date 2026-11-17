package com.fitapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CoresClaras = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = VerdeAcento,
    background = CinzaFundo,
    onBackground = CinzaTexto,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = CinzaTexto
)

private val CoresEscuras = darkColorScheme(
    primary = AzulPrimario,
    secondary = VerdeAcento
)

@Composable
fun FitAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) CoresEscuras else CoresClaras,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
