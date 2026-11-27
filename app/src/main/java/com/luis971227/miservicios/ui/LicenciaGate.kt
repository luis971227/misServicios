package com.luis971227.miservicios.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis971227.miservicios.util.Licencias

private val Rojo = Color(0xFFE53935)
private val RojoOscuro = Color(0xFF7A0C14)
private val FondoCard = Color(0xFF131318)
private val FondoInput = Color(0xFF0E0E12)
private val Borde = Color(0xFF33333B)
private val Gris = Color(0xFF8E8E96)
private val RojoSuave = Color(0xFFF2B8B5)
private val Verde = Color(0xFF4CAF50)

@Composable
fun LicenciaGate(onValidada: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var clave by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var colorMsg by remember { mutableStateOf(Rojo) }
    val device = Licencias.deviceCode(context)

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF3A0A0D), Color(0xFF120304)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(FondoCard, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF2A2A31), RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(64.dp)
                    .background(Brush.linearGradient(listOf(Rojo, RojoOscuro)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🔐", fontSize = 30.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "LICENCIA REQUERIDA",
                color = Color.White, fontSize = 18.sp,
                fontWeight = FontWeight.Black, letterSpacing = 2.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Esta licencia es para un solo teléfono.\nCopia los códigos y pídele la clave al administrador.",
                color = Gris, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 17.sp
            )
            Spacer(Modifier.height(16.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(FondoInput, RoundedCornerShape(12.dp))
                    .border(1.dp, Borde, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                FilaCodigo("APP", Licencias.APP_CODE)
                Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF26262B)))
                FilaCodigo("TELÉFONO", device)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    val codigos = Licencias.APP_CODE + "-" + device
                    clipboard.setText(AnnotatedString(codigos))
                    mensaje = "Códigos copiados: $codigos"
                    colorMsg = Verde
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B1114),
                    contentColor = RojoSuave
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("⧉ COPIAR CÓDIGOS", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it.uppercase() },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pega la licencia aquí", color = Color(0xFF63636B), fontSize = 13.sp) }
            )
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Brush.linearGradient(listOf(Rojo, RojoOscuro)), RoundedCornerShape(10.dp))
                    .clickable {
                        when (val r = Licencias.validar(context, clave)) {
                            is Licencias.Result.Valid -> {
                                Licencias.guardarClave(context, clave)
                                onValidada()
                            }
                            is Licencias.Result.Invalid -> {
                                mensaje = r.reason
                                colorMsg = Rojo
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "ACTIVAR LICENCIA",
                    color = Color.White, fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp
                )
            }
            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(mensaje, color = colorMsg, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun FilaCodigo(etiqueta: String, valor: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = Gris, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
        Text(
            valor.ifEmpty { "···" },
            color = RojoSuave, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp
        )
    }
}
