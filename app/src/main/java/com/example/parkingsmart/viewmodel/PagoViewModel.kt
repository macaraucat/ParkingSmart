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

class PagoViewModel(private val dao: ParkingDao) : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUIState())
    val uiState = _uiState.asStateFlow()

    fun inicializarPago(monto: Double, tiempo: String) {
        _uiState.update { it.copy(montoTotal = monto, tiempoTotal = tiempo) }
    }

    fun seleccionarMetodoPago(metodo: String) {
        _uiState.update { it.copy(metodoPagoSeleccionado = metodo) }
    }

    fun procesarPago(correoUsuario: String) {
        if (_uiState.value.estaProcesando) return

        _uiState.update { it.copy(estaProcesando = true, mensajeError = null) }

        viewModelScope.launch {
            delay(1500)
            try {
                val ticketActivo = dao.obtenerTicketActivo(correoUsuario)

                if (ticketActivo == null) {
                    _uiState.update { it.copy(mensajeError = "No se encontró un ticket activo.") }
                    return@launch
                }

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

    fun reiniciarEstado() {
        _uiState.value = PagoUIState()
    }
}
