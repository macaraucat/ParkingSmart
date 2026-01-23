package com.example.parkingsmart.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Objeto de Acceso a Datos (DAO) para la base de datos de estacionamiento.
 *
 * Esta interfaz define los métodos para interactuar con las tablas `usuarios` y `tickets`
 * en la base de datos de la aplicación. Utiliza suspend functions para operaciones asíncronas
 * con corutinas.
 */
@Dao
interface ParkingDao {
    /**
     * Registra un nuevo usuario en la base de datos.
     * Si el usuario ya existe (basado en la clave primaria), la operación se ignora.
     * @param usuario La entidad [UsuarioEntity] a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrarUsuario(usuario: UsuarioEntity)

    /**
     * Realiza el login de un usuario verificando el correo y la clave.
     * @param correo El correo electrónico del usuario.
     * @param clave La contraseña del usuario.
     * @return La [UsuarioEntity] si las credenciales son correctas, de lo contrario, null.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): UsuarioEntity?

    /**
     * Obtiene un usuario por su correo electrónico.
     * @param correo El correo electrónico del usuario a buscar.
     * @return La [UsuarioEntity] si se encuentra, de lo contrario, null.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?

    /**
     * Inserta un nuevo ticket de estacionamiento en la base de datos.
     * @param ticket La entidad [TicketEntity] a insertar.
     * @return El ID del ticket recién insertado.
     */
    @Insert
    suspend fun iniciarTicket(ticket: TicketEntity): Long

    /**
     * Obtiene el ticket activo para un usuario específico.
     * @param correo El correo electrónico del usuario.
     * @return El [TicketEntity] activo si existe, de lo contrario, null.
     */
    @Query("SELECT * FROM tickets WHERE usuarioCorreo = :correo AND estado = 'ACTIVO' LIMIT 1")
    suspend fun obtenerTicketActivo(correo: String): TicketEntity?

    /**
     * Actualiza un ticket existente en la base de datos.
     * @param ticket La entidad [TicketEntity] a actualizar.
     */
    @Update
    suspend fun actualizarTicket(ticket: TicketEntity)

    /**
     * Finaliza un ticket estableciendo su hora de salida, costo total y estado a 'PAGADO'.
     * @param ticketId El ID del ticket a finalizar.
     * @param horaSalida La hora de salida en milisegundos.
     * @param monto El costo total del estacionamiento.
     */
    @Query("UPDATE tickets SET horaSalida = :horaSalida, costoTotal = :monto, estado = 'PAGADO' WHERE id = :ticketId")
    suspend fun finalizarTicket(ticketId: Int, horaSalida: Long, monto: Double)
}
