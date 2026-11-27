package com.luis971227.miservicios.data

import android.content.Context
import org.json.JSONObject

data class DatosPerfil(
    val nombre: String = "",
    val telefono: String = "",
    val tarjeta: String = ""
)

object Perfil {

    fun leer(context: Context): DatosPerfil {
        val prefs = context.getSharedPreferences("miservicios", Context.MODE_PRIVATE)
        return DatosPerfil(
            nombre = prefs.getString("perfil_nombre", "") ?: "",
            telefono = prefs.getString("perfil_telefono", "") ?: "",
            tarjeta = prefs.getString("perfil_tarjeta", "") ?: ""
        )
    }

    fun guardar(context: Context, datos: DatosPerfil) {
        context.getSharedPreferences("miservicios", Context.MODE_PRIVATE)
            .edit()
            .putString("perfil_nombre", datos.nombre.trim())
            .putString("perfil_telefono", datos.telefono.trim())
            .putString("perfil_tarjeta", datos.tarjeta.trim())
            .apply()
    }

    fun textoQr(datos: DatosPerfil): String {
        return JSONObject()
            .put("app", "miservicios")
            .put("nombre", datos.nombre.trim())
            .put("telefono", datos.telefono.trim())
            .put("tarjeta", datos.tarjeta.trim())
            .toString()
    }

    fun parsearQr(texto: String): DatosPerfil? {
        val t = texto.trim()
        try {
            val o = JSONObject(t)
            if (o.optString("app") == "miservicios") {
                return DatosPerfil(
                    nombre = o.optString("nombre", ""),
                    telefono = o.optString("telefono", ""),
                    tarjeta = o.optString("tarjeta", "")
                )
            }
            val tarjeta = listOf("tarjeta", "card", "cuenta", "account", "numero")
                .firstNotNullOfOrNull { k ->
                    o.optString(k, "").trim().takeIf { v -> v.isNotBlank() && v.matches(Regex("\\d{8,19}")) }
                }
            val telefono = listOf("telefono", "phone", "tel", "movil")
                .firstNotNullOfOrNull { k ->
                    o.optString(k, "").trim().takeIf { v -> v.matches(Regex("5\\d{7}")) }
                }
            val nombre = listOf("nombre", "name", "titular")
                .firstNotNullOfOrNull { k -> o.optString(k, "").trim().takeIf { it.isNotBlank() } }
            if (tarjeta != null || telefono != null) {
                return DatosPerfil(nombre = nombre ?: "", telefono = telefono ?: "", tarjeta = tarjeta ?: "")
            }
        } catch (_: Exception) {
        }
        val tarjeta = Regex("\\b\\d{16}\\b").find(t)?.value
        val telefono = Regex("\\b5\\d{7}\\b").find(t)?.value
        return if (tarjeta != null || telefono != null) {
            DatosPerfil(telefono = telefono ?: "", tarjeta = tarjeta ?: "")
        } else {
            null
        }
    }
}
