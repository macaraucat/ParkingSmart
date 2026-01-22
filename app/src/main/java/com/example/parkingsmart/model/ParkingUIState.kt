package com.example.parkingsmart.model

import com.google.android.gms.maps.model.LatLng

data class ParkingUIState(
    val numeroEspacio: Int = 0,
    val estaActivo: Boolean = false,
    val tiempoTranscurridoSegundos: Long = 0,
    val tiempoFormateado: String = "0:00",
    val costoActual: Double = 0.0,
    val tarifaPorMinuto: Double = 30.0,
    val mensajeError: String? = null,
    val estaCargando: Boolean = false,
    val ubicacionActual: LatLng = LatLng(-33.0335599, -71.5392316) // CITT Duoc por defecto
)