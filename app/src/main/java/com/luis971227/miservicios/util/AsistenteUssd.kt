package com.luis971227.miservicios.util

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AsistenteUssd : AccessibilityService() {

    override fun onAccessibilityEvent(evento: AccessibilityEvent?) {
        if (evento == null) return
        val prefs = getSharedPreferences("miservicios", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("asistente_activo", false)) return
        if (System.currentTimeMillis() - prefs.getLong("asistente_ts", 0) > 10 * 60 * 1000) return

        val destino = prefs.getString("asistente_destino", "") ?: ""
        val telefono = prefs.getString("asistente_telefono", "") ?: ""
        val monto = prefs.getString("asistente_monto", "") ?: ""
        val concepto = prefs.getString("asistente_concepto", "") ?: ""
        if (destino.isBlank() && monto.isBlank() && concepto.isBlank() && telefono.isBlank()) return

        val paquete = evento.packageName?.toString() ?: return
        val esTelefono = paquete.contains("phone") ||
            paquete.contains("telephony") ||
            paquete.contains("telecom")
        if (!esTelefono) return

        val raiz = rootInActiveWindow ?: return
        val prompt = StringBuilder()
        recolectar(raiz, prompt)
        val texto = prompt.toString()
        if (texto.contains("pin", ignoreCase = true) && !texto.contains("cuenta", ignoreCase = true)) return

        val campo = buscarCampo(raiz) ?: return
        if (!campo.text.isNullOrBlank()) return

        val valor = when {
            texto.contains("monto", true) || texto.contains("importe", true) -> monto
            texto.contains("mensaje", true) ||
                texto.contains("concepto", true) ||
                texto.contains("notificaci", true) -> concepto
            texto.contains("teléfono", true) || texto.contains("telefono", true) -> telefono
            texto.contains("cuenta", true) ||
                texto.contains("tarjeta", true) ||
                texto.contains("depositar", true) ||
                texto.contains("número", true) ||
                texto.contains("numero", true) -> destino
            else -> ""
        }
        if (valor.isNotBlank()) campo.setText(valor)
    }

    override fun onInterrupt() {}

    private fun recolectar(nodo: AccessibilityNodeInfo?, destino: StringBuilder) {
        if (nodo == null) return
        nodo.text?.let { destino.append(it).append(' ') }
        nodo.contentDescription?.let { destino.append(it).append(' ') }
        for (i in 0 until nodo.childCount) recolectar(nodo.getChild(i), destino)
    }

    private fun buscarCampo(nodo: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (nodo == null) return null
        if (nodo.className == "android.widget.EditText" && nodo.isEditable) return nodo
        for (i in 0 until nodo.childCount) {
            buscarCampo(nodo.getChild(i))?.let { return it }
        }
        return null
    }

    companion object {
        fun estaActivo(contexto: Context): Boolean {
            val servicios = Settings.Secure.getString(
                contexto.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            return servicios.contains("com.luis971227.miservicios")
        }
    }
}
