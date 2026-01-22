package com.example.parkingsmart.model.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val patente: String
)

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