package com.lahielera.app.ui.detalleproducto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lahielera.app.model.Producto

class DetalleProductoViewModel (producto : Producto) : ViewModel() {
    private val _producto = MutableLiveData<Producto>(producto)
    val  producto : LiveData<Producto>
        get() = _producto
}