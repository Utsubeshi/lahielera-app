package com.lahielera.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lahielera.app.model.Producto

@Database(entities = [Producto::class], version = 3, exportSchema = false)
abstract class ProductoDatabase: RoomDatabase() {

   abstract val productoDatabaseDAO: ProductoDatabaseDAO
    companion object {
        @Volatile
        private var INSTANCE: ProductoDatabase? = null

        fun getInstance(context: Context): ProductoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductoDatabase::class.java,
                        "carrito_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}