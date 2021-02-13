package com.lahielera.app.ui.carrito

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Producto
import kotlinx.coroutines.*

class CarritoViewModel (
    val db: ProductoDatabaseDAO,
    aplication: Application) : AndroidViewModel(aplication) {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    var productos = db.getAllProductos()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //limpiar carrito
    fun onClear() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            db.clear()
        }
    }

    fun eliminarDelCarrito(uid: String) {
        uiScope.launch {
            db.deleteById(uid)
        }
    }

    fun update(producto: Producto) {
        uiScope.launch {
            db.update(producto)
        }
    }
}

