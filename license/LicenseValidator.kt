package com.seyeal.licencia

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Copia de LicenseManager (repo luis971227/Licencia) para validar claves en esta app.
 * Debe mantenerse sincronizada con el SECRET del proyecto Licencia.
 *
 * Uso en el arranque de la app:
 *   when (val r = LicenseValidator.validate("$appCode", claveIntroducida)) {
 *       is LicenseValidator.Result.Valid -> { LicenseValidator.saveKey(context, "$appCode", clave) /* continuar */ }
 *       is LicenseValidator.Result.Invalid -> { /* mostrar r.reason y cerrar */ }
 *   }
 */
object LicenseValidator {

    const val SECRET = "SEYEAL-LICENCIA-SECRET-2026-v1"
    private const val APP_CODE = "$appCode"

    private val BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray()

    sealed class Result {
        object Valid : Result()
        data class Invalid(val reason: String) : Result()
    }

    fun validate(appCode: String = APP_CODE, key: String): Result {
        val parts = key.trim().uppercase(Locale.US).split("-")
        if (parts.size != 4 || parts[0] != "SEYEAL") {
            return Result.Invalid("Formato de clave incorrecto")
        }
        val (_, code, expiry, sig) = parts
        if (code != appCode.uppercase(Locale.US)) {
            return Result.Invalid("La clave no corresponde a esta app ($appCode)")
        }
        if (sig != signature(code, expiry)) {
            return Result.Invalid("Firma inválida: clave falsificada o mal copiada")
        }
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        sdf.isLenient = false
        val expiryDate = try {
            sdf.parse(expiry) ?: return Result.Invalid("Fecha de caducidad inválida")
        } catch (e: Exception) {
            return Result.Invalid("Fecha de caducidad inválida")
        }
        if (System.currentTimeMillis() > expiryDate.time + 24L * 3600 * 1000) {
            return Result.Invalid("Licencia caducada el $expiry")
        }
        return Result.Valid
    }

    /** Clave guardada previamente en este dispositivo, si existe y sigue siendo válida. */
    fun hasValidSavedKey(context: Context, appCode: String = APP_CODE): Boolean {
        val key = context.getSharedPreferences("licencia", Context.MODE_PRIVATE)
            .getString("key_$appCode", null) ?: return false
        return validate(appCode, key) is Result.Valid
    }

    fun saveKey(context: Context, appCode: String = APP_CODE, key: String) {
        context.getSharedPreferences("licencia", Context.MODE_PRIVATE)
            .edit().putString("key_$appCode", key).apply()
    }

    private fun signature(appCode: String, expiry: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(SECRET.toByteArray(), "HmacSHA256"))
        val digest = mac.doFinal((appCode.uppercase(Locale.US) + expiry).toByteArray())
        var bits = 0L
        for (i in 0 until 5) bits = (bits shl 8) or (digest[i].toLong() and 0xFF)
        val sb = StringBuilder()
        repeat(8) {
            val idx = ((bits shr (35 - it * 5)) and 0x1F).toInt()
            sb.append(BASE32[idx])
        }
        return sb.toString()
    }
}
