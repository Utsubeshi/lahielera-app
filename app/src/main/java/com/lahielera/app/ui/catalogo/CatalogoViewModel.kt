package com.lahielera.app.ui.catalogo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Categoria
import com.lahielera.app.model.Producto
import kotlinx.coroutines.*

class CatalogoViewModel(

        val db: ProductoDatabaseDAO,
        aplication: Application) : AndroidViewModel(aplication) {

    private val database = FirebaseFirestore.getInstance()

    private var _productList = MutableLiveData<ArrayList<Producto>>()
    val productList : LiveData<ArrayList<Producto>>
        get() = _productList

    private var _categorias = MutableLiveData<ArrayList<Categoria>>()
    val categorias : LiveData<ArrayList<Categoria>>
        get() = _categorias

    //Agregar al carro - coroutine
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
      getProductosFromFireStore()
      getCategiorias()
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

    private fun getCategiorias() {
        uiScope.launch {
            getCategioriasFromFireStore()
        }
    }

    private suspend fun getCategioriasFromFireStore() {
        database.collection("Categorias").document("VnIRHpSxr4H8ZezsYG19")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null){
                            var categoriasList: ArrayList<Categoria> = arrayListOf()
                            val categorias: ArrayList<HashMap<String, String>> = document.get("Categorias") as ArrayList<HashMap<String, String>>
                               for (cat in categorias) {
                                   var categoria = Categoria()
                                   categoria.nombre = cat.get("nombre").toString()
                                   categoria.imgUrl = cat.get("imgUrl").toString()
                                   categoriasList.add(categoria)
                               }
                            _categorias.value = categoriasList
                        }
                    }
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

    data class CategoriasDoc(
            var categorias: ArrayList<Categoria>
    ) { }
}