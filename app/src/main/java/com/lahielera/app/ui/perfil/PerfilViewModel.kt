package com.lahielera.app.ui.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Producto
import com.lahielera.app.model.Usuario

class PerfilViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()
    private val auth = Firebase.auth
    private val usuariosRef = database.collection("Usuarios")
    private var userId: String

    private var _onSaveSucess = MutableLiveData<Boolean>()
    val onSaveSucess : LiveData<Boolean>
        get() = _onSaveSucess

    private var _usuario = MutableLiveData<Usuario>()
    val usuario : LiveData<Usuario>
        get() = _usuario

    init {
        userId = auth.currentUser!!.uid
        getUserFromFireStore()
    }

    fun saveUserOnFireStore (usuario: Usuario) {
        usuariosRef.document(userId).set(usuario).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _onSaveSucess.value = true
            }
        }
        _onSaveSucess.value = false
    }

    fun getUserFromFireStore () {
        usuariosRef.document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                val usuario = document.toObject(Usuario::class.java)!!
                _usuario.value = usuario
            }
        }
    }
}