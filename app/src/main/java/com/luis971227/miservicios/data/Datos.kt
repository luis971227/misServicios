package com.luis971227.miservicios.data

data class Servicio(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val pasos: List<String> = emptyList(),
    val codigo: String? = null,
    val enlace: String? = null,
    val categoria: String = "general"
)

val SERVICIOS_ETECSA = listOf(
    Servicio(
        id = "etecsa-saldo",
        nombre = "Consultar saldo",
        descripcion = "Consulta el saldo principal y el vencimiento de tu línea (prepago).",
        pasos = listOf("Marca el código desde la app de Teléfono", "Espera unos segundos y aparece el saldo en pantalla"),
        codigo = "*222#",
        categoria = "saldo"
    ),
    Servicio(
        id = "etecsa-bonos",
        nombre = "Consultar bonos y datos",
        descripcion = "Muestra los datos móviles y bonificaciones disponibles en tu línea.",
        pasos = listOf("Marca el código desde la app de Teléfono", "Elige la opción de consulta en el menú"),
        codigo = "*222*266#",
        categoria = "saldo"
    ),
    Servicio(
        id = "etecsa-recarga",
        nombre = "Recargar el móvil",
        descripcion = "Recarga desde el portal de ETECSA o desde un sitio de recargas con tarjeta.",
        pasos = listOf("Entra al portal y elige Recarga", "Pon el número y la cantidad", "Paga con tarjeta y recibe el SMS de confirmación"),
        enlace = "https://www.etecsa.cu",
        categoria = "recarga"
    ),
    Servicio(
        id = "etecsa-oferta",
        nombre = "Planes y promociones",
        descripcion = "Paquetes de datos, planes de voz y promociones vigentes de ETECSA.",
        pasos = listOf("Abre el portal oficial", "Entra a Comercio electrónico / Ofertas"),
        enlace = "https://www.etecsa.cu",
        categoria = "plan"
    )
)

val SERVICIOS_TMV = listOf(
    Servicio(
        id = "tmv-ussd",
        nombre = "Menú USSD Transfermóvil",
        descripcion = "Canal alternativo de Transfermóvil sin instalar la app: funciona con el menú USSD de la línea.",
        pasos = listOf("Marca el código desde la app de Teléfono", "Navega por el menú con los números de opción"),
        codigo = "*99#",
        categoria = "pago"
    ),
    Servicio(
        id = "tmv-descarga",
        nombre = "Descargar Transfermóvil",
        descripcion = "Portal oficial para bajar la última versión de la app de Transfermóvil.",
        pasos = listOf("Abre el enlace en el navegador", "Descarga el APK y actívalo (permite apps de esta fuente)"),
        enlace = "https://www.transfermovil.cu",
        categoria = "pago"
    ),
    Servicio(
        id = "tmv-pagos",
        nombre = "Pagos de servicios",
        descripcion = "Paga electricidad, agua, teléfono y otros servicios con tarjeta cubana dentro de la app.",
        pasos = listOf("Entra a Pagos y elige el servicio", "Pon la cuenta y el importe", "Confirma con tu clave de Transfermóvil"),
        enlace = "https://www.transfermovil.cu",
        categoria = "pago"
    ),
    Servicio(
        id = "tmv-transferencias",
        nombre = "Transferencias entre tarjetas",
        descripcion = "Manda dinero de tu tarjeta a la de otra persona al instante.",
        pasos = listOf("Entra a Transferencias y elige Banco/CU o tarjetas propias", "Pon la tarjeta destino y el importe", "Confirma la operación"),
        enlace = "https://www.transfermovil.cu",
        categoria = "pago"
    ),
    Servicio(
        id = "tmv-saldo-tarjeta",
        nombre = "Consultar saldo de tarjeta",
        descripcion = "Consulta el saldo y el extracto de tus tarjetas asociadas en Banca Remota.",
        pasos = listOf("En Banca Remota elige Consulta de saldo", "Selecciona la tarjeta", "El sistema muestra el saldo disponible"),
        enlace = "https://www.transfermovil.cu",
        categoria = "saldo"
    ),
    Servicio(
        id = "zona-recargas",
        nombre = "Recargas y compras online",
        descripcion = "Plataforma oficial para recargas, pagos y compras con tarjeta cubana.",
        pasos = listOf("Abre el enlace en el navegador", "Regístrate o entra con tu cuenta"),
        enlace = "https://www.enzona.net",
        categoria = "recarga"
    )
)

val CATEGORIAS_GASTO = listOf("Recarga", "Datos", "Pagos", "Transferencia", "Otro")

val NOTA = "Los códigos y enlaces son informativos y pueden cambiar. Verifica siempre en los portales oficiales (etecsa.cu, transfermovil.cu)."
