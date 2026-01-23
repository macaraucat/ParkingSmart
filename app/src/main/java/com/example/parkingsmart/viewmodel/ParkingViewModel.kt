package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingsmart.model.ParkingUIState
import com.example.parkingsmart.repository.ParkingDao
import com.example.parkingsmart.repository.TicketEntity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica y el estado de la sesión de estacionamiento.
 *
 * @param dao El objeto de acceso a datos (DAO) para interactuar con la base de datos.
 */
class ParkingViewModel(private val dao: ParkingDao) : ViewModel() {

    private val _uiState = MutableStateFlow(ParkingUIState())
    val uiState: StateFlow<ParkingUIState> = _uiState.asStateFlow()
    private var trabajoCronometro: Job? = null

    /**
     * Actualiza la ubicación actual del usuario en el estado de la interfaz de usuario.
     *
     * @param latLng La nueva ubicación [LatLng].
     */
    fun actualizarUbicacion(latLng: LatLng) {
        _uiState.update { it.copy(ubicacionActual = latLng) }
    }

    /**
     * Selecciona un espacio de estacionamiento, crea un nuevo ticket en la base de datos
     * e inicia la sesión de estacionamiento, incluido el cronómetro.
     *
     * @param numeroEspacio El número del espacio de estacionamiento seleccionado.
     * @param correo El correo electrónico del usuario para asociar el ticket.
     * @param patente La patente del vehículo del usuario.
     */
    fun seleccionarYActivarEspacio(numeroEspacio: Int, correo: String, patente: String) {
        viewModelScope.launch {
            val ticket = TicketEntity(
                usuarioCorreo = correo,
                patente = patente,
                numeroEspacio = numeroEspacio,
                horaEntrada = System.currentTimeMillis()
            )
            val id = dao.iniciarTicket(ticket)
            _uiState.update {
                it.copy(
                    idTicket = id.toInt(),
                    numeroEspacio = numeroEspacio,
                    estaActivo = true,
                    tiempoTranscurridoSegundos = 0,
                    tiempoFormateado = "0:00",
                    costoActual = 0.0,
                    mensajeError = null,
                    estaCargando = false
                )
            }
            iniciarCronometro()
        }
    }

    /**
     * Inicia un cronómetro que actualiza el tiempo transcurrido y el costo cada segundo.
     * El trabajo del cronómetro se puede cancelar si es necesario.
     */
    fun iniciarCronometro() {
        trabajoCronometro?.cancel()

        _uiState.update { it.copy(estaActivo = true) }

        trabajoCronometro = viewModelScope.launch {
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

    /**
     * Detiene el cronómetro y simula la generación de una boleta de pago.
     * Actualiza el estado de la interfaz de usuario y llama a onExito al completarse.
     *
     * @param onExito Callback a ejecutar cuando la boleta se genera con éxito.
     */
    fun generarBoleta(onExito: () -> Unit) {
        _uiState.update { it.copy(estaCargando = true, mensajeError = null) }

        trabajoCronometro?.cancel()

        viewModelScope.launch {
            try {
                delay(2000) // Simula una operación de red.

                _uiState.update { it.copy(estaCargando = false, estaActivo = false) }
                onExito()
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

    /**
     * Formatea un tiempo en segundos a un formato de cadena "H:MM:SS" o "M:SS".
     *
     * @param segundos El número total de segundos a formatear.
     * @return El tiempo formateado como una cadena.
     */
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

    /**
     * Limpia cualquier mensaje de error en el estado de la interfaz de usuario.
     */
    fun limpiarError() {
        _uiState.update { it.copy(mensajeError = null) }
    }

    /**
     * Cierra la sesión de estacionamiento, detiene el cronómetro y restablece el estado de la interfaz de usuario.
     */
    fun cerrarSesion() {
        trabajoCronometro?.cancel()
        _uiState.value = ParkingUIState()
    }
}
