package com.luis971227.miservicios.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object Qr {

    fun generar(texto: String, lado: Int = 720): Bitmap {
        val matriz = QRCodeWriter().encode(
            texto,
            BarcodeFormat.QR_CODE,
            lado,
            lado
        )
        val pixeles = IntArray(lado * lado)
        for (y in 0 until lado) {
            for (x in 0 until lado) {
                pixeles[y * lado + x] = if (matriz[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(lado, lado, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixeles, 0, lado, 0, 0, lado, lado)
        return bitmap
    }
}
