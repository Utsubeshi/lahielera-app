package com.lahielera.app.ui.catalogo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.app.model.Producto

class CatalogoViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()

    private var _productList = MutableLiveData<ArrayList<Producto>>()
    val productList : LiveData<ArrayList<Producto>>
        get() = _productList

    init {
      getProductosFromFireStore()
    }

    private fun getProductosFromFireStore() {
        var data = arrayListOf<Producto>()
        database.collection("Productos")
            .get()
            .addOnSuccessListener { result ->
                for (document in  result) {
                    val producto = document.toObject(Producto::class.java)
                    data.add(producto)
                }
                _productList.value = data
            }
            .addOnFailureListener { exception ->
                Log.e("CatalogoViewModelError:", "Error getting documents. ", exception)
            }
    }
}