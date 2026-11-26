package com.luis971227.miservicios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.data.Servicio

@Composable
fun TituloPestana(titulo: String, nota: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        nota?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TarjetaServicio(
    servicio: Servicio,
    onMarcar: (String) -> Unit,
    onAbrir: (String) -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                servicio.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                servicio.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (servicio.pasos.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    servicio.pasos.forEachIndexed { i, paso ->
                        Text(
                            "${i + 1}. $paso",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                servicio.codigo?.let { codigo ->
                    Button(onClick = { onMarcar(codigo) }) { Text(codigo) }
                }
                servicio.enlace?.let { url ->
                    OutlinedButton(onClick = { onAbrir(url) }) { Text("Abrir portal") }
                }
            }
        }
    }
}

@Composable
fun ListaServicios(servicios: List<Servicio>, onMarcar: (String) -> Unit, onAbrir: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(servicios, key = { it.id }) { servicio ->
            TarjetaServicio(servicio, onMarcar = onMarcar, onAbrir = onAbrir)
        }
    }
}
