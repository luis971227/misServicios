package com.luis971227.miservicios.data

data class NodoCompra(
    val id: String,
    val titulo: String,
    val emoji: String,
    val descripcion: String = "",
    val precio: String? = null,
    val teclas: String? = null,
    val pendiente: Boolean = false,
    val color: Long = 0xFF6366F1,
    val hijos: List<NodoCompra> = emptyList()
)

object ComprasEtecsa {
    const val CODIGO = "*133#"

    val raiz = NodoCompra(
        id = "raiz",
        titulo = "Comprar paquetes y planes",
        emoji = "🛒",
        descripcion = "Menú *133# de ETECSA",
        hijos = listOf(
            NodoCompra(
                id = "datos",
                titulo = "Datos",
                emoji = "📶",
                descripcion = "Planes y paquetes de datos móviles.",
                teclas = "1",
                color = 0xFF0EA5E9,
                hijos = listOf(
                    NodoCompra(
                        id = "datos-tarifa",
                        titulo = "Tarifa por Consumo",
                        emoji = "⏱️",
                        teclas = "1>1",
                        pendiente = true,
                        color = 0xFF0EA5E9
                    ),
                    NodoCompra(
                        id = "datos-todus",
                        titulo = "Plan toDus",
                        emoji = "🟢",
                        teclas = "1>2",
                        pendiente = true,
                        color = 0xFF10B981
                    ),
                    NodoCompra(
                        id = "datos-diario",
                        titulo = "Plan Diario",
                        emoji = "📅",
                        teclas = "1>3",
                        pendiente = true,
                        color = 0xFFF59E0B
                    ),
                    NodoCompra(
                        id = "datos-planes",
                        titulo = "Planes",
                        emoji = "📦",
                        descripcion = "Paquetes mensuales combinados.",
                        teclas = "1>4",
                        color = 0xFF6366F1,
                        hijos = listOf(
                            NodoCompra(
                                id = "plan-45gb",
                                emoji = "📶",
                                titulo = "4.5 GB de datos",
                                precio = "240.00 CUP",
                                teclas = "1>4>1",
                                color = 0xFF6366F1
                            ),
                            NodoCompra(
                                id = "plan-2gb",
                                emoji = "📶",
                                titulo = "2 GB + 15 min + 20 SMS",
                                precio = "120.00 CUP",
                                teclas = "1>4>2",
                                color = 0xFF0EA5E9
                            ),
                            NodoCompra(
                                id = "plan-4gb",
                                emoji = "📶",
                                titulo = "4 GB + 35 min + 40 SMS",
                                precio = "240.00 CUP",
                                teclas = "1>4>3",
                                color = 0xFF10B981
                            ),
                            NodoCompra(
                                id = "plan-6gb",
                                emoji = "📶",
                                titulo = "6 GB + 60 min + 70 SMS",
                                precio = "360.00 CUP",
                                teclas = "1>4>4",
                                color = 0xFF8B5CF6
                            )
                        )
                    )
                )
            ),
            NodoCompra(
                id = "sms",
                titulo = "SMS",
                emoji = "✉️",
                descripcion = "Bolsos y planes de mensajes.",
                teclas = "2",
                pendiente = true,
                color = 0xFF8B5CF6
            ),
            NodoCompra(
                id = "voz",
                titulo = "Voz",
                emoji = "🗣️",
                descripcion = "Planes y bolsos de minutos.",
                teclas = "3",
                pendiente = true,
                color = 0xFF10B981
            ),
            NodoCompra(
                id = "plan-amigos",
                titulo = "Plan Amigos",
                emoji = "👥",
                descripcion = "Números amigos y bonificación.",
                teclas = "4",
                pendiente = true,
                color = 0xFFF59E0B
            )
        )
    )
}
