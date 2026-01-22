package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingsmart.model.ParkingUIState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParkingViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(ParkingUIState())

    val uiState: StateFlow<ParkingUIState> = _uiState.asStateFlow()
    private var timerJob: Job? = null


    fun actualizarUbicacion(latLng: LatLng) {
        _uiState.update { it.copy(ubicacionActual = latLng) }
    }

    fun seleccionarYActivarEspacio(numeroEspacio: Int) {
        _uiState.update {
            it.copy(
                numeroEspacio = numeroEspacio,
                estaActivo = false, // Ahora se inicia solo al confirmar
                tiempoTranscurridoSegundos = 0,
                tiempoFormateado = "0:00",
                costoActual = 0.0,
                mensajeError = null,
                estaCargando = false
            )
        }
        // Ya no se llama a iniciarCronometro() aquí automáticamente
    }


    fun iniciarCronometro() {
        // Evitar múltiples cronómetros si ya está activo
        if (_uiState.value.estaActivo && timerJob?.isActive == true) return

        timerJob?.cancel()

        _uiState.update { it.copy(estaActivo = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.estaActivo) {
                delay(1000)

                _uiState.update { estadoActual ->
                    val nuevosSegundos = estadoActual.tiempoTranscurridoSegundos + 1


                    val minutos = (nuevosSegundos / 60.0)
                    val nuevoCosto = minutos * estadoActual.tarifaPorMinuto

                    estadoActual.copy(
                        tiempoTranscurridoSegundos = nuevosSegundos,
                        tiempoFormateado = formatearTiempo(nuevosSegundos),
                        costoActual = nuevoCosto
                    )
                }
            }
        }
    }


    fun generarBoleta(onSuccess: () -> Unit) {
        _uiState.update { it.copy(estaCargando = true, mensajeError = null) }

        timerJob?.cancel()

        viewModelScope.launch {
            try {
                delay(2000)


                _uiState.update { it.copy(estaCargando = false, estaActivo = false) }
                onSuccess()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        estaCargando = false,
                        mensajeError = "Error al generar boleta. Intente nuevamente."
                    )
                }
                iniciarCronometro()
            }
        }
    }


    private fun formatearTiempo(segundos: Long): String {
        val horas = segundos / 3600
        val minutos = (segundos % 3600) / 60
        val segs = segundos % 60

        return if (horas > 0) {
            "%d:%02d:%02d".format(horas, minutos, segs)
        } else {
            "%d:%02d".format(minutos, segs)
        }
    }

    fun limpiarError() {
        _uiState.update { it.copy(mensajeError = null) }
    }


fun cerrarSesion() {
    timerJob?.cancel()

    _uiState.value = ParkingUIState()
}
}
