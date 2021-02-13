package com.lahielera.app.ui.payment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lahielera.app.database.ProductoDatabaseDAO
import java.lang.IllegalArgumentException

class PaymentViewModelFactory (private val dataSource: ProductoDatabaseDAO,
                               private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknowm ViewModel class")
    }
}