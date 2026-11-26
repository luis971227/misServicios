package com.luis971227.miservicios.data

data class ModuloApp(
    val id: String,
    val titulo: String,
    val emoji: String,
    val presentacion: String,
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
    val pendiente: Boolean = false
)

object Modulos {

    val etecsa = ModuloApp(
        id = "etecsa",
        titulo = "ETECSA",
        emoji = "📱",
        presentacion = "Gestión de tu línea: paquetes, saldo y guía de opciones.",
        opciones = listOf(
            OpcionMenu(
                id = "paquetes",
                titulo = "Comprar paquetes",
                emoji = "📦",
                descripcion = "Compra de datos, voz y SMS.",
                pendiente = true
            ),
            OpcionMenu(
                id = "guia",
                titulo = "Guía de opciones",
                emoji = "📖",
                descripcion = "Guía paso a paso del menú de tu línea.",
                pendiente = true
            ),
            OpcionMenu(
                id = "saldo",
                titulo = "Consultar saldo",
                emoji = "💰",
                codigo = "*222#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
                    "En unos segundos aparece el saldo en pantalla"
                )
            ),
            OpcionMenu(
                id = "bonos",
                titulo = "Consultar bonos y datos",
                emoji = "📊",
                codigo = "*222*266#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
                    "Elige la opción de consulta en el menú"
                )
            )
        )
    )

    val tmv = ModuloApp(
        id = "tmv",
        titulo = "Transfermóvil",
        emoji = "💳",
        presentacion = "Códigos USSD y servicios con tu tarjeta cubana.",
        opciones = listOf(
            OpcionMenu(
                id = "ussd",
                titulo = "Códigos USSD",
                emoji = "🔢",
                descripcion = "Lista completa de códigos USSD de Transfermóvil.",
                pendiente = true
            ),
            OpcionMenu(
                id = "menu-ussd",
                titulo = "Menú USSD *99#",
                emoji = "📞",
                codigo = "*99#",
                pasos = listOf(
                    "Marca el código desde la app de Teléfono",
                    "Navega por el menú con los números de opción"
                )
            ),
            OpcionMenu(
                id = "portal",
                titulo = "Portal y aplicación",
                emoji = "🌐",
                enlace = "https://www.transfermovil.cu",
                pasos = listOf(
                    "Abre el enlace en el navegador",
                    "Descarga la app o entra al portal"
                )
            )
        )
    )

    val nota = "Los códigos son informativos y pueden cambiar. Las opciones marcadas como «Pendiente» se completarán con las guías que se vayan pasando."
}
