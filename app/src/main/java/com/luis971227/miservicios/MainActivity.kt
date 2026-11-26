package com.luis971227.miservicios

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.luis971227.miservicios.ui.EtecsaScreen
import com.luis971227.miservicios.ui.GastosScreen
import com.luis971227.miservicios.ui.InicioScreen
import com.luis971227.miservicios.ui.TransfermovilScreen
import com.luis971227.miservicios.ui.theme.MisServiciosTheme

data class Pestana(val id: String, val titulo: String, val emoji: String)

val PESTANAS = listOf(
    Pestana("inicio", "Inicio", "🏠"),
    Pestana("etecsa", "ETECSA", "📱"),
    Pestana("tmv", "Transfermóvil", "💳"),
    Pestana("gastos", "Gastos", "🧾")
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
    var pestana by remember { mutableStateOf("inicio") }

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
                "etecsa" -> EtecsaScreen()
                "tmv" -> TransfermovilScreen()
                "gastos" -> GastosScreen()
                else -> InicioScreen(irA = { pestana = it })
            }
        }
    }
}

fun marcarCodigo(context: Context, codigo: String) {
    startActivityCon(context, Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(codigo))))
}

fun abrirEnlace(context: Context, url: String) {
    startActivityCon(context, Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

private fun startActivityCon(context: Context, intent: Intent) {
    context.startActivity(Intent(intent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}
