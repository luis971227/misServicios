package com.luis971227.miservicios.ui.etecsa

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis971227.miservicios.data.ComprasEtecsa
import com.luis971227.miservicios.data.Modulos
import com.luis971227.miservicios.data.NodoCompra

@Composable
fun PantallaCompras(onVolver: () -> Unit, onMarcar: (String) -> Unit) {
    var ruta by remember { mutableStateOf(listOf(ComprasEtecsa.raiz)) }
    var seleccionado by remember { mutableStateOf<NodoCompra?>(null) }
    BackHandler(enabled = ruta.size > 1) { ruta = ruta.dropLast(1) }

    val actual = ruta.last()
    val acento = Color(Modulos.etecsa.colorB.toInt())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraCompras(
            ruta = ruta,
            onVolver = onVolver,
            onSubir = { ruta = ruta.dropLast(1) }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (ruta.size == 1) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = acento.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Toca una categoría para ver lo que puedes comprar. Al elegir un paquete la app marca *133# y te dice qué números responder.",
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            actual.hijos.forEach { hijo ->
                TarjetaNodo(
                    nodo = hijo,
                    acento = acento,
                    onToque = {
                        when {
                            hijo.pendiente -> {}
                            hijo.hijos.isNotEmpty() -> ruta = ruta + hijo
                            else -> seleccionado = hijo
                        }
                    }
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }

    seleccionado?.let { paquete ->
        val teclasTexto = paquete.teclas?.split(">")?.joinToString("  →  ") ?: ""
        AlertDialog(
            onDismissRequest = { seleccionado = null },
            title = { Text(paquete.emoji + " " + paquete.titulo) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (paquete.precio != null) {
                        Text(
                            "Precio: " + paquete.precio,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = acento
                        )
                    }
                    Text(
                        "La app va a marcar *133#. Cuando ETECSA te muestre el menú, responde con estos números:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        teclasTexto,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = acento
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        seleccionado = null
                        onMarcar(ComprasEtecsa.CODIGO)
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "📞 Marcar " + ComprasEtecsa.CODIGO,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { seleccionado = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun CabeceraCompras(
    ruta: List<NodoCompra>,
    onVolver: () -> Unit,
    onSubir: () -> Unit
) {
    val actual = ruta.last()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(Modulos.etecsa.colorA.toInt()),
                        Color(Modulos.etecsa.colorB.toInt())
                    )
                )
            )
            .padding(bottom = 44.dp)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
                .align(Alignment.TopEnd)
        )
        Column(modifier = Modifier.padding(start = 8.dp, end = 20.dp, top = 10.dp)) {
            TextButton(
                onClick = { if (ruta.size > 1) onSubir() else onVolver() },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) {
                Text(
                    if (ruta.size > 1) "‹ Atrás" else "‹ Volver al menú",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) { Text(actual.emoji, fontSize = 27.sp) }
                Column(
                    modifier = Modifier.padding(start = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        actual.titulo,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    if (ruta.size > 1) {
                        Text(
                            ruta.drop(1).joinToString(" › ") { it.titulo },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    } else {
                        Text(
                            "Elige qué quieres comprar hoy",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaNodo(
    nodo: NodoCompra,
    acento: Color,
    onToque: () -> Unit
) {
    val colorNodo = Color(nodo.color.toInt())
    val accionable = !nodo.pendiente
    ElevatedCard(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (accionable) {
                    Modifier.clickable { onToque() }
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorNodo.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(nodo.emoji, fontSize = 22.sp)
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    nodo.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (nodo.descripcion.isNotBlank()) {
                    Text(
                        nodo.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (nodo.precio != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colorNodo.copy(alpha = 0.12f)
                    ) {
                        Text(
                            nodo.precio,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorNodo
                        )
                    }
                }
                if (nodo.pendiente) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFF3DB)
                    ) {
                        Text(
                            "⏳ Pendiente de la captura",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9A6B0F),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(colorNodo.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "›",
                    color = colorNodo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
