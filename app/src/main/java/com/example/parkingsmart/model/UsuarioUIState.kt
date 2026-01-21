package com.example.parkingsmart.model

data class UsuarioUIState(
    val nombre : String = "",
    val patente : String = "",
    val correo : String = "",
    val clave : String = "",
    val aceptaTerminos : Boolean = false,
    val errores : UsuarioErrores = UsuarioErrores()
)