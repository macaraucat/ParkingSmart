package com.example.parkingsmart.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Este archivo contiene las clases de entidad de Room para la base de datos de la aplicación.
 *
 * Define la estructura de las tablas `usuarios` y `tickets`.
 */

/**
 * Define la entidad `UsuarioEntity` para la tabla `usuarios` en la base de datos de Room.
 *
 * @property id El identificador único del usuario, generado automáticamente.
 * @property nombre El nombre completo del usuario.
 * @property correo El correo electrónico del usuario, que también se utiliza como identificador.
 * @property clave La contraseña del usuario.
 * @property patente La patente del vehículo del usuario.
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val patente: String
)

/**
 * Define la entidad `TicketEntity` para la tabla `tickets` en la base de datos de Room.
 *
 * @property id El identificador único del ticket, generado automáticamente.
 * @property usuarioCorreo El correo electrónico del usuario asociado a este ticket.
 * @property patente La patente del vehículo asociada a este ticket.
 * @property numeroEspacio El número del espacio de estacionamiento utilizado.
 * @property horaEntrada La hora de entrada al estacionamiento, en milisegundos.
 * @property horaSalida La hora de salida del estacionamiento, en milisegundos. Es nulo si el ticket está activo.
 * @property costoTotal El costo total del estacionamiento.
 * @property estado El estado actual del ticket. Puede ser "ACTIVO" o "PAGADO".
 */
@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioCorreo: String,
    val patente: String,
    val numeroEspacio: Int,
    val horaEntrada: Long,
    val horaSalida: Long? = null,
    val costoTotal: Double = 0.0,
    val estado: String = "ACTIVO" // Valores: "ACTIVO", "PAGADO"
)
