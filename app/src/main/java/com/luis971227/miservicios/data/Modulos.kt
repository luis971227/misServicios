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
    val codigo: String? = null,
    val enlace: String? = null,
    val pasos: List<String> = emptyList(),
    val pendiente: Boolean = false,
    val color: Long = 0xFF64748B
)

object Modulos {

    val etecsa = ModuloApp(
        id = "etecsa",
        titulo = "ETECSA",
        emoji = "📱",
        presentacion = "Gestión de tu línea: paquetes, saldo y guía de opciones.",
        colorA = 0xFF0B3D91,
        colorB = 0xFF2E7DFF,
        opciones = listOf(
            OpcionMenu(
                id = "paquetes",
                titulo = "Comprar paquetes",
                emoji = "📦",
                descripcion = "Datos, voz y SMS.",
                pendiente = true,
                color = 0xFF6366F1
            ),
            OpcionMenu(
                id = "guia",
                titulo = "Guía de opciones",
                emoji = "📖",
                descripcion = "Guía paso a paso del menú de tu línea.",
                pendiente = true,
                color = 0xFF8B5CF6
            ),
            OpcionMenu(
                id = "saldo",
                titulo = "Consultar saldo",
                emoji = "💰",
                codigo = "*222#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
                    "En unos segundos aparece el saldo en pantalla"
                ),
                color = 0xFFF59E0B
            ),
            OpcionMenu(
                id = "bonos",
                titulo = "Consultar bonos y datos",
                emoji = "📊",
                codigo = "*222*266#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
                    "Elige la opción de consulta en el menú"
                ),
                color = 0xFF10B981
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
                codigo = "*99#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
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

    val nota = "Los códigos son informativos y pueden cambiar. Las opciones marcadas como «Pendiente» se completarán con las guías que se vayan pasando."
}
