package com.example.parkingsmart.model

/**
 * Representa el estado de la interfaz de usuario para las pantallas de registro y de inicio de sesión de usuario.
 *
 * @property nombre El nombre del usuario.
 * @property patente La patente del vehículo del usuario.
 * @property correo El correo electrónico del usuario.
 * @property clave La contraseña del usuario.
 * @property aceptaTerminos `true` si el usuario ha aceptado los términos y condiciones, `false` en caso contrario.
 * @property errores Un objeto [UsuarioErrores] que contiene cualquier error de validación para los campos de entrada.
 */
data class UsuarioUIState(
    val nombre : String = "",
    val patente : String = "",
    val correo : String = "",
    val clave : String = "",
    val aceptaTerminos : Boolean = false,
    val errores : UsuarioErrores = UsuarioErrores()
)
