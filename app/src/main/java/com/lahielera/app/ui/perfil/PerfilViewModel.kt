package com.lahielera.app.ui.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.model.Producto
import com.lahielera.app.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible : LiveData<Boolean>
        get() = _isVisible

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        userId = auth.currentUser!!.uid
        getUser()
        _onSaveSucess.value = false
    }

    fun saveUser(usuario: Usuario) {
        uiScope.launch {
            saveUserOnFireStore(usuario)
        }
    }

    private suspend fun saveUserOnFireStore (usuario: Usuario) {
        usuariosRef.document(userId).set(usuario).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _onSaveSucess.postValue(true)
            }
        }
        //_onSaveSucess.postValue(false)
        _isVisible.postValue(true)
    }

    private fun getUser() {
        uiScope.launch {
            getUserFromFireStore()
        }
    }

    private suspend fun getUserFromFireStore () {
        usuariosRef.document(userId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val usuario = document.toObject(Usuario::class.java)
                _usuario.postValue(usuario)
            } else {
                _isVisible.postValue(false)
            }
        }
    }

    fun disableToast() {
        _onSaveSucess.value = false
    }

}