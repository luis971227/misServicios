package com.luis971227.miservicios.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Azul = Color(0xFF1A4FA0)
val AzulClaro = Color(0xFF5B8DEF)
val Blanco = Color(0xFFFFFFFF)
val GrisTexto = Color(0xFF444A55)
val RojoEtecsa = Color(0xFFE04B3A)
val VerdeTmv = Color(0xFF128C7E)

private val ColoresClaro = lightColorScheme(
    primary = Azul,
    onPrimary = Blanco,
    primaryContainer = Color(0xFFD9E6FF),
    onPrimaryContainer = Color(0xFF0D2B5C),
    secondary = VerdeTmv,
    onSecondary = Blanco,
    secondaryContainer = Color(0xFFD2F0EB),
    onSecondaryContainer = Color(0xFF0B3A34),
    background = Color(0xFFF6F8FC),
    onBackground = Color(0xFF16181D),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF16181D),
    surfaceVariant = Color(0xFFEDF1F8),
    onSurfaceVariant = Color(0xFF475060)
)

private val ColoresOscuro = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Color(0xFF0D2B5C),
    primaryContainer = Color(0xFF153A76),
    onPrimaryContainer = Color(0xFFD9E6FF),
    secondary = Color(0xFF6FC7BA),
    onSecondary = Color(0xFF0B3A34),
    secondaryContainer = Color(0xFF0E4B43),
    onSecondaryContainer = Color(0xFFD2F0EB),
    background = Color(0xFF0F1115),
    onBackground = Color(0xFFE4E7ED),
    surface = Color(0xFF16181D),
    onSurface = Color(0xFFE4E7ED),
    surfaceVariant = Color(0xFF1E2229),
    onSurfaceVariant = Color(0xFFB4BCC9)
)

@Composable
fun MisServiciosTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) ColoresOscuro else ColoresClaro,
        content = content
    )
}
