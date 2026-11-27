package com.luis971227.miservicios.util

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.graphics.drawable.GradientDrawable
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class AsistenteUssd : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var pendiente = false
    private var cortina: View? = null
    @Volatile private var cortinaPendiente = false
    @Volatile private var cortinaOcultadaManual = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        instancia = this
    }

    override fun onDestroy() {
        ocultarCortina()
        instancia = null
        super.onDestroy()
    }

    override fun onAccessibilityEvent(evento: AccessibilityEvent?) {
        if (evento == null) return
        val tipo = evento.eventType
        if (tipo != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            tipo != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) return
        if (pendiente) return
        pendiente = true
        handler.post { pendiente = false; intentar() }
        handler.postDelayed({ intentar() }, 100)
        handler.postDelayed({ intentar() }, 300)
        handler.postDelayed({ intentar() }, 800)
    }

    override fun onInterrupt() {}

    private fun intentar() {
        val prefs = getSharedPreferences("miservicios", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("asistente_activo", false)) return
        if (System.currentTimeMillis() - prefs.getLong("asistente_ts", 0) > 10 * 60 * 1000) {
            ocultarCortina()
            return
        }

        val destino = prefs.getString("asistente_destino", "") ?: ""
        val telefono = prefs.getString("asistente_telefono", "") ?: ""
        val monto = prefs.getString("asistente_monto", "") ?: ""
        val concepto = prefs.getString("asistente_concepto", "") ?: ""
        val moneda = prefs.getString("asistente_moneda", "CUP") ?: "CUP"
        val tipoTransfer = prefs.getString("asistente_tipo", "terceros") ?: "terceros"
        if (destino.isBlank() && monto.isBlank() && concepto.isBlank() && telefono.isBlank()) return

        val raiz = rootInActiveWindow ?: return
        val prompt = StringBuilder()
        recolectar(raiz, prompt, 0)
        val texto = prompt.toString()
        if (texto.isBlank()) return

        if (texto.contains("pin", ignoreCase = true) ||
            texto.contains("exitosa", ignoreCase = true) ||
            texto.contains("rechazad", ignoreCase = true) ||
            texto.contains("fallid", ignoreCase = true)
        ) {
            ocultarCortina()
            return
        }
        if (cortina == null && !cortinaPendiente && !cortinaOcultadaManual) {
            mostrarCortina()
        }

        val campo = buscarCampo(raiz, 0)
        val esConfirmacion = texto.contains("está seguro", true) ||
            texto.contains("seguro que", true) ||
            texto.contains("confirmar", true) ||
            texto.contains("confirma", true)
        val esMoneda = Regex("\\b(moneda|divisa|cup|mlc)\\b", RegexOption.IGNORE_CASE).containsMatchIn(texto)
        val esTipoTransfer = texto.contains("tercero", true) || texto.contains("propi", true) ||
            texto.contains("ajen", true)
        if (campo == null) {
            if (esConfirmacion) {
                pulsarBoton()
            } else if (esTipoTransfer) {
                val buscados = if (tipoTransfer == "terceros") {
                    listOf("tercero", "ajen")
                } else {
                    listOf("propi")
                }
                pulsarPrimeraOpcion(buscados)
            } else if (esMoneda) {
                pulsarBotonDeTexto(moneda)
            }
            return
        }
        if (!campo.text.isNullOrBlank()) return

        val valor = when {
            esConfirmacion -> "1"
            texto.contains("monto", ignoreCase = true) ||
                texto.contains("importe", ignoreCase = true) ||
                texto.contains("cantidad", ignoreCase = true) -> monto
            texto.contains("mensaje", ignoreCase = true) ||
                texto.contains("concepto", ignoreCase = true) ||
                texto.contains("notificaci", ignoreCase = true) -> concepto
            esMoneda -> Regex("(\\d)\\s*\\.?\\s*" + moneda, RegexOption.IGNORE_CASE)
                .find(texto)?.groupValues?.getOrNull(1)
                ?: if (moneda == "MLC") "2" else "1"
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
            pulsarBoton()
            handler.postDelayed({ pulsarBoton() }, 200)
            handler.postDelayed({ pulsarBoton() }, 600)
        }
    }

    private fun pulsarBoton() {
        val raiz = rootInActiveWindow ?: return
        val boton = buscarBoton(raiz, 0) ?: return
        boton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private fun pulsarBotonDeTexto(buscado: String) {
        val raiz = rootInActiveWindow ?: return
        val boton = buscarBotonConTexto(raiz, buscado, 0) ?: return
        boton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private fun pulsarPrimeraOpcion(buscados: List<String>) {
        val raiz = rootInActiveWindow ?: return
        for (buscado in buscados) {
            val boton = buscarBotonConTexto(raiz, buscado, 0)
            if (boton != null) {
                boton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                handler.postDelayed({
                    val raiz2 = rootInActiveWindow ?: return@postDelayed
                    buscarBotonConTexto(raiz2, buscado, 0)?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }, 200)
                return
            }
        }
    }

    private fun buscarBoton(nodo: AccessibilityNodeInfo?, nivel: Int): AccessibilityNodeInfo? {
        if (nodo == null || nivel > 25) return null
        if (nodo.isClickable) {
            val t = (nodo.text?.toString() ?: "") + " " + (nodo.contentDescription?.toString() ?: "")
            val acepta = listOf(
                "enviar", "envía", "ok", "aceptar", "acepta",
                "sí", "confirmar", "continuar", "send"
            ).any { t.trim().equals(it, true) || t.trim().startsWith(it, true) }
            if (acepta) return nodo
        }
        for (i in 0 until nodo.childCount) {
            buscarBoton(nodo.getChild(i), nivel + 1)?.let { return it }
        }
        return null
    }

    private fun buscarBotonConTexto(nodo: AccessibilityNodeInfo?, buscado: String, nivel: Int): AccessibilityNodeInfo? {
        if (nodo == null || nivel > 25) return null
        if (nodo.isClickable) {
            val t = nodo.text?.toString() ?: ""
            if (t.contains(buscado, ignoreCase = true)) return nodo
        }
        for (i in 0 until nodo.childCount) {
            buscarBotonConTexto(nodo.getChild(i), buscado, nivel + 1)?.let { return it }
        }
        return null
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

    fun mostrarCortina() {
        cortinaOcultadaManual = false
        if (cortinaPendiente) return
        cortinaPendiente = true
        handler.post {
            if (cortina != null) return@post
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val emoji = TextView(this).apply {
                text = "🤖"
                textSize = 34f
                gravity = Gravity.CENTER
            }
            val titulo = TextView(this).apply {
                text = "Atendiendo el banco…"
                setTextColor(0xFFFFFFFF.toInt())
                textSize = 18f
                gravity = Gravity.CENTER
                setPadding(0, 30, 0, 0)
            }
            val texto = TextView(this).apply {
                text = "Estamos haciendo tu transferencia.\nCuando llegue tu PIN, escr\u00edbelo."
                setTextColor(0xFFB9C4D4.toInt())
                textSize = 15f
                gravity = Gravity.CENTER
                setPadding(0, 18, 0, 0)
            }
            val progreso = ProgressBar(this)
            val boton = TextView(this).apply {
                text = "Ocultar"
                setTextColor(0xFF8FA3BC.toInt())
                textSize = 13f
                gravity = Gravity.CENTER
                setPadding(40, 24, 40, 24)
                background = GradientDrawable().apply {
                    setColor(0xFF223146.toInt())
                    cornerRadius = 60f
                }
                setOnClickListener {
                    cortinaOcultadaManual = true
                    ocultarCortina()
                }
            }
            val tarjeta = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(70, 70, 70, 46)
                background = GradientDrawable().apply {
                    setColor(0xFF15202E.toInt())
                    cornerRadius = 64f
                }
            }
            tarjeta.addView(
                emoji,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { gravity = Gravity.CENTER }
            )
            tarjeta.addView(
                progreso,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { gravity = Gravity.CENTER; topMargin = 30 }
            )
            tarjeta.addView(titulo)
            tarjeta.addView(texto)
            tarjeta.addView(
                boton,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { gravity = Gravity.CENTER; topMargin = 44 }
            )
            val contenido = FrameLayout(this).apply {
                setBackgroundColor(0xFF0A0E17.toInt())
                foregroundGravity = Gravity.CENTER
            }
            contenido.addView(
                tarjeta,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply { gravity = Gravity.CENTER }
            )
            try {
                val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.OPAQUE
                )
                wm.addView(contenido, params)
                cortina = contenido
            } catch (_: Exception) {
                if (Settings.canDrawOverlays(this)) {
                    try {
                        val params = WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                            PixelFormat.OPAQUE
                        )
                        wm.addView(contenido, params)
                        cortina = contenido
                    } catch (_: Exception) {
                    }
                }
            }
            handler.postDelayed({ if (cortina === contenido) ocultarCortina() }, 120_000)
        }
    }

    fun ocultarCortina() {
        cortinaPendiente = false
        handler.post {
            val vista = cortina ?: return@post
            cortina = null
            try {
                (getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(vista)
            } catch (_: Exception) {
            }
        }
    }

    companion object {
        var instancia: AsistenteUssd? = null

        fun estaActivo(contexto: Context): Boolean {
            val servicios = Settings.Secure.getString(
                contexto.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
            return servicios.contains("com.luis971227.miservicios")
        }
    }
}
