package com.example.parkingsmart.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Clase de base de datos principal para la aplicación ParkingSmart.
 *
 * Esta clase abstracta extiende [RoomDatabase] y sirve como el punto de acceso principal
 * a la base de datos persistente de la aplicación. Define las entidades que contiene
 * y proporciona acceso al DAO ([ParkingDao]).
 *
 * @property entities Las clases de entidad que forman parte de la base de datos.
 * @property version El número de versión de la base de datos.
 * @property exportSchema Especifica si se debe exportar el esquema de la base de datos a un archivo JSON.
 */
@Database(entities = [UsuarioEntity::class, TicketEntity::class], version = 1, exportSchema = false)
abstract class ParkingDatabase : RoomDatabase() {

    /**
     * Proporciona una instancia del DAO para interactuar con la base de datos.
     * @return Una instancia de [ParkingDao].
     */
    abstract fun dao(): ParkingDao

    /**
     * Objeto complementario para proporcionar una instancia singleton de la base de datos.
     */
    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos.
         *
         * Utiliza un patrón singleton para garantizar que solo exista una instancia de la base de datos
         * en toda la aplicación. La creación de la instancia es segura para subprocesos (thread-safe)
         * mediante un bloque `synchronized`.
         *
         * @param context El contexto de la aplicación.
         * @return La instancia singleton de [ParkingDatabase].
         */
        fun getDatabase(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ParkingDatabase::class.java,
                    "parking_smart_database" // Nombre del archivo físico de la base de datos
                )
                    // Si se actualiza el esquema, las migraciones destructivas recrearán la base de datos.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
