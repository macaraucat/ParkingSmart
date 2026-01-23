package com.example.parkingsmart.model

import com.google.android.gms.maps.model.LatLng

/**
 * Representa el estado de la interfaz de usuario para la pantalla de estacionamiento.
 *
 * @property idTicket El ID único del ticket de estacionamiento.
 * @property numeroEspacio El número del espacio de estacionamiento seleccionado.
 * @property estaActivo `true` si hay una sesión de estacionamiento activa, `false` en caso contrario.
 * @property tiempoTranscurridoSegundos El tiempo total transcurrido en segundos.
 * @property tiempoFormateado El tiempo transcurrido con formato de cadena (por ejemplo, "00:00").
 * @property costoActual El costo actual calculado del estacionamiento.
 * @property tarifaPorMinuto El costo del estacionamiento por minuto.
 * @property mensajeError Un mensaje de error opcional para mostrar.
 * @property estaCargando `true` si los datos se están cargando, `false` en caso contrario.
 * @property ubicacionActual La ubicación geográfica actual del usuario. Por defecto es CITT Duoc.
 */
data class ParkingUIState(
    val idTicket: Int = 0,
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
