package com.example.parkingsmart.model

/**
 * Representa el estado de la interfaz de usuario para la pantalla de pago.
 *
 * @property montoTotal El monto total a pagar.
 * @property tiempoTotal El tiempo total de estacionamiento, en formato de cadena.
 * @property metodoPagoSeleccionado El método de pago seleccionado por el usuario (por ejemplo, "Efectivo", "Tarjeta").
 * @property estaProcesando `true` si el pago se está procesando actualmente, `false` en caso contrario.
 * @property pagoExitoso `true` si el pago se ha completado con éxito, `false` en caso contrario.
 * @property mensajeError Un mensaje de error opcional para mostrar si ocurre un problema durante el proceso de pago.
 */
data class PagoUIState(
    val montoTotal: Double = 0.0,
    val tiempoTotal: String = "0:00",
    val metodoPagoSeleccionado: String = "",
    val estaProcesando: Boolean = false,
    val pagoExitoso: Boolean = false,
    val mensajeError: String? = null
)
