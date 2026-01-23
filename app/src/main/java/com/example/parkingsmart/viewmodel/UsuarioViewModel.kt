package com.example.parkingsmart.viewmodel

import androidx.lifecycle.ViewModel
import com.example.parkingsmart.model.UsuarioErrores
import com.example.parkingsmart.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.parkingsmart.repository.ParkingDao
import com.example.parkingsmart.repository.UsuarioEntity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica y el estado de la interfaz de usuario para el registro y el inicio de sesión de usuarios.
 *
 * @param dao El objeto de acceso a datos (DAO) para interactuar con la base de datos.
 */
class UsuarioViewModel(private val dao: ParkingDao) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUIState())
    val uiState: StateFlow<UsuarioUIState> = _uiState.asStateFlow()

    /** Actualiza el nombre en el estado de la interfaz de usuario. */
    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }
    /** Actualiza la patente en el estado de la interfaz de usuario. */
    fun onPatenteChange(valor: String) {
        _uiState.update { it.copy(patente = valor, errores = it.errores.copy(patente = null)) }
    }
    /** Actualiza el correo en el estado de la interfaz de usuario. */
    fun onCorreoChange(valor: String) {
        _uiState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }
    /** Actualiza la clave en el estado de la interfaz de usuario. */
    fun onClaveChange(valor: String) {
        _uiState.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }
    /** Actualiza el estado de aceptación de términos en la interfaz de usuario. */
    fun onAceptarTerminosChange(valor: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = valor) }
    }

    /**
     * Registra un nuevo usuario en la base de datos después de validar los datos.
     *
     * @param onSuccess Callback que se ejecuta si el registro es exitoso (aunque no se llama actualmente en la implementación).
     */
    fun registrarUsuario(onSuccess: () -> Unit) {
        if (!validarFormatoRegistro()) return

        viewModelScope.launch {
            try {
                val estadoActual = _uiState.value

                val usuarioExistente = dao.obtenerUsuarioPorCorreo(estadoActual.correo)
                if (usuarioExistente != null) {
                    _uiState.update { it.copy(errores = it.errores.copy(correo = "CORREO YA SE ENCUENTRA REGISTRADO")) }
                    return@launch
                }

                val nuevoUsuario = UsuarioEntity(
                    nombre = estadoActual.nombre,
                    correo = estadoActual.correo,
                    clave = estadoActual.clave,
                    patente = estadoActual.patente
                )

                dao.registrarUsuario(nuevoUsuario)
                // TODO: Llamar a onSuccess() aquí si es necesario.

            } catch (e: Exception) {
                println("Error al registrar: ${e.message}")
            }
        }
    }

    /**
     * Autentica a un usuario con el correo y la clave proporcionados.
     *
     * @param onSuccess Callback que se ejecuta si el inicio de sesión es exitoso.
     */
    fun loginUsuario(onSuccess: () -> Unit) {
        if (!validarFormatoLogin()) return

        val correo = _uiState.value.correo
        val clave = _uiState.value.clave

        viewModelScope.launch {
            val usuarioEncontrado = dao.login(correo, clave)

            if (usuarioEncontrado != null) {
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(errores = it.errores.copy(clave = "CLAVE O CORREO ELECTRÓNICO INCORRECTOS"))
                }
            }
        }
    }

    /**
     * Valida los campos del formulario de inicio de sesión.
     *
     * @return `true` si la validación es exitosa, `false` en caso contrario.
     */
    private fun validarFormatoLogin(): Boolean {
        val estadoActual = _uiState.value
        val errores = UsuarioErrores(
            correo = if (estadoActual.correo.isBlank()) "Ingrese su correo" else null,
            clave = if (estadoActual.clave.isBlank()) "Ingrese su clave" else null,
        )

        val hayErrores = listOfNotNull(errores.correo, errores.clave).isNotEmpty()
        _uiState.update { it.copy(errores = errores) }
        return !hayErrores
    }

    /**
     * Valida los campos del formulario de registro.
     *
     * @return `true` si la validación es exitosa, `false` en caso contrario.
     */
    private fun validarFormatoRegistro(): Boolean {
        val estadoActual = _uiState.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Este campo no puede estar vacío" else null,
            patente = if (estadoActual.patente.isBlank()) "Este campo no puede estar vacío" else if (estadoActual.patente.length > 6) "La patente no es válida" else null,
            correo = if (!estadoActual.correo.contains("@")) "El correo no es válido" else null,
            clave = if (estadoActual.clave.length < 8) "La clave debe tener al menos 8 caracteres" else null,
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.patente,
            errores.correo,
            errores.clave,
        ).isNotEmpty()

        _uiState.update { it.copy(errores = errores) }

        return !hayErrores
    }

    /**
     * Limpia todos los datos del estado de la interfaz de usuario, restableciendo el formulario.
     */
    fun limpiarDatos() {
        _uiState.value = UsuarioUIState()
    }
}
