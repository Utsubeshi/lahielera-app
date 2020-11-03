package com.lahielera.app.ui.direcciones

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Direccion
import com.lahielera.app.model.Producto
import com.lahielera.app.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UbicacionesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private var userId: String
    private val direccionesRef: CollectionReference
    private val usuariosReference: CollectionReference

    private var _onSaveSuccess = MutableLiveData<Boolean>()
    val onSaveSuccess : LiveData<Boolean>
        get() = _onSaveSuccess

    private var _direcionesList = MutableLiveData<ArrayList<Direccion>>()
    val direcionesList : LiveData<ArrayList<Direccion>>
        get() = _direcionesList

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private var direccion = Direccion()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var data = arrayListOf<Direccion>()

    init {
        userId = auth.currentUser!!.uid
        direccionesRef = db.collection("Usuarios").document(userId).collection("Direcciones")
        usuariosReference = db.collection("Usuarios")
        getDirecciones()
    }

    fun getDirecciones() {
        uiScope.launch {
            getDireccionesFromFirestore()
        }
    }

    private suspend fun getDireccionesFromFirestore() {
        _isLoading.postValue(true)
        direccionesRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val direccion = document.toObject(Direccion::class.java)
                data.add(direccion)
                _isLoading.postValue(false)
            }
            getDefault()
        }.addOnFailureListener { exception ->
            Log.e("DireccionesVMError:", "Error getting documents. ", exception)
        }

    }

    fun setPredeterminada(direccion: Direccion) {
        uiScope.launch {
            setDireccionPredeterminada(direccion)
        }
    }

    private suspend fun setDireccionPredeterminada(direccion: Direccion) {
        direccion.esPredeterminada = true
        usuariosReference.document(userId).update("direccion", direccion)
        if (data.size > 1) {
            for (d in data) {
                if (direccion.calle.equals(d.calle) && direccion.numero.equals(d.numero)) {
                    data.remove(d)
                    break
                }
            }
            data[0].esPredeterminada = false
            data.add(0, direccion)
        } else {
            data[0] = direccion
        }
        _direcionesList.postValue(data)
    }

    private fun getDefault() {
        uiScope.launch {
            getDireccionPredeterminada()
        }
    }

    private suspend fun getDireccionPredeterminada() {
        usuariosReference.document(userId).get().addOnSuccessListener { document ->
            if (document.exists()){
                val usuario = document.toObject(Usuario::class.java)
                direccion = usuario?.direccion!!
                if (direccion.calle.isNotEmpty()) {
                    var dir = Direccion()
                        for (d in data) {
                        if (direccion.calle.equals(d.calle) && direccion.numero.equals(d.numero)) {
                            dir = d
                            data.remove(d)
                            break
                        }
                    }
                    dir.esPredeterminada = true
                    if (dir.calle.isNotEmpty()) {
                        data.add(0, dir)
                    }
                }
            }
            _direcionesList.postValue(data)
        }
    }

    fun eliminar(id: String) {
        uiScope.launch {
            eliminarFromFireStore(id)
        }
    }

    private suspend fun eliminarFromFireStore(id: String) {
        if (data.size == 1) {
            val update = hashMapOf<String, Any>(
                    "direccion" to FieldValue.delete()
            )
            usuariosReference.document(userId).update(update).onSuccessTask {
                direccionesRef.limit(1).get().addOnSuccessListener { result ->
                    for (document in result) {
                        val idDireccion = document.id
                        direccionesRef.document(id).delete()
                    }
                }
            }
            data.clear()
            _direcionesList.postValue(data)
            return
        }
        direccionesRef.document(id).delete().addOnSuccessListener {
            for (d in data) {
                if (d.id.equals(id)) {
                    data.remove(d)
                    break
                }
            }
            _direcionesList.postValue(data)
        }
    }
}