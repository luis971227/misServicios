package com.luis971227.miservicios.data

data class ModuloApp(
    val id: String,
    val titulo: String,
    val emoji: String,
    val presentacion: String,
    val colorA: Long,
    val colorB: Long,
    val opciones: List<OpcionMenu>
)

data class OpcionMenu(
    val id: String,
    val titulo: String,
    val emoji: String,
    val descripcion: String = "",
    val codigos: List<String> = emptyList(),
    val enlace: String? = null,
    val pasos: List<String> = emptyList(),
    val pendiente: Boolean = false,
    val color: Long = 0xFF64748B,
    val entrada: String? = null,
    val plantilla: String? = null
)

object Modulos {

    val etecsa = ModuloApp(
        id = "etecsa",
        titulo = "ETECSA",
        emoji = "📱",
        presentacion = "Consultas, paquetes y recargas de tu línea con un toque.",
        colorA = 0xFF0B3D91,
        colorB = 0xFF2E7DFF,
        opciones = listOf(
            OpcionMenu(
                id = "comprar-paquetes",
                titulo = "Comprar paquetes y planes",
                emoji = "🛒",
                descripcion = "Compra planes de datos, voz o SMS con el menú interactivo.",
                codigos = listOf("*133#"),
                pasos = listOf(
                    "Marca *133#",
                    "Sigue las instrucciones del menú",
                    "Elige el plan de datos, voz o SMS que quieras"
                ),
                color = 0xFF6366F1
            ),
            OpcionMenu(
                id = "saldo-integral",
                titulo = "Consultar saldo integral",
                emoji = "💰",
                descripcion = "Saldo principal, voz, SMS, datos y vencimiento de la línea.",
                codigos = listOf("*222#"),
                pasos = listOf("Marca *222#", "En unos segundos aparece el resumen en pantalla"),
                color = 0xFFF59E0B
            ),
            OpcionMenu(
                id = "plan-datos",
                titulo = "Consultar plan de datos",
                emoji = "📶",
                descripcion = "Datos restantes y fecha de validez.",
                codigos = listOf("*222*328#"),
                pasos = listOf("Marca *222*328#", "Verás los datos disponibles y su validez"),
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "plan-voz",
                titulo = "Consultar plan de voz",
                emoji = "🗣️",
                descripcion = "Minutos de voz restantes.",
                codigos = listOf("*222*869#"),
                pasos = listOf("Marca *222*869#", "Verás los minutos disponibles"),
                color = 0xFF10B981
            ),
            OpcionMenu(
                id = "plan-sms",
                titulo = "Consultar plan de SMS",
                emoji = "✉️",
                descripcion = "SMS restantes.",
                codigos = listOf("*222*767#"),
                pasos = listOf("Marca *222*767#", "Verás los SMS disponibles"),
                color = 0xFF8B5CF6
            ),
            OpcionMenu(
                id = "bonos-usd",
                titulo = "Bonos y planes en USD",
                emoji = "💵",
                descripcion = "Bonificaciones y planes en moneda libremente convertible.",
                codigos = listOf("*222*266#"),
                pasos = listOf("Marca *222*266#", "Verás tus bonificaciones y planes en USD"),
                color = 0xFF059669
            ),
            OpcionMenu(
                id = "estado-recarga",
                titulo = "Estado de recarga",
                emoji = "🔄",
                descripcion = "Límite de recarga restante o estado de la recarga nacional.",
                codigos = listOf("*222*732#"),
                pasos = listOf("Marca *222*732#", "Verás el estado de tu recarga"),
                color = 0xFF14B8A6
            ),
            OpcionMenu(
                id = "recarga-tarjeta",
                titulo = "Recarga rápida con tarjeta",
                emoji = "🎫",
                descripcion = "Recarga con el código impreso en tu tarjeta.",
                plantilla = "*662*{0}#",
                entrada = "Código de la tarjeta de recarga",
                pasos = listOf(
                    "Toca la opción y escribe el código de la tarjeta",
                    "La app marca *662*código# automáticamente"
                ),
                color = 0xFFEF6C00
            ),
            OpcionMenu(
                id = "atencion-cliente",
                titulo = "Servicio al cliente",
                emoji = "☎️",
                descripcion = "Hablar con atención al cliente de ETECSA.",
                codigos = listOf("*2266", "164"),
                pasos = listOf("Marca *2266 o llama al 164"),
                color = 0xFFDC2626
            ),
            OpcionMenu(
                id = "guia",
                titulo = "Guía completa de opciones",
                emoji = "📖",
                descripcion = "Guía paso a paso con todas las opciones del menú.",
                pendiente = true,
                color = 0xFF8B5CF6
            )
        )
    )

    val tmv = ModuloApp(
        id = "tmv",
        titulo = "Transfermóvil",
        emoji = "💳",
        presentacion = "Códigos USSD y servicios con tu tarjeta cubana.",
        colorA = 0xFF00564B,
        colorB = 0xFF00BFA5,
        opciones = listOf(
            OpcionMenu(
                id = "ussd",
                titulo = "Códigos USSD",
                emoji = "🔢",
                descripcion = "Lista completa de códigos USSD de Transfermóvil.",
                pendiente = true,
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "menu-ussd",
                titulo = "Menú USSD *99#",
                emoji = "📞",
                codigos = listOf("*99#"),
                pasos = listOf(
                    "Marca *99#",
                    "Navega por el menú con los números de opción"
                ),
                color = 0xFF059669
            ),
            OpcionMenu(
                id = "portal",
                titulo = "Portal y aplicación",
                emoji = "🌐",
                enlace = "https://www.transfermovil.cu",
                pasos = listOf(
                    "Abre el enlace en el navegador",
                    "Descarga la app o entra al portal"
                ),
                color = 0xFF6366F1
            )
        )
    )

    val nota = "La mayoría de los códigos son gratuitos y no necesitan internet. Si alguno no funciona, consulta www.etecsa.cu o llama al servicio al cliente."
}
