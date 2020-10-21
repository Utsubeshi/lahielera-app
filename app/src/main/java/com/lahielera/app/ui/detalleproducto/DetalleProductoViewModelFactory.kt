package com.lahielera.app.ui.detalleproducto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahielera.app.model.Producto
import java.lang.IllegalArgumentException

class DetalleProductoViewModelFactory (private val producto: Producto) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)) {
            return DetalleProductoViewModel(producto) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}