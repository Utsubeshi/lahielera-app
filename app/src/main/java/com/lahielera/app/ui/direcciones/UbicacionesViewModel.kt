package com.lahielera.app.ui.direcciones

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Direccion
import com.lahielera.app.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UbicacionesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private var userId: String
    private val direccionesRef: CollectionReference

    private var _onSaveSuccess = MutableLiveData<Boolean>()
    val onSaveSuccess : LiveData<Boolean>
        get() = _onSaveSuccess

    private var _direcionesList = MutableLiveData<ArrayList<Direccion>>()
    val direcionesList : LiveData<ArrayList<Direccion>>
        get() = _direcionesList

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        userId = auth.currentUser!!.uid
        direccionesRef = db.collection("Usuarios").document(userId).collection("Direcciones")
        getDirecciones()
    }

    fun getDirecciones() {
        uiScope.launch {
            getDireccionesFromFirestore()
        }
    }

    private suspend fun getDireccionesFromFirestore() {
        _isLoading.postValue(true)
        var data = arrayListOf<Direccion>()
        direccionesRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val direccion = document.toObject(Direccion::class.java)
                data.add(direccion)
            }
            _direcionesList.postValue(data)
            _isLoading.postValue(false)
        }.addOnFailureListener { exception ->
            Log.e("DireccionesVMError:", "Error getting documents. ", exception)
        }
    }

}