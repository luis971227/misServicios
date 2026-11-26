package com.luis971227.miservicios.util

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class AsistenteUssd : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var ultimoIntento = 0L

    override fun onAccessibilityEvent(evento: AccessibilityEvent?) {
        if (evento == null) return
        val tipo = evento.eventType
        if (tipo != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            tipo != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) return
        val ahora = SystemClock.uptimeMillis()
        if (ahora - ultimoIntento < 250) return
        ultimoIntento = ahora
        handler.postDelayed({ intentar() }, 300)
        handler.postDelayed({ intentar() }, 1000)
    }

    override fun onInterrupt() {}

    private fun intentar() {
        val prefs = getSharedPreferences("miservicios", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("asistente_activo", false)) return
        if (System.currentTimeMillis() - prefs.getLong("asistente_ts", 0) > 10 * 60 * 1000) return

        val destino = prefs.getString("asistente_destino", "") ?: ""
        val telefono = prefs.getString("asistente_telefono", "") ?: ""
        val monto = prefs.getString("asistente_monto", "") ?: ""
        val concepto = prefs.getString("asistente_concepto", "") ?: ""
        if (destino.isBlank() && monto.isBlank() && concepto.isBlank() && telefono.isBlank()) return

        val raiz = rootInActiveWindow ?: return
        val prompt = StringBuilder()
        recolectar(raiz, prompt, 0)
        val texto = prompt.toString()
        if (texto.isBlank()) return
        if (texto.contains("pin", ignoreCase = true) && !texto.contains("cuenta", ignoreCase = true)) return

        val campo = buscarCampo(raiz, 0) ?: return
        if (!campo.text.isNullOrBlank()) return

        val valor = when {
            texto.contains("monto", ignoreCase = true) ||
                texto.contains("importe", ignoreCase = true) -> monto
            texto.contains("mensaje", ignoreCase = true) ||
                texto.contains("concepto", ignoreCase = true) ||
                texto.contains("notificaci", ignoreCase = true) -> concepto
            texto.contains("teléfono", ignoreCase = true) ||
                texto.contains("telefono", ignoreCase = true) -> telefono
            texto.contains("cuenta", ignoreCase = true) ||
                texto.contains("tarjeta", ignoreCase = true) ||
                texto.contains("depositar", ignoreCase = true) ||
                texto.contains("número", ignoreCase = true) ||
                texto.contains("numero", ignoreCase = true) -> destino
            else -> ""
        }
        if (valor.isBlank()) return

        val argumentos = Bundle()
        argumentos.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            valor
        )
        var hecho = campo.performAction(
            AccessibilityNodeInfo.ACTION_SET_TEXT,
            argumentos
        )
        if (!hecho) {
            campo.setText(valor)
            hecho = campo.text?.toString() == valor
        }
        if (hecho) {
            Toast.makeText(this, "🤖 Asistente rellenó el campo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recolectar(nodo: AccessibilityNodeInfo?, destino: StringBuilder, nivel: Int) {
        if (nodo == null || nivel > 25) return
        nodo.text?.let { destino.append(it).append(' ') }
        nodo.contentDescription?.let { destino.append(it).append(' ') }
        for (i in 0 until nodo.childCount) recolectar(nodo.getChild(i), destino, nivel + 1)
    }

    private fun buscarCampo(nodo: AccessibilityNodeInfo?, nivel: Int): AccessibilityNodeInfo? {
        if (nodo == null || nivel > 25) return null
        if (nodo.className == "android.widget.EditText" && nodo.isEditable) return nodo
        for (i in 0 until nodo.childCount) {
            buscarCampo(nodo.getChild(i), nivel + 1)?.let { return it }
        }
        return null
    }

    companion object {
        fun estaActivo(contexto: Context): Boolean {
            val servicios = android.provider.Settings.Secure.getString(
                contexto.contentResolver,
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            return servicios.contains("com.luis971227.miservicios")
        }
    }
}
