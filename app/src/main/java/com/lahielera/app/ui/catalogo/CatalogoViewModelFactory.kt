package com.lahielera.app.ui.catalogo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahielera.app.database.ProductoDatabaseDAO
import java.lang.IllegalArgumentException

class CatalogoViewModelFactory(private val dataSource: ProductoDatabaseDAO,
                               private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogoViewModel::class.java)) {
            return CatalogoViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknowm ViewModel class")
    }
}