package com.luis971227.miservicios.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.data.Gasto
import com.luis971227.miservicios.data.NOTA
import com.luis971227.miservicios.data.RepositorioGastos
import com.luis971227.miservicios.marcarCodigo
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

fun totalDelMes(gastos: List<Gasto>): Double {
    val cal = Calendar.getInstance()
    val anio = cal.get(Calendar.YEAR)
    val mes = cal.get(Calendar.MONTH)
    return gastos.filter {
        val c = Calendar.getInstance()
        c.timeInMillis = it.fecha
        c.get(Calendar.YEAR) == anio && c.get(Calendar.MONTH) == mes
    }.sumOf { it.importe }
}

@Composable
fun InicioScreen(irA: (String) -> Unit) {
    val context = LocalContext.current
    val gastos by RepositorioGastos.gastos.collectAsState()
    val moneda = NumberFormat.getCurrencyInstance(Locale("es", "CU"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TituloPestana("misServicios", "Tus servicios de ETECSA y Transfermóvil en un solo lugar")

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Bienvenido 👋",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Consulta saldo, marca los códigos USSD, conoce cada servicio y lleva el control de lo que gastas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ElevatedCard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🧾 Gastado este mes", style = MaterialTheme.typography.bodySmall)
                    Text(
                        moneda.format(totalDelMes(gastos)),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            ElevatedCard(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("💰 Total registrado", style = MaterialTheme.typography.bodySmall)
                    Text(
                        moneda.format(gastos.sumOf { it.importe }),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Acceso rápido", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { marcarCodigo(context, "*222#") }) { Text("📞 Consultar saldo") }
                    Button(onClick = { irA("gastos") }) { Text("🧾 Agregar gasto") }
                }
            }
        }

        TituloPestana("Secciones")
        FilaDestacada("📱", "ETECSA · saldo, bonos y recargas") { irA("etecsa") }
        FilaDestacada("💳", "Transfermóvil · pagos y transferencias") { irA("tmv") }
        FilaDestacada("🧾", "Gastos · registro local de tu actividad") { irA("gastos") }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                NOTA,
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FilaDestacada(emoji: String, texto: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier.size(34.dp),
            contentAlignment = Alignment.Center
        ) { Text(emoji) }
        Text(texto, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text("›", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
    }
}
