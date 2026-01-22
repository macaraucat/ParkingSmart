package com.example.parkingsmart.model.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UsuarioEntity::class, TicketEntity::class], version = 1, exportSchema = false)
abstract class ParkingDatabase : RoomDatabase() {

    abstract fun dao(): ParkingDao

    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        fun getDatabase(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ParkingDatabase::class.java,
                    "parking_smart_database" // Nombre del archivo físico
                )
                    .fallbackToDestructiveMigration() // Si cambias la BD, borra la anterior para no crashear
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}