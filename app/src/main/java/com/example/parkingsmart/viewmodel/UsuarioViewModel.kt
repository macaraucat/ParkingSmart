package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import com.example.parkingsmart.model.UsuarioErrores
import com.example.parkingsmart.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUIState())
    val estado: StateFlow<UsuarioUIState> = _estado
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }
    fun onPatenteChange(valor: String) {
        _estado.update { it.copy(patente = valor, errores = it.errores.copy(patente = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }
    fun validarLogIn(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            patente = if (estadoActual.patente.isBlank()) "NO PUEDE ESTAR VACÍO" else if (estadoActual.patente.length > 6) "PATENTE INVÁLIDA" else null,
            clave = if (estadoActual.clave.length < 8) "DEBE TENER AL MENOS 8 CARACTERES" else null,
        )

        val hayErrores = listOfNotNull(
            errores.patente,
            errores.clave,
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    fun validarRegistro(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "NO PUEDE ESTAR VACÍO" else null,
            patente = if (estadoActual.patente.isBlank()) "NO PUEDE ESTAR VACÍO" else if (estadoActual.patente.length > 6) "PATENTE INVÁLIDA" else null,
            correo = if (!estadoActual.correo.contains("@")) "CORREO INVÁLIDO" else null,
            clave = if (estadoActual.clave.length < 8) "DEBE TENER AL MENOS 8 CARACTERES" else null,
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.patente,
            errores.correo,
            errores.clave,
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    fun limpiarDatos() {
        _estado.value = UsuarioUIState()
    }
}
