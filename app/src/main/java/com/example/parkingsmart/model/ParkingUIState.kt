package com.example.parkingsmart.model

data class ParkingUIState(
    val numeroEspacio: Int = 0,
    val estaActivo: Boolean = false,
    val tiempoTranscurridoSegundos: Long = 0,
    val tiempoFormateado: String = "0:00",
    val costoActual: Double = 0.0,
    val tarifaPorMinuto: Double = 30.0,
    val mensajeError: String? = null,
    val estaCargando: Boolean = false
)