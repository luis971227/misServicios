package com.luis971227.miservicios.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

data class Gasto(
    val id: String,
    val fecha: Long,
    val categoria: String,
    val descripcion: String,
    val importe: Double
)

object RepositorioGastos {

    private val CLAVE = "gastos_json"
    private val NOMBRE_PREFS = "mis_servicios"

    private val _gastos = MutableStateFlow<List<Gasto>>(emptyList())
    val gastos: StateFlow<List<Gasto>> = _gastos.asStateFlow()

    fun cargar(context: Context) {
        val json = prefs(context).getString(CLAVE, null) ?: return
        try {
            val array = JSONArray(json)
            val lista = List(array.length()) { i ->
                val o = array.getJSONObject(i)
                Gasto(
                    id = o.getString("id"),
                    fecha = o.getLong("fecha"),
                    categoria = o.getString("categoria"),
                    descripcion = o.optString("descripcion", ""),
                    importe = o.getDouble("importe")
                )
            }
            _gastos.value = lista.sortedByDescending { it.fecha }
        } catch (e: Exception) {
            _gastos.value = emptyList()
        }
    }

    fun agregar(context: Context, categoria: String, descripcion: String, importe: Double) {
        val gasto = Gasto(
            id = java.util.UUID.randomUUID().toString(),
            fecha = System.currentTimeMillis(),
            categoria = categoria,
            descripcion = descripcion,
            importe = importe
        )
        _gastos.value = listOf(gasto) + _gastos.value
        guardar(context)
    }

    fun eliminar(context: Context, id: String) {
        _gastos.value = _gastos.value.filterNot { it.id == id }
        guardar(context)
    }

    private fun guardar(context: Context) {
        val array = JSONArray()
        _gastos.value.forEach { gasto ->
            array.put(
                JSONObject()
                    .put("id", gasto.id)
                    .put("fecha", gasto.fecha)
                    .put("categoria", gasto.categoria)
                    .put("descripcion", gasto.descripcion)
                    .put("importe", gasto.importe)
            )
        }
        prefs(context).edit().putString(CLAVE, array.toString()).apply()
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(NOMBRE_PREFS, Context.MODE_PRIVATE)
}
