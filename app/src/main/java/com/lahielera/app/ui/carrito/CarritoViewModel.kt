package com.lahielera.app.ui.carrito

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Producto
import kotlinx.coroutines.*

class CarritoViewModel (
    val database: ProductoDatabaseDAO,
    aplication: Application) : AndroidViewModel(aplication) {

    private var viewModelJob = Job()


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var producto = MutableLiveData<Producto?>()

    //TODO debe ser mutable
    private lateinit var productos: Array<Producto>  //database.getAllProductos()

    init {
        initializeProductos()
    }

    private fun initializeProductos() {
        uiScope.launch {
            productos = getProductosFromDatabase()
        }
    }

    private suspend fun getProductosFromDatabase(): Array<Producto> {
        return withContext(Dispatchers.IO) {
            val producto = database.getAllProductos()
            producto
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        uiScope.launch {
            insert(producto)
        }
    }

    private suspend fun insert(producto: Producto) {
        withContext(Dispatchers.IO) {
            database.insert(producto)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
         }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
}