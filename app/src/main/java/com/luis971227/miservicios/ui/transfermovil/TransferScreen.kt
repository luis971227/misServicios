package com.luis971227.miservicios.ui.transfermovil

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.luis971227.miservicios.data.CuentaGuardada
import com.luis971227.miservicios.data.Cuentas
import com.luis971227.miservicios.data.Modulos
import com.luis971227.miservicios.data.Perfil
import com.luis971227.miservicios.util.AsistenteUssd

@Composable
fun PantallaTransferir(
    onVolver: () -> Unit,
    onTransferir: (destino: String, detalle: String) -> Unit
) {
    val context = LocalContext.current
    var cuentas by remember { mutableStateOf(Cuentas.listar(context)) }
    var destino by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var moneda by remember { mutableStateOf("CUP") }
    var tipoTransfer by remember { mutableStateOf("terceros") }
    var concepto by remember { mutableStateOf("") }
    var guardarNueva by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    BackHandler { onVolver() }

    val escaner = rememberLauncherForActivityResult(ScanContract()) { resultado ->
        val texto = resultado.contents ?: return@rememberLauncherForActivityResult
        val datos = Perfil.parsearQr(texto)
        if (datos != null) {
            nombre = datos.nombre
            destino = datos.tarjeta.ifBlank { datos.telefono }
            telefono = datos.telefono
            mensaje = null
        } else {
            mensaje = "Este QR no trae el número en claro; escribe el destinatario manualmente"
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
                    ) { Text("💸", fontSize = 27.sp) }
                    Column(
                        modifier = Modifier.padding(start = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            "Transferir efectivo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "Todo en una pantalla y confirmas una vez",
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
                        "DESTINATARIO",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    if (cuentas.isNotEmpty()) {
                        cuentas.forEach { cuenta ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        destino = cuenta.numero
                                        nombre = cuenta.nombre
                                        telefono = cuenta.telefono
                                    }
                            ) {
                                Text(
                                    if (destino == cuenta.numero) "☑" else "⬜",
                                    fontSize = 18.sp
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(1.dp)
                                ) {
                                    if (cuenta.nombre.isNotBlank()) {
                                        Text(
                                            cuenta.nombre,
                                            style = MaterialTheme.typography.bodyMedium,
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
                    OutlinedTextField(
                        value = destino,
                        onValueChange = { destino = it },
                        label = { Text("Tarjeta o teléfono del destinatario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (destino.isNotBlank() && cuentas.none { it.numero == destino.trim() }) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre (para guardarla)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { guardarNueva = !guardarNueva }
                        ) {
                            Text(if (guardarNueva) "☑" else "⬜", fontSize = 18.sp)
                            Text(
                                "Guardar esta cuenta",
                                modifier = Modifier.padding(start = 10.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it.filter { c -> c.isDigit() }.take(8) },
                        label = { Text("Teléfono del destinatario (para el aviso)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
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
                            "📷 Escanear QR del destinatario",
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
                        "DETALLES",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto a transferir") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Tipo:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        listOf("terceros" to "A un tercero", "propias" to "Entre mis cuentas").forEach { (valor, etiqueta) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (tipoTransfer == valor) Color(Modulos.tmv.colorB.toInt()) else Color(0xFFEDEFF3),
                                modifier = Modifier.clickable { tipoTransfer = valor }
                            ) {
                                Text(
                                    etiqueta,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (tipoTransfer == valor) Color.White else Color(0xFF444B54)
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Moneda:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        listOf("CUP", "MLC").forEach { divisa ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (moneda == divisa) Color(Modulos.tmv.colorB.toInt()) else Color(0xFFEDEFF3),
                                modifier = Modifier.clickable { moneda = divisa }
                            ) {
                                Text(
                                    divisa,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = if (moneda == divisa) Color.White else Color(0xFF444B54)
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = concepto,
                        onValueChange = { concepto = it },
                        label = { Text("Concepto (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            mensaje?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val numero = destino.trim()
                    if (numero.isBlank() || monto.isBlank()) {
                        mensaje = "Escribe el destinatario y el monto"
                        return@Button
                    }
                    if (guardarNueva && cuentas.none { it.numero == numero }) {
                        Cuentas.guardar(context, CuentaGuardada(nombre, numero, telefono.trim()))
                    }
                    context.getSharedPreferences("miservicios", android.content.Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("asistente_activo", true)
                        .putString("asistente_destino", numero)
                        .putString("asistente_telefono", telefono.trim())
                        .putString("asistente_monto", monto.trim())
                        .putString("asistente_moneda", moneda)
                        .putString("asistente_tipo", tipoTransfer)
                        .putString("asistente_concepto", concepto.trim())
                        .putLong("asistente_ts", System.currentTimeMillis())
                        .apply()
                    AsistenteUssd.instancia?.mostrarCortina()
                    val partes = mutableListOf("Monto: " + monto.trim() + " " + moneda)
                    if (telefono.isNotBlank()) partes.add("Teléfono: " + telefono.trim())
                    if (concepto.isNotBlank()) partes.add("Concepto: " + concepto.trim())
                    onTransferir(numero, partes.joinToString(" · "))
                },
                enabled = destino.isNotBlank() && monto.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(Modulos.tmv.colorB.toInt())
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "📞 Confirmar y transferir",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            if (!AsistenteUssd.estaActivo(context)) {
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "🤖 Asistente de diálogos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Actívalo una sola vez y misServicios escribirá por ti la tarjeta, el monto y el mensaje en los diálogos del banco. Tu PIN siempre lo pones tú.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = {
                                context.startActivity(
                                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("⚙️ Activar en Ajustes de accesibilidad")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
