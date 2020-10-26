package com.lahielera.app.ui.detalleproducto

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Producto
import java.lang.IllegalArgumentException

class DetalleProductoViewModelFactory (
        private val producto: Producto,
        private val dataSource: ProductoDatabaseDAO,
        private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)) {
            return DetalleProductoViewModel(producto, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}