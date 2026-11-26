package com.luis971227.miservicios

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.ui.etecsa.EtecsaScreen
import com.luis971227.miservicios.ui.transfermovil.TransfermovilScreen
import com.luis971227.miservicios.ui.theme.MisServiciosTheme
import com.luis971227.miservicios.util.Actualizaciones

data class Pestana(val id: String, val titulo: String, val emoji: String)

val PESTANAS = listOf(
    Pestana("etecsa", "ETECSA", "📱"),
    Pestana("tmv", "Transfermóvil", "💳")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MisServiciosTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    var pestana by remember { mutableStateOf("etecsa") }
    val context = LocalContext.current
    var actualizacion by remember { mutableStateOf<Actualizaciones.VersionRemota?>(null) }
    var descargando by remember { mutableStateOf(false) }
    var progreso by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        Actualizaciones.hayActualizacion(context) { version ->
            actualizacion = version
        }
    }

    actualizacion?.let { remota ->
        AlertDialog(
            onDismissRequest = { if (!descargando) actualizacion = null },
            title = { Text("⬆️ Nueva versión " + remota.version) },
            text = {
                Column {
                    if (descargando) {
                        Text("Descargando actualización… $progreso%")
                        LinearProgressIndicator(
                            progress = { progreso / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        )
                    } else {
                        Text("Hay una versión nueva (${remota.version}) disponible. ¿Descargarla e instalarla ahora?")
                    }
                }
            },
            confirmButton = {
                if (!descargando) {
                    TextButton(onClick = {
                        descargando = true
                        progreso = 0
                        Actualizaciones.descargar(
                            context = context,
                            url = remota.urlApk,
                            onProgreso = { progreso = it },
                            alListo = { archivo ->
                                descargando = false
                                if (archivo != null) {
                                    actualizacion = null
                                    Actualizaciones.instalar(context, archivo)
                                }
                            }
                        )
                    }) { Text("Descargar") }
                }
            },
            dismissButton = {
                if (!descargando) {
                    TextButton(onClick = { actualizacion = null }) { Text("Después") }
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                PESTANAS.forEach { p ->
                    NavigationBarItem(
                        selected = pestana == p.id,
                        onClick = { pestana = p.id },
                        icon = { Text(p.emoji) },
                        label = { Text(p.titulo) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color(0xFF6B7280),
                            unselectedTextColor = Color(0xFF6B7280),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (pestana) {
                "tmv" -> TransfermovilScreen()
                else -> EtecsaScreen()
            }
        }
    }
}

fun marcarCodigo(context: Context, codigo: String, llamadaDirecta: Boolean = false) {
    val uri = Uri.parse("tel:" + Uri.encode(codigo))
    val accion = if (llamadaDirecta) Intent.ACTION_CALL else Intent.ACTION_DIAL
    startActivityCon(context, Intent(accion, uri))
}

fun abrirEnlace(context: Context, url: String) {
    startActivityCon(context, Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

private fun startActivityCon(context: Context, intent: Intent) {
    context.startActivity(Intent(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}
