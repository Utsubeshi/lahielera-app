package com.lahielera.app.ui.detalleproducto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lahielera.app.model.Producto

class DetalleProductoViewModel (producto : Producto) : ViewModel() {
    private val _producto = MutableLiveData<Producto>(producto)
    val  producto : LiveData<Producto>
        get() = _producto

    private val _cantidad = MutableLiveData<Int>()
    val cantidad: LiveData<Int>
        get() = _cantidad

    init {
        _cantidad.value = 1
    }

    fun aumentarCantidad() {
        _cantidad.value = (cantidad.value)?.plus(1)
    }

    fun reducirCantidad() {
        _cantidad.value = (cantidad.value)?.minus(1)
    }
}