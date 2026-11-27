package com.luis971227.miservicios.ui.transfermovil

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.luis971227.miservicios.data.CuentaGuardada
import com.luis971227.miservicios.data.Cuentas
import com.luis971227.miservicios.data.DatosPerfil
import com.luis971227.miservicios.data.Modulos
import com.luis971227.miservicios.data.Perfil
import com.luis971227.miservicios.util.Qr

@Composable
fun PantallaPerfil(onVolver: () -> Unit) {
    val context = LocalContext.current
    val inicial = remember { Perfil.leer(context) }
    var nombre by remember { mutableStateOf(inicial.nombre) }
    var telefono by remember { mutableStateOf(inicial.telefono) }
    var tarjeta by remember { mutableStateOf(inicial.tarjeta) }
    var guardado by remember { mutableStateOf(false) }
    var escaneado by remember { mutableStateOf<DatosPerfil?>(null) }
    var cuentas by remember { mutableStateOf(Cuentas.listar(context)) }

    BackHandler { onVolver() }

    val escaner = rememberLauncherForActivityResult(ScanContract()) { resultado ->
        val texto = resultado.contents ?: return@rememberLauncherForActivityResult
        escaneado = Perfil.parsearQr(texto)
    }

    val qr = remember(nombre, telefono, tarjeta) {
        if (tarjeta.isNotBlank()) {
            Qr.generar(Perfil.textoQr(DatosPerfil(nombre, telefono, tarjeta)))
        } else {
            null
        }
    }

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
                        listOf(
                            Color(Modulos.tmv.colorA.toInt()),
                            Color(Modulos.tmv.colorB.toInt())
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
                    ) { Text("👤", fontSize = 27.sp) }
                    Column(
                        modifier = Modifier.padding(start = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            "Mi perfil y QR",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "Tu tarjeta, tu QR y tus cuentas guardadas",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-24).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "MIS DATOS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; guardado = false },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it; guardado = false },
                        label = { Text("Teléfono") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tarjeta,
                        onValueChange = { tarjeta = it; guardado = false },
                        label = { Text("Número de tarjeta") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            Perfil.guardar(context, DatosPerfil(nombre, telefono, tarjeta))
                            guardado = true
                        },
                        enabled = tarjeta.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(Modulos.tmv.colorB.toInt())
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (guardado) "✅ Datos guardados" else "💾 Guardar mis datos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            qr?.let { imagen ->
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
                            "MI CÓDIGO QR",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White
                        ) {
                            Image(
                                bitmap = imagen.asImageBitmap(),
                                contentDescription = "QR con mis datos",
                                modifier = Modifier
                                    .padding(14.dp)
                                    .size(230.dp)
                            )
                        }
                        Text(
                            "Muéstralo para que te escaneen y te transfieran a esta tarjeta.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "ESCANEAR UN QR",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        "Escanea el QR de otra persona para obtener su tarjeta y transferirle.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedButton(
                        onClick = {
                            escaner.launch(
                                ScanOptions()
                                    .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                                    .setPrompt("Apunta al código QR")
                                    .setBeepEnabled(false)
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "📷 Escanear código QR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "CUENTAS GUARDADAS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    if (cuentas.isEmpty()) {
                        Text(
                            "Todavía no has guardado cuentas. Se guardan cuando transferir o escaneas un QR.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    cuentas.forEach { cuenta ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                if (cuenta.nombre.isNotBlank()) {
                                    Text(
                                        cuenta.nombre,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    cuenta.numero,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(onClick = {
                                Cuentas.eliminar(context, cuenta.numero)
                                cuentas = Cuentas.listar(context)
                            }) { Text("🗑️") }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    escaneado?.let { datos ->
        AlertDialog(
            onDismissRequest = { escaneado = null },
            title = { Text("✅ QR escaneado") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (datos.nombre.isNotBlank()) Text("Nombre: " + datos.nombre)
                    if (datos.telefono.isNotBlank()) Text("Teléfono: " + datos.telefono)
                    if (datos.tarjeta.isNotBlank()) Text("Tarjeta: " + datos.tarjeta)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Cuentas.guardar(
                            context,
                            CuentaGuardada(datos.nombre, datos.tarjeta.ifBlank { datos.telefono })
                        )
                        cuentas = Cuentas.listar(context)
                        escaneado = null
                    },
                    enabled = datos.tarjeta.isNotBlank() || datos.telefono.isNotBlank(),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Guardar cuenta") }
            },
            dismissButton = {
                TextButton(onClick = { escaneado = null }) { Text("Cerrar") }
            }
        )
    }
}
