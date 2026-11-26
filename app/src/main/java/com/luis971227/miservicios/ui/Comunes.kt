package com.luis971227.miservicios.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis971227.miservicios.abrirEnlace
import com.luis971227.miservicios.data.Modulos
import com.luis971227.miservicios.data.ModuloApp
import com.luis971227.miservicios.data.OpcionMenu
import com.luis971227.miservicios.marcarCodigo

@Composable
fun PantallaModulo(modulo: ModuloApp) {
    val context = LocalContext.current
    var abierta by remember(modulo.id) { mutableStateOf<OpcionMenu?>(null) }
    BackHandler(enabled = abierta != null) { abierta = null }

    val opcion = abierta
    if (opcion == null) {
        MenuModulo(modulo) { abierta = it }
    } else {
        DetalleOpcion(
            modulo = modulo,
            opcion = opcion,
            onMarcar = { marcarCodigo(context, it) },
            onAbrir = { abrirEnlace(context, it) },
            onVolver = { abierta = null }
        )
    }
}

@Composable
private fun MenuModulo(modulo: ModuloApp, onAbrirOpcion: (OpcionMenu) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraModulo(modulo)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            modulo.opciones.forEach { opcion ->
                TarjetaOpcion(opcion) { onAbrirOpcion(opcion) }
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    Modulos.nota,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CabeceraModulo(modulo: ModuloApp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(modulo.colorA.toInt()), Color(modulo.colorB.toInt()))
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
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
                .align(Alignment.BottomStart)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 42.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(modulo.emoji, fontSize = 30.sp)
            }
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modulo.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    modulo.presentacion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun TarjetaOpcion(opcion: OpcionMenu, onClick: () -> Unit) {
    val acento = Color(opcion.color.toInt())
    ElevatedCard(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AvatarOpcion(opcion.emoji, acento)
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    opcion.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (opcion.descripcion.isNotBlank()) {
                    Text(
                        opcion.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (opcion.codigo != null) {
                    Text(
                        opcion.codigo,
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = acento
                    )
                }
                if (opcion.pendiente) {
                    ChipPendiente()
                }
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(acento.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "›",
                    color = acento,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AvatarOpcion(emoji: String, acento: Color) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(acento.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 22.sp)
    }
}

@Composable
private fun ChipPendiente() {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF3DB),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Text(
            "⏳ Pendiente de la guía",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9A6B0F),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DetalleOpcion(
    modulo: ModuloApp,
    opcion: OpcionMenu,
    onMarcar: (String) -> Unit,
    onAbrir: (String) -> Unit,
    onVolver: () -> Unit
) {
    val acento = Color(opcion.color.toInt())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(modulo.colorA.toInt()), Color(modulo.colorB.toInt()))
                    )
                )
                .padding(bottom = 26.dp)
        ) {
            Column(modifier = Modifier.padding(start = 8.dp, end = 20.dp, top = 10.dp)) {
                TextButton(
                    onClick = onVolver,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Text("‹ Volver al menú", fontWeight = FontWeight.SemiBold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Text(opcion.emoji, fontSize = 27.sp) }
                    Column(
                        modifier = Modifier.padding(start = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            opcion.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        if (opcion.descripcion.isNotBlank()) {
                            Text(
                                opcion.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-18).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (opcion.pendiente) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8EE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "⏳ En preparación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A6B0F)
                        )
                        Text(
                            "Esta opción se completará con la guía y los códigos que se pasen al proyecto.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF7A5A22)
                        )
                    }
                }
            }

            opcion.codigo?.let { codigo ->
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "CÓDIGO USSD",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = acento.copy(alpha = 0.1f)
                        ) {
                            Text(
                                codigo,
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold,
                                color = acento
                            )
                        }
                        Button(
                            onClick = { onMarcar(codigo) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = acento),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "📲 Marcar $codigo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            if (opcion.pasos.isNotEmpty()) {
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "PASOS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        opcion.pasos.forEachIndexed { i, paso ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(acento.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${i + 1}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = acento
                                    )
                                }
                                Text(
                                    paso,
                                    modifier = Modifier.padding(start = 12.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            opcion.enlace?.let { url ->
                OutlinedButton(
                    onClick = { onAbrir(url) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "🌐 Abrir portal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
