package com.lahielera.app.ui.direcciones.direccion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Direccion

class DireccionViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private var userId: String
    private val direccionesRef: CollectionReference

    private var _onSaveSuccess = MutableLiveData<Boolean>()
    val onSaveSuccess : LiveData<Boolean>
        get() = _onSaveSuccess

    init {
        userId = auth.currentUser!!.uid
        direccionesRef = db.collection("Usuarios").document(userId).collection("Direcciones")
    }


    fun saveDirecion(direccion: Direccion) {
        direccionesRef.add(direccion).addOnCompleteListener { task ->
            if (task.isSuccessful) _onSaveSuccess.value = true
        }
        _onSaveSuccess.value = false
    }
}