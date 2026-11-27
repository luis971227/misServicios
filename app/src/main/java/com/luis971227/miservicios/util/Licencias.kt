package com.luis971227.miservicios.util

import android.content.Context
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Licencias SEYEAL atadas a un teléfono.
 * Misma lógica y secreto que LicenseManager.kt (repo luis971227/Licencia).
 */
object Licencias {

    const val SECRET = "SEYEAL-0758pf1q5zq42eeablb3oss1-2026"
    const val APP_CODE = "MISSERV"
    private val BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray()

    /** Código único de este teléfono (ANDROID_ID) para el generador de licencias. */
    fun deviceCode(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?.uppercase(Locale.US) ?: ""

    fun firma(appCode: String, deviceCode: String, expiry: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(SECRET.toByteArray(), "HmacSHA256"))
        val digest = mac.doFinal(
            (appCode.uppercase(Locale.US) + deviceCode.uppercase(Locale.US) + expiry).toByteArray()
        )
        var bits = 0L
        for (i in 0 until 5) bits = (bits shl 8) or (digest[i].toLong() and 0xFF)
        val sb = StringBuilder()
        repeat(8) { sb.append(BASE32[((bits shr (35 - it * 5)) and 0x1F).toInt()]) }
        return sb.toString()
    }

    sealed class Result {
        object Valid : Result()
        data class Invalid(val reason: String) : Result()
    }

    fun validar(context: Context, key: String): Result {
        val device = deviceCode(context)
        val parts = key.trim().uppercase(Locale.US).split("-")
        if (parts.size != 5 || parts[0] != "SEYEAL") return Result.Invalid("Formato de clave incorrecto")
        if (parts[1] != APP_CODE) return Result.Invalid("La clave no corresponde a esta app ($APP_CODE)")
        if (device.isEmpty()) return Result.Invalid("No se pudo leer el código del teléfono")
        if (parts[2] != device) return Result.Invalid("Esta licencia es para OTRO teléfono")
        if (!Regex("^\\d{8}$").matches(parts[3])) return Result.Invalid("Fecha de caducidad inválida")
        if (parts[4] != firma(APP_CODE, device, parts[3])) {
            return Result.Invalid("Firma inválida: clave falsificada o mal copiada")
        }
        return try {
            val sdf = SimpleDateFormat("yyyyMMdd", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
            sdf.isLenient = false
            val exp = sdf.parse(parts[3]) ?: return Result.Invalid("Fecha de caducidad inválida")
            if (System.currentTimeMillis() > exp.time + 24L * 3600 * 1000) {
                Result.Invalid("Licencia caducada el ${parts[3]}")
            } else {
                Result.Valid
            }
        } catch (e: Exception) {
            Result.Invalid("Fecha de caducidad inválida")
        }
    }

    fun claveGuardada(context: Context): String? =
        context.getSharedPreferences("licencia", Context.MODE_PRIVATE)
            .getString("key_$APP_CODE", null)

    fun guardarClave(context: Context, key: String) {
        context.getSharedPreferences("licencia", Context.MODE_PRIVATE)
            .edit().putString("key_$APP_CODE", key.trim().uppercase(Locale.US)).apply()
    }

    fun quitarClave(context: Context) {
        context.getSharedPreferences("licencia", Context.MODE_PRIVATE)
            .edit().remove("key_$APP_CODE").apply()
    }
}
