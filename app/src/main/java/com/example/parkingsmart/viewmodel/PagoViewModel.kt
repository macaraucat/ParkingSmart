package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingsmart.model.PagoUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PagoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUIState())
    val uiState: StateFlow<PagoUIState> = _uiState.asStateFlow()

    fun inicializarPago(monto: Double, tiempo: String) {
        _uiState.update {
            it.copy(
                montoTotal = monto, 
                tiempoTotal = tiempo,
                pagoExitoso = false,
                estaProcesando = false,
                mensajeError = null
            )
        }
    }

    fun seleccionarMetodoPago(metodo: String) {
        _uiState.update { it.copy(metodoPagoSeleccionado = metodo) }
    }

    fun procesarPago() {
        _uiState.update { it.copy(estaProcesando = true, mensajeError = null) }

        viewModelScope.launch {
            try {
                delay(2000)
                _uiState.update {
                    it.copy(estaProcesando = false, pagoExitoso = true)
                }
            } catch (_: Exception) { // Se usa '_' para evitar la advertencia de parámetro no usado
                _uiState.update {
                    it.copy(estaProcesando = false, mensajeError = "Pago rechazado. Intente nuevamente.")
                }
            }
        }
    }

    fun reiniciarEstado() {
        _uiState.value = PagoUIState()
    }
}