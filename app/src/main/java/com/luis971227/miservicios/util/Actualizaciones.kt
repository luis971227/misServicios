package com.luis971227.miservicios.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.FileProvider
import com.luis971227.miservicios.BuildConfig
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object Actualizaciones {

    private const val REPO = "luis971227/misServicios"
    private const val NOMBRE_APK = "app-debug.apk"

    data class VersionRemota(val version: String, val urlApk: String)

    fun hayActualizacion(context: Context, alTerminar: (VersionRemota?) -> Unit) {
        val main = Handler(Looper.getMainLooper())
        Thread {
            var resultado: VersionRemota? = null
            var conn: HttpURLConnection? = null
            try {
                val conexion = URL("https://api.github.com/repos/$REPO/releases/latest").openConnection() as HttpURLConnection
                conn = conexion
                conn.connectTimeout = 10000
                conn.readTimeout = 10000
                conn.setRequestProperty("Accept", "application/vnd.github+json")
                if (conn.responseCode == 200) {
                    val texto = conn.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(texto)
                    val tag = json.optString("tag_name", "").removePrefix("v")
                    val assets = json.optJSONArray("assets")
                    var urlApk: String? = null
                    if (assets != null) {
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            if (asset.optString("name", "") == NOMBRE_APK) {
                                urlApk = asset.optString("browser_download_url", "")
                            }
                        }
                    }
                    if (tag.isNotEmpty() && !urlApk.isNullOrEmpty() && esMasNueva(tag, BuildConfig.VERSION_NAME)) {
                        resultado = VersionRemota(tag, urlApk)
                    }
                }
            } catch (e: Exception) {
                resultado = null
            } finally {
                conn?.disconnect()
            }
            main.post { alTerminar(resultado) }
        }.start()
    }

    fun descargar(
        context: Context,
        url: String,
        onProgreso: (Int) -> Unit,
        alListo: (File?) -> Unit
    ) {
        val main = Handler(Looper.getMainLooper())
        Thread {
            var destino: File? = null
            try {
                val conexion = URL(url).openConnection() as HttpURLConnection
                conexion.connectTimeout = 15000
                conexion.readTimeout = 30000
                val total = conexion.contentLengthLong
                val salida = File(context.cacheDir, "misServicios-actualizado.apk")
                salida.outputStream().use { out ->
                    conexion.inputStream.use { input ->
                        val buffer = ByteArray(64 * 1024)
                        var leidos = 0L
                        var leido = input.read(buffer)
                        var ultimo = -1
                        while (leido != -1) {
                            leidos += leido
                            out.write(buffer, 0, leido)
                            if (total > 0) {
                                val pct = ((leidos * 100) / total).toInt()
                                if (pct != ultimo) {
                                    ultimo = pct
                                    main.post { onProgreso(pct) }
                                }
                            }
                            leido = input.read(buffer)
                        }
                    }
                }
                destino = salida
            } catch (e: Exception) {
                destino = null
            }
            main.post { alListo(destino) }
        }.start()
    }

    fun instalar(context: Context, archivo: File) {
        val uri = FileProvider.getUriForFile(context, context.packageName + ".files", archivo)
        val intent = Intent(Intent.ACTION_VIEW, uri)
            .setDataAndType(uri, "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun esMasNueva(remota: String, local: String): Boolean {
        val a = remota.split('.').map { it.trim().toIntOrNull() ?: 0 }
        val b = local.split('.').map { it.trim().toIntOrNull() ?: 0 }
        val largo = maxOf(a.size, b.size)
        for (i in 0 until largo) {
            val pa = a.getOrElse(i) { 0 }
            val pb = b.getOrElse(i) { 0 }
            if (pa != pb) return pa > pb
        }
        return false
    }
}
