package com.luis971227.miservicios.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.abrirEnlace
import com.luis971227.miservicios.data.Modulos
import com.luis971227.miservicios.data.ModuloApp
import com.luis971227.miservicios.data.OpcionMenu
import com.luis971227.miservicios.marcarCodigo

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
fun PantallaModulo(modulo: ModuloApp) {
    val context = LocalContext.current
    var abierta by remember(modulo.id) { mutableStateOf<OpcionMenu?>(null) }
    BackHandler(enabled = abierta != null) { abierta = null }

    val opcion = abierta
    if (opcion == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloPestana(modulo.emoji + " " + modulo.titulo, modulo.presentacion)
            modulo.opciones.forEach { opcionMenu ->
                TarjetaOpcion(opcionMenu) { abierta = opcionMenu }
            }
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    Modulos.nota,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        DetalleOpcion(
            opcion = opcion,
            onMarcar = { marcarCodigo(context, it) },
            onAbrir = { abrirEnlace(context, it) },
            onVolver = { abierta = null }
        )
    }
}

@Composable
private fun TarjetaOpcion(opcion: OpcionMenu, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.size(38.dp),
                contentAlignment = Alignment.Center
            ) { Text(opcion.emoji) }
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(opcion.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (opcion.descripcion.isNotBlank()) {
                    Text(
                        opcion.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (opcion.pendiente) {
                    Text(
                        "⏳ Pendiente de la guía",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Text("›", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun DetalleOpcion(opcion: OpcionMenu, onMarcar: (String) -> Unit, onAbrir: (String) -> Unit, onVolver: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver, modifier = Modifier.padding(top = 8.dp)) {
            Text("‹ Volver al menú")
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(opcion.emoji, style = MaterialTheme.typography.headlineMedium)
                Text(opcion.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (opcion.descripcion.isNotBlank()) {
                    Text(
                        opcion.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (opcion.pendiente) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("⏳ En preparación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Esta opción se completará con la guía y los códigos que se pasen al proyecto.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        opcion.codigo?.let { codigo ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Código USSD", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        codigo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(onClick = { onMarcar(codigo) }) { Text("Marca $codigo") }
                }
            }
        }

        if (opcion.pasos.isNotEmpty()) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Pasos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    opcion.pasos.forEachIndexed { i, paso ->
                        Text(
                            "${i + 1}. $paso",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        opcion.enlace?.let { url ->
            OutlinedButton(
                onClick = { onAbrir(url) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("🌐 Abrir portal") }
        }
    }
}
