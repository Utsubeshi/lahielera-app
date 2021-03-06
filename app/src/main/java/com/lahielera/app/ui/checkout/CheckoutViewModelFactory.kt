package com.lahielera.app.ui.checkout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.ui.carrito.CarritoViewModel
import java.lang.IllegalArgumentException

class CheckoutViewModelFactory(
    private val dataSource: ProductoDatabaseDAO,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknowm ViewModel class")
    }
}