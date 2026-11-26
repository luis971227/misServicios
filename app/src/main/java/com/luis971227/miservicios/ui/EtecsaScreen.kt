package com.luis971227.miservicios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.luis971227.miservicios.abrirEnlace
import com.luis971227.miservicios.data.RepositorioGastos
import com.luis971227.miservicios.data.SERVICIOS_ETECSA
import com.luis971227.miservicios.marcarCodigo

@Composable
fun EtecsaScreen() {
    val context = LocalContext.current
    LaunchedEffect(Unit) { RepositorioGastos.cargar(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TituloPestana(
            "📱 ETECSA",
            "Servicios de tu línea móvil: consulta, recargas y ofertas oficiales"
        )
        ListaServicios(
            servicios = SERVICIOS_ETECSA,
            onMarcar = { marcarCodigo(context, it) },
            onAbrir = { abrirEnlace(context, it) }
        )
    }
}
