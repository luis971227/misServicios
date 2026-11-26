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
        return try {
            val o = JSONObject(texto)
            if (o.optString("app") != "miservicios") return null
            DatosPerfil(
                nombre = o.optString("nombre", ""),
                telefono = o.optString("telefono", ""),
                tarjeta = o.optString("tarjeta", "")
            )
        } catch (e: Exception) {
            null
        }
    }
}
