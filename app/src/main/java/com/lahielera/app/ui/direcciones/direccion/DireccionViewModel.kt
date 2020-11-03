package com.lahielera.app.ui.direcciones.direccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Direccion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DireccionViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private var userId: String
    private val direccionesRef: CollectionReference

    private var _onSaveSuccess = MutableLiveData<Boolean>()
    val onSaveSuccess : LiveData<Boolean>
        get() = _onSaveSuccess
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        userId = auth.currentUser!!.uid
        direccionesRef = db.collection("Usuarios").document(userId).collection("Direcciones")
    }


    fun saveDirecion(direccion: Direccion) {
        uiScope.launch {
            saveDireccionOnFirestore(direccion)
        }
    }

    private suspend fun saveDireccionOnFirestore(direccion: Direccion) {
        direccionesRef.add(direccion).addOnCompleteListener { task ->
            if (task.isSuccessful) _onSaveSuccess.postValue(true)
        }
        _onSaveSuccess.postValue(false)
    }

    fun updateDireccion(direccion: Direccion) {
        direccionesRef.document(direccion.id).set(direccion).addOnCompleteListener { task ->
            if (task.isSuccessful) _onSaveSuccess.postValue(true)
        }
        _onSaveSuccess.postValue(false)
    }
}

