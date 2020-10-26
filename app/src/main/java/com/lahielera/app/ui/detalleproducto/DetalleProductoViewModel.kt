package com.lahielera.app.ui.detalleproducto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Producto
import kotlinx.coroutines.*

class DetalleProductoViewModel (
        producto : Producto,
        val db: ProductoDatabaseDAO,
        application: Application) : AndroidViewModel(application) {
    private val _producto = MutableLiveData<Producto>(producto)
    val  producto : LiveData<Producto>
        get() = _producto

    private val _cantidad = MutableLiveData<Int>()
    val cantidad: LiveData<Int>
        get() = _cantidad

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        _cantidad.value = 1
    }

    fun aumentarCantidad() {
        _cantidad.value = (cantidad.value)?.plus(1)
    }

    fun reducirCantidad() {
        _cantidad.value = (cantidad.value)?.minus(1)
    }

    fun agregarAlCarrito() {
        var producto: Producto? = _producto.value
        uiScope.launch {
            val getProdutoTask = async { get(producto?.uid.toString()) }
            val _p: Producto? = getProdutoTask.await()
            if (_p == null) {
                if (producto != null) {
                    producto.cantidad = _cantidad.value!!
                    insert(producto)
                }
            } else {
                _p.cantidad = _p.cantidad.plus(_cantidad.value!!)
                update(_p)
            }
        }
    }

    private  suspend fun get(uid: String): Producto? {
        return  db.get(uid)
    }

    private suspend fun insert(producto: Producto) {
        db.insert(producto)
    }

    private suspend fun update(producto: Producto) {
        db.update(producto)
    }
}