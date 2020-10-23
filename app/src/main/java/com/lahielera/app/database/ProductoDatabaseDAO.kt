package com.lahielera.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lahielera.app.model.Producto

@Dao
interface ProductoDatabaseDAO {

    @Insert
    fun insert(producto: Producto)

    @Update
    fun update(producto: Producto)

    @Query("SELECT * from producto WHERE idFirebase = :id")
    fun get(id: String): Producto?

    @Query("DELETE FROM producto")
    fun clear()

    @Query("SELECT * from producto")
    fun getAllProductos(): Array<Producto>

   // @Query("SELECT * from producto ORDER BY idProducto LIMIT 1")
    //fun get(): Producto?
}