package com.lahielera.app.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lahielera.app.model.Producto

@Dao
interface ProductoDatabaseDAO {

    @Insert
    suspend fun insert(producto: Producto)

    @Update
    suspend fun update(producto: Producto)

    @Query("SELECT * from producto WHERE idFirebase = :id")
    suspend fun get(id: String): Producto?

    @Query("DELETE FROM producto")
    fun clear()

    @Query("SELECT * from producto")
    fun getAllProductos(): LiveData<List<Producto>>

    @Query("delete from producto where idFirebase = :idFirebase")
    fun deleteById(idFirebase: String)

   // @Query("SELECT * from producto ORDER BY idProducto LIMIT 1")
    //fun get(): Producto?
}