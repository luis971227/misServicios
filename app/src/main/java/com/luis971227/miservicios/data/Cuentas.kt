package com.luis971227.miservicios.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class CuentaGuardada(
    val nombre: String,
    val numero: String
)

object Cuentas {

    fun listar(context: Context): List<CuentaGuardada> {
        val json = context
            .getSharedPreferences("miservicios", Context.MODE_PRIVATE)
            .getString("cuentas", "[]") ?: "[]"
        val arr = JSONArray(json)
        return (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            CuentaGuardada(o.optString("nombre", ""), o.optString("numero", ""))
        }.filter { it.numero.isNotBlank() }
    }

    fun guardar(context: Context, cuenta: CuentaGuardada) {
        val lista = listar(context).toMutableList()
        lista.removeAll { it.numero == cuenta.numero }
        lista.add(0, cuenta)
        escribir(context, lista)
    }

    fun eliminar(context: Context, numero: String) {
        escribir(context, listar(context).filter { it.numero != numero })
    }

    private fun escribir(context: Context, lista: List<CuentaGuardada>) {
        val arr = JSONArray()
        lista.forEach {
            arr.put(JSONObject().put("nombre", it.nombre).put("numero", it.numero))
        }
        context
            .getSharedPreferences("miservicios", Context.MODE_PRIVATE)
            .edit()
            .putString("cuentas", arr.toString())
            .apply()
    }
}
