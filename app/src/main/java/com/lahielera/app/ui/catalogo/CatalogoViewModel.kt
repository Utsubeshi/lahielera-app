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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CatalogoViewModel(

        val db: ProductoDatabaseDAO,
        aplication: Application) : AndroidViewModel(aplication) {

    private val database = FirebaseFirestore.getInstance()

    private var _productList = MutableLiveData<ArrayList<Producto>>()
    val productList : LiveData<ArrayList<Producto>>
        get() = _productList

    private var listaSinFiltrar = MutableLiveData<ArrayList<Producto>>()

    private var _categorias = MutableLiveData<ArrayList<Categoria>>()
    val categorias : LiveData<ArrayList<Categoria>>
        get() = _categorias

    //Agregar al carro - coroutine
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    //var _isEmpty = MutableLiveData<Boolean>()

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty : LiveData<Boolean>
        get() = _isEmpty

    init {
      getProductosFromFireStore()
      getCategorias()
        _isEmpty.value = false
    }


    private fun getProductosFromFireStore() {
        _isLoading.value = true
        var data = arrayListOf<Producto>()
        database.collection(PRODUCTOS)
            .get()
            .addOnSuccessListener { result ->
                for (document in  result) {
                    val producto = document.toObject(Producto::class.java)
                    data.add(producto)
                }
                _productList.value = data
                listaSinFiltrar.value = data
                _isLoading.value = false
            }
            .addOnFailureListener { exception ->
                Log.e("CatalogoViewModelError:", "Error getting documents. ", exception)
            }
    }

    private fun getCategorias() {
        uiScope.launch {
            getCategoriasFromFireStore()
        }
    }

    private suspend fun getCategoriasFromFireStore() {
        database.collection("Categorias").document(CATEGORIAS_DOC)
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

    fun productosPorCategoria(categoria: String) {
        uiScope.launch {
            if (categoria == "Todo") {
                _productList.postValue(listaSinFiltrar.value)
            } else {
                getProductosByCategoria(categoria)
            }
        }
    }

    private suspend fun getProductosByCategoria(categoria: String) {
        _isLoading.postValue(true)
        var data = arrayListOf<Producto>()
        database.collection(PRODUCTOS).whereEqualTo("categoria", categoria)
                .get()
                .addOnSuccessListener { result ->
                    for (document in  result) {
                        val producto = document.toObject(Producto::class.java)
                        data.add(producto)
                    }
                    _productList.postValue(data)
                    _isLoading.postValue(false)
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

    data class CategoriasDoc(
            var categorias: ArrayList<Categoria>
    )

    companion object {
        private const val CATEGORIAS_DOC = "tgunBfhwCxheO8yTSRWs"
        private const val PRODUCTOS = "Productos"
    }

    fun filtrarProductos(newText: String) {
        val productos = listaSinFiltrar.value
        val listaFiltrada = arrayListOf<Producto>()
        if (productos != null) {
            for (producto in productos ) {
                val nombre = producto.nombre?.toLowerCase(Locale.getDefault())
                val marca = producto.marca?.toLowerCase(Locale.getDefault())
                if (nombre!!.contains(newText) ||  marca!!.contains(newText)){
                    listaFiltrada.add(producto)
                }
            }
        }
        _isEmpty.value = listaFiltrada.isEmpty()
        _productList.value = listaFiltrada
    }
}