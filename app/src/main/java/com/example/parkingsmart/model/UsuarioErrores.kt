package com.example.parkingsmart.model

/**
 * Representa los errores de validación para los campos de entrada del usuario.
 *
 * Cada propiedad corresponde a un campo de entrada y contendrá un mensaje de error
 * si la validación falla; de lo contrario, será nulo.
 *
 * @property nombre Un mensaje de error para el campo de nombre.
 * @property patente Un mensaje de error para el campo de patente.
 * @property correo Un mensaje de error para el campo de correo electrónico.
 * @property clave Un mensaje de error para el campo de contraseña.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val patente: String? = null,
    val correo: String? = null,
    val clave: String? = null
)
