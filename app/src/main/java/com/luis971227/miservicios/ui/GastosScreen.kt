package com.luis971227.miservicios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.data.CATEGORIAS_GASTO
import com.luis971227.miservicios.data.Gasto
import com.luis971227.miservicios.data.RepositorioGastos
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GastosScreen() {
    val context = LocalContext.current
    val gastos by RepositorioGastos.gastos.collectAsState()
    var categoria by remember { mutableStateOf(CATEGORIAS_GASTO.first()) }
    var importeTexto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val moneda = NumberFormat.getCurrencyInstance(Locale("es", "CU"))
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val importe = importeTexto.replace(",", ".").toDoubleOrNull()

    LaunchedEffect(Unit) { RepositorioGastos.cargar(context) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            TituloPestana("🧾 Gastos", "Registro local de recargas, pagos y transferencias")
        }
        item {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Nuevo registro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CATEGORIAS_GASTO.forEach { cat ->
                            FilterChip(
                                selected = categoria == cat,
                                onClick = { categoria = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = importeTexto,
                        onValueChange = { importeTexto = it },
                        label = { Text("Importe (CUP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            RepositorioGastos.agregar(context, categoria, descripcion, importe ?: 0.0)
                            importeTexto = ""
                            descripcion = ""
                        },
                        enabled = importe != null && importe > 0.0,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Agregar gasto") }
                }
            }
        }
        if (gastos.isEmpty()) {
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Aún no hay registros. Agrega tu primera recarga o pago 👆",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            item {
                Text(
                    "Historial (${gastos.size}) · Total: ${moneda.format(gastos.sumOf { it.importe })}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(gastos, key = { it.id }) { gasto ->
                TarjetaGasto(
                    gasto = gasto,
                    fecha = formatoFecha.format(Date(gasto.fecha)),
                    moneda = { moneda.format(it) },
                    onEliminar = { RepositorioGastos.eliminar(context, gasto.id) }
                )
            }
        }
    }
}

@Composable
private fun TarjetaGasto(gasto: Gasto, fecha: String, moneda: (Double) -> String, onEliminar: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    gasto.categoria + if (gasto.descripcion.isNotBlank()) " · ${gasto.descripcion}" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                moneda(gasto.importe),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar registro",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
