package com.lahielera.app.ui.checkout

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.model.Direccion
import com.lahielera.app.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class CheckoutViewModel (
    val db: ProductoDatabaseDAO,
    application: Application) : ViewModel() {

    private val dbFirestore = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val usuariosRef = dbFirestore.collection("Usuarios")
    private val userId: String

    var productos = db.getAllProductos()

    private var _usuario = MutableLiveData<Usuario>()
    val usuario : LiveData<Usuario>
        get() = _usuario

    private var _direccion = MutableLiveData<Direccion>()
    val direccion : LiveData<Direccion>
        get() = _direccion

    private var _hasDireccion = MutableLiveData<Boolean>(false)
    val hasDireccion : LiveData<Boolean>
        get() = _hasDireccion

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        userId = auth.currentUser!!.uid
        getUser()
    }

    fun getUser() {
        uiScope.launch {
            getUserFromFireStore()
        }
    }

    private suspend fun getUserFromFireStore () {
        usuariosRef.document(userId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val usuario = document.toObject(Usuario::class.java)
                _usuario.postValue(usuario)
                val direccion = usuario?.direccion
                if (direccion != null) {
                    if (direccion.esPredeterminada){
                        _direccion.postValue(direccion)
                        _hasDireccion.postValue(true)
                    } else {
                        _hasDireccion.postValue(false)
                    }
                }
            }
        }
    }

//    private fun getDireccion() {
//        uiScope.launch {
//            getDireccionPredeterminada()
//        }
//    }
//
//    private suspend fun getDireccionPredeterminada() {
//        val usuario = _usuario.value
//        val direccion = usuario?.direccion
//        if (direccion != null) {
//                _direccion.postValue(direccion)
//        }
//    }
}