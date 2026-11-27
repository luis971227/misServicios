package com.luis971227.miservicios.util

import java.util.Random

/**
 * Codificador USSD de Transfermovil replicado de la app oficial.
 * Marca transferencias en una sola cadena: *444*45*<payload codificado>*<version>#
 * El payload se codifica con sustitucion por pares (tabla de 100) + XOR con
 * clave ciclica sembrada, igual que hace la APK de ETECSA.
 */
object TransfermovilUssd {

    private const val CODE_VERSION = "372653282298718711"
    private const val VERSION_TEL = "1260416"
    private const val CRUDE_CODE = "10000110101100011000100100100000110010001100000011100010100110010001010010100010111110001100010000011000010101100000000110001100101010100100011000000101110001100001100010001011110001100010100001000110000001101000101001001101001000001000010001100100011010010111001000110011110010001001100001000100000010000111001000110000101110100001111110011000001000110010000111110001100001111111000110011000101001100101000110001000111100011000101011101011111100111010100010111101000110000110011000110011100011000100111000101000011100010100111000011101100011000010101010000010101000100010100011001010101101000110001011101100011001001101101001111100011000000100010001100100010110101000110110000110000101000100011000000010100000010010001101011011100010110001110001001011100000110110001100001000011001011010100101100010001100100011110100011000011000110001100101010110101011110001100010000101000110001001101010010001001110001000010110110001100100111010001101101000110010010110001000110101000011110110000011101000110001001001100100010010000001111000110010011101110001100000001011000110000100011001111101000110000010010100011000110111100101100101011001100000010011000110000000001000100111100011000101101000100100000100010010101000100011100011000001101110001110011000101001"

    private val tabla: List<Int> by lazy {
        val lista = ArrayList<Int>()
        var pos = 0
        var j = 0
        while (pos + 8 <= CRUDE_CODE.length) {
            pos += Character.getNumericValue(CODE_VERSION[j % CODE_VERSION.length])
            lista.add(Integer.parseInt(CRUDE_CODE.substring(pos, pos + 8), 2))
            pos += 8
            j++
        }
        lista
    }

    private fun pad2(n: Int): String = if (n < 10) "0$n" else n.toString()

    private fun sustituir(campo: String): String {
        val sb = StringBuilder()
        val n = campo.length
        var i = 0
        if (n % 2 == 0) {
            while (i < n) {
                val v = tabla[Integer.parseInt(campo.substring(i, i + 2))]
                sb.append(if (v >= 10) v.toString() else if (v == 0) "00" else "0$v")
                i += 2
            }
        } else {
            val singles = IntArray(10)
            var idx = 0
            for (v in tabla) {
                if (v < 10) {
                    singles[idx] = v
                    idx++
                    if (idx == 10) break
                }
            }
            while (i < n - 1) {
                val v = tabla[Integer.parseInt(campo.substring(i, i + 2))]
                sb.append(if (v >= 10) v.toString() else if (v == 0) "00" else "0$v")
                i += 2
            }
            sb.append(singles[Character.getNumericValue(campo[n - 1])].toString())
        }
        return sb.toString()
    }

    private fun codificarCampo(campo: String, semilla: Int): String {
        var idx = semilla
        val clave = StringBuilder()
        for (i in 0 until campo.length) {
            if (idx == 99) {
                clave.append(tabla[0])
                idx = 1
            } else {
                clave.append(tabla[idx])
                idx++
            }
        }
        var claveStr = clave.toString()
        val sub = sustituir(campo)
        if (claveStr.length > sub.length) claveStr = claveStr.substring(0, sub.length)
        return (claveStr.toLong() xor sub.toLong()).toString()
    }

    fun code(cadena: String, semilla: Int): String {
        val campos = cadena.split("*")
        val longs = campos.map { it.length }
        val salida = ArrayList<String>()
        salida.add(semilla.toString())
        for (c in campos) salida.add(codificarCampo(c, semilla))
        val out = StringBuilder()
        if (campos.size == 1) {
            out.append(pad2(semilla)).append(pad2(longs[0])).append(salida[1])
        } else {
            out.append(pad2(semilla))
            for (i in 1..campos.size) {
                if (i > 1) out.append("*")
                out.append(pad2(longs[i - 1])).append(salida[i])
            }
        }
        return out.toString()
    }

    fun construirTransferencia(
        banco: String,
        destino: String,
        monto: String,
        moneda: String,
        telefono: String,
        concepto: String,
        origen: String = ""
    ): String? {
        return try {
            val destinoLimpio = destino.replace("-", "").trim()
            val montoLimpio = monto.trim()
            val tel = telefono.trim()
            val origenLimpio = origen.replace("-", "").trim()
            if (destinoLimpio.isEmpty() || montoLimpio.isEmpty()) return null
            if (!destinoLimpio.all { it.isDigit() } || !montoLimpio.all { it.isDigit() }) return null
            if (destinoLimpio.length > 19 || montoLimpio.length > 19 || tel.length > 19) return null
            val usaOrigen = origenLimpio.length >= 4 && origenLimpio.length <= 19 && origenLimpio.all { it.isDigit() }
            val monedaNum = when (moneda.uppercase()) {
                "MLC" -> "2"
                "USD" -> "3"
                else -> "1"
            }
            val msg = if (concepto.isNotEmpty() && concepto.all { it.isDigit() }) concepto else "0000"
            val semilla = Random().nextInt(100)
            val payload = when {
                usaOrigen ->
                    destinoLimpio + "*" + montoLimpio + "*" + monedaNum + "*0*" + origenLimpio + "*" +
                        (if (tel.length == 8) tel else "0000") + "*0*" + msg + "*0"
                banco == "01" || banco == "03" ->
                    destinoLimpio + "*" + montoLimpio + "*" + monedaNum + "*" +
                        (if (tel.length == 8) tel else "00000000")
                banco == "02" ->
                    destinoLimpio + "*" + montoLimpio + "*" + monedaNum + "*" + monedaNum + "*0000*" +
                        (if (tel.length == 8) tel else "0000") + "*0*" + msg + "*0"
                else ->
                    destinoLimpio + "*" + montoLimpio + "*" +
                        (if (tel.length == 8) tel else "00000000")
            }
            "*444*45*" + code(payload, semilla) + "*" + VERSION_TEL + "#"
        } catch (e: Exception) {
            null
        }
    }
}
