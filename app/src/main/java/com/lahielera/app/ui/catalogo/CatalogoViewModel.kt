package com.lahielera.app.ui.catalogo

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Producto
import kotlinx.coroutines.*

class CatalogoViewModel(

    val db: ProductoDatabaseDAO,
    aplication: Application) : AndroidViewModel(aplication) {

    private val database = FirebaseFirestore.getInstance()

    private var _productList = MutableLiveData<ArrayList<Producto>>()
    val productList : LiveData<ArrayList<Producto>>
        get() = _productList

    //Agregar al carro - coroutine
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun agregarAlCarro(producto: Producto) {
        uiScope.launch {
            val getProdutoTask = async { get(producto.uid.toString()) }
            val _p: Producto? = getProdutoTask.await()
            if (_p == null) {
                insert(producto)
            } else {
                _p.cantidad = _p.cantidad.plus(1)
                update(_p)
            }
        }
    }

    private suspend fun insert(producto: Producto) {
        db.insert(producto)
    }

    private  suspend fun get(uid: String): Producto? {
        return  db.get(uid)
    }

    private suspend fun update(producto: Producto) {
        db.update(producto)
    }
}