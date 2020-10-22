package com.lahielera.app.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashViewModel : ViewModel() {

    val firebase = Firebase
    private var auth: FirebaseAuth = Firebase.auth

    private val _hasSession = MutableLiveData<Boolean>(false)
    val hasSesion: LiveData<Boolean>
        get() = _hasSession

    init {
        validarUsuario()
    }

    fun validarUsuario() {
        val user = auth.currentUser
        if ( user != null) {
            _hasSession.value = true
        }
    }

}