package com.luis971227.miservicios.data

data class ModuloApp(
    val id: String,
    val titulo: String,
    val emoji: String,
    val presentacion: String,
    val colorA: Long,
    val colorB: Long,
    val opciones: List<OpcionMenu>,
    val nota: String = ""
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
    val plantilla: String? = null,
    val pantalla: String? = null,
    val inicio: String? = null,
    val ocultarCodigos: Boolean = false,
    val etiquetas: List<String> = emptyList()
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
                id = "comprar-datos",
                titulo = "Comprar datos",
                emoji = "📶",
                descripcion = "Los planes de datos, directo con precio.",
                pantalla = "compras",
                inicio = "datos-planes",
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "comprar-planes",
                titulo = "Comprar planes",
                emoji = "📦",
                descripcion = "Los paquetes mensuales combinados, directo con precio.",
                pantalla = "compras",
                inicio = "datos-planes",
                color = 0xFF6366F1
            ),
            OpcionMenu(
                id = "saldo-integral",
                titulo = "Consultar saldo integral",
                emoji = "💰",
                descripcion = "Saldo principal, voz, SMS, datos y vencimiento de la línea.",
                codigos = listOf("*222#"),
                ocultarCodigos = true,
                pasos = listOf("Marca *222#", "En unos segundos aparece el resumen en pantalla"),
                color = 0xFFF59E0B
            ),
            OpcionMenu(
                id = "plan-datos",
                titulo = "Consultar plan de datos",
                emoji = "📶",
                descripcion = "Datos restantes y fecha de validez.",
                codigos = listOf("*222*328#"),
                ocultarCodigos = true,
                pasos = listOf("Marca *222*328#", "Verás los datos disponibles y su validez"),
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "plan-voz",
                titulo = "Consultar plan de voz",
                emoji = "🗣️",
                descripcion = "Minutos de voz restantes.",
                codigos = listOf("*222*869#"),
                ocultarCodigos = true,
                pasos = listOf("Marca *222*869#", "Verás los minutos disponibles"),
                color = 0xFF10B981
            ),
            OpcionMenu(
                id = "plan-sms",
                titulo = "Consultar plan de SMS",
                emoji = "✉️",
                descripcion = "SMS restantes.",
                codigos = listOf("*222*767#"),
                ocultarCodigos = true,
                pasos = listOf("Marca *222*767#", "Verás los SMS disponibles"),
                color = 0xFF8B5CF6
            ),
            OpcionMenu(
                id = "bonos-usd",
                titulo = "Bonos y planes en USD",
                emoji = "💵",
                descripcion = "Bonificaciones y planes en moneda libremente convertible.",
                codigos = listOf("*222*266#"),
                ocultarCodigos = true,
                pasos = listOf("Marca *222*266#", "Verás tus bonificaciones y planes en USD"),
                color = 0xFF059669
            ),
            OpcionMenu(
                id = "estado-recarga",
                titulo = "Estado de recarga",
                emoji = "🔄",
                descripcion = "Límite de recarga restante o estado de la recarga nacional.",
                codigos = listOf("*222*732#"),
                ocultarCodigos = true,
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
                ocultarCodigos = true,
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
                etiquetas = listOf("Marcar por USSD", "Llamar al 164"),
                ocultarCodigos = true,
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
        ),
        nota = "La mayoría de los códigos son gratuitos y no necesitan internet. Si alguno no funciona, consulta www.etecsa.cu o llama al servicio al cliente."
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
                id = "autenticar",
                titulo = "Autenticarse",
                emoji = "🔑",
                descripcion = "Solo necesitas tu PIN de Transfermóvil. No es el PIN de la tarjeta ni de Telebanca.",
                codigos = listOf("*444*40*03#"),
                ocultarCodigos = true,
                pasos = listOf(
                    "Toca Autenticarse y se marca el código solo",
                    "Cuando el sistema lo pida, escribe tu PIN de Transfermóvil",
                    "Listo: se desbloquean las demás operaciones"
                ),
                color = 0xFFB45309
            ),
            OpcionMenu(
                id = "perfil",
                titulo = "Mi perfil y QR",
                emoji = "👤",
                descripcion = "Guarda tu tarjeta, genera tu QR y escanea el de otros.",
                pantalla = "perfil",
                color = 0xFF0D9488
            ),
            OpcionMenu(
                id = "transferir",
                titulo = "Transferir efectivo",
                emoji = "💸",
                descripcion = "Elige una cuenta guardada, escanea un QR o escribe la tarjeta.",
                pantalla = "transferir",
                codigos = listOf("*444*45#"),
                ocultarCodigos = true,
                color = 0xFF059669
            ),
            OpcionMenu(
                id = "pagar-electricidad",
                titulo = "Pagar electricidad",
                emoji = "⚡",
                descripcion = "Pago de la factura eléctrica.",
                codigos = listOf("*444*41#"),
                ocultarCodigos = true,
                color = 0xFFF59E0B
            ),
            OpcionMenu(
                id = "pagar-telefono",
                titulo = "Pagar teléfono",
                emoji = "📞",
                descripcion = "Pago del servicio telefónico.",
                codigos = listOf("*444*42#"),
                ocultarCodigos = true,
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "pagar-onat",
                titulo = "Pagar ONAT",
                emoji = "🧾",
                descripcion = "Pago de impuestos y tributos.",
                codigos = listOf("*444*43#"),
                ocultarCodigos = true,
                color = 0xFFDC2626
            ),
            OpcionMenu(
                id = "recargar-movil",
                titulo = "Recargar mi móvil",
                emoji = "📲",
                descripcion = "Recargar el saldo de tu propio número.",
                codigos = listOf("*444*54#"),
                ocultarCodigos = true,
                color = 0xFF10B981
            ),
            OpcionMenu(
                id = "recargar-nauta",
                titulo = "Recargar Nauta",
                emoji = "🌐",
                descripcion = "Recargar una cuenta de correo Nauta.",
                codigos = listOf("*444*59#"),
                ocultarCodigos = true,
                color = 0xFF6366F1
            ),
            OpcionMenu(
                id = "giro-postal",
                titulo = "Giro postal",
                emoji = "✉️",
                descripcion = "Enviar o cobrar un giro postal.",
                codigos = listOf("*444*64#"),
                ocultarCodigos = true,
                color = 0xFF8B5CF6
            ),
            OpcionMenu(
                id = "consultar-saldo",
                titulo = "Consultar saldo",
                emoji = "💰",
                descripcion = "Saldo disponible de tus cuentas.",
                codigos = listOf("*444*46#"),
                ocultarCodigos = true,
                color = 0xFFF59E0B
            ),
            OpcionMenu(
                id = "ultimas-operaciones",
                titulo = "Últimas operaciones",
                emoji = "🧮",
                descripcion = "Movimientos recientes de tu tarjeta.",
                codigos = listOf("*444*48#"),
                ocultarCodigos = true,
                color = 0xFF0EA5E9
            ),
            OpcionMenu(
                id = "consultar-limite",
                titulo = "Consultar límite",
                emoji = "📏",
                descripcion = "Límite de operaciones vigente.",
                codigos = listOf("*444*62#"),
                ocultarCodigos = true,
                color = 0xFF10B981
            ),
            OpcionMenu(
                id = "todas-cuentas",
                titulo = "Ver todas las cuentas",
                emoji = "🗂️",
                descripcion = "Listado de tus cuentas asociadas.",
                codigos = listOf("*444*58#"),
                ocultarCodigos = true,
                color = 0xFF6366F1
            ),
            OpcionMenu(
                id = "cerrar-sesion",
                titulo = "Cerrar sesión",
                emoji = "🚪",
                descripcion = "Termina la sesión y vuelve a bloquear las opciones.",
                codigos = listOf("*444*70#"),
                ocultarCodigos = true,
                color = 0xFF64748B
            ),
            OpcionMenu(
                id = "cambiar-pin",
                titulo = "Cambiar PIN",
                emoji = "🔒",
                descripcion = "Cambiar el PIN de Transfermóvil.",
                codigos = listOf("*444*69#"),
                ocultarCodigos = true,
                color = 0xFF7C3AED
            ),
            OpcionMenu(
                id = "cambiar-limite",
                titulo = "Cambiar límite",
                emoji = "⚖️",
                descripcion = "Modificar el límite de tus operaciones.",
                codigos = listOf("*444*61#"),
                ocultarCodigos = true,
                color = 0xFFDB2777
            ),
            OpcionMenu(
                id = "registro-bandec",
                titulo = "Registro BANDEC",
                emoji = "📝",
                descripcion = "Registrar tu tarjeta de BANDEC.",
                plantilla = "*444*49*02*{0}#",
                entrada = "Número de tarjeta",
                ocultarCodigos = true,
                color = 0xFF059669
            ),
            OpcionMenu(
                id = "registro-banmet",
                titulo = "Registro BANMET",
                emoji = "📝",
                descripcion = "Registrar tu tarjeta de BANMET.",
                plantilla = "*444*49*03*{0}#",
                entrada = "Número de tarjeta",
                ocultarCodigos = true,
                color = 0xFF0F766E
            ),
            OpcionMenu(
                id = "menu-ussd",
                titulo = "Menú USSD *99#",
                emoji = "☎️",
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
        ),
        nota = "Autentica tu tarjeta antes de operar: BANMET usa *444*40*03# y BANDEC *444*40*02#. Estos códigos provienen de fuentes de la comunidad y pueden cambiar con la política bancaria; si alguno falla, confirma en www.transfermovil.cu o en tu banco."
    )

    val nota = "La mayoría de los códigos son gratuitos y no necesitan internet. Si alguno no funciona, consulta www.etecsa.cu o llama al servicio al cliente."
}
