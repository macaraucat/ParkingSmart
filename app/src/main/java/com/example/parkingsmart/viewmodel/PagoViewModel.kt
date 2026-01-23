package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingsmart.model.PagoUIState
import com.example.parkingsmart.repository.ParkingDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica y el estado de la pantalla de pago.
 *
 * @param dao El objeto de acceso a datos (DAO) para interactuar con la base de datos de estacionamiento.
 */
class PagoViewModel(private val dao: ParkingDao) : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUIState())
    val uiState = _uiState.asStateFlow()

    /**
     * Inicializa el estado del pago con el monto y el tiempo proporcionados.
     *
     * @param monto El monto total a pagar.
     * @param tiempo El tiempo total de estacionamiento.
     */
    fun inicializarPago(monto: Double, tiempo: String) {
        _uiState.update { it.copy(montoTotal = monto, tiempoTotal = tiempo) }
    }

    /**
     * Actualiza el método de pago seleccionado en el estado de la interfaz de usuario.
     *
     * @param metodo El método de pago seleccionado (p. ej., "Efectivo", "Tarjeta").
     */
    fun seleccionarMetodoPago(metodo: String) {
        _uiState.update { it.copy(metodoPagoSeleccionado = metodo) }
    }

    /**
     * Procesa el pago. Muestra un indicador de carga, actualiza el ticket en la base de datos
     * y actualiza el estado de la interfaz de usuario para indicar el éxito o el fracaso del pago.
     *
     * @param correoUsuario El correo electrónico del usuario para identificar el ticket activo.
     */
    fun procesarPago(correoUsuario: String) {
        if (_uiState.value.estaProcesando) return

        _uiState.update { it.copy(estaProcesando = true, mensajeError = null) }

        viewModelScope.launch {
            delay(1500) // Simula un retraso de la red o del procesamiento.
            try {
                val ticketActivo = dao.obtenerTicketActivo(correoUsuario)

                if (ticketActivo == null) {
                    _uiState.update { it.copy(mensajeError = "No se encontró un ticket activo.") }
                    return@launch
                }

                // Actualiza el ticket a "PAGADO" y guarda el costo total.
                val ticketPagado = ticketActivo.copy(
                    costoTotal = _uiState.value.montoTotal,
                    estado = "PAGADO"
                )

                dao.actualizarTicket(ticketPagado)

                _uiState.update { it.copy(pagoExitoso = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error: ${e.message}") }
            } finally {
                _uiState.update { it.copy(estaProcesando = false) }
            }
        }
    }

    /**
     * Restablece el estado de la interfaz de usuario a sus valores iniciales.
     */
    fun reiniciarEstado() {
        _uiState.value = PagoUIState()
    }
}
