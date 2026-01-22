package com.example.parkingsmart.model.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ParkingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?

    @Insert
    suspend fun iniciarTicket(ticket: TicketEntity): Long

    @Query("SELECT * FROM tickets WHERE usuarioCorreo = :correo AND estado = 'ACTIVO' LIMIT 1")
    suspend fun obtenerTicketActivo(correo: String): TicketEntity?

    @Update
    suspend fun actualizarTicket(ticket: TicketEntity)

    @Query("UPDATE tickets SET horaSalida = :horaSalida, costoTotal = :monto, estado = 'PAGADO' WHERE id = :ticketId")
    suspend fun finalizarTicket(ticketId: Int, horaSalida: Long, monto: Double)
}