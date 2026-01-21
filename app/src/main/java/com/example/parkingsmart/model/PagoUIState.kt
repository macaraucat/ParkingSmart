package com.example.parkingsmart.model


data class PagoUIState(
    val montoTotal: Double = 0.0,
    val tiempoTotal: String = "0:00",
    val metodoPagoSeleccionado: String = "",
    val estaProcesando: Boolean = false,
    val pagoExitoso: Boolean = false,
    val mensajeError: String? = null
)