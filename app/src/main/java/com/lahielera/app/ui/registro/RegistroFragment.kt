package com.lahielera.app.ui.registro

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.app.LoginActivity
import com.lahielera.app.MainActivity
import com.lahielera.app.R
import com.lahielera.app.databinding.RegistroFragmentBinding
import java.util.regex.Pattern


class RegistroFragment : Fragment() {

    private lateinit var viewModel: RegistroViewModel
    private lateinit var binding: RegistroFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.registro_fragment,
                container,
                false
        )
        binding.botonRegistro.setOnClickListener {
           createAccount(binding.mailRegistro.editText?.text.toString(),
                   binding.passRegistro.editText?.text.toString()
           )
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistroViewModel::class.java)
        auth = Firebase.auth
    }

    private fun validarForm(): Boolean{
        var valido = true
        val email = binding.mailRegistro.editText?.text.toString()
        if (TextUtils.isEmpty(email) || !validarEmail(email)) {
            binding.mailRegistro.error = "Ingrese un correo válido"
            binding.mailRegistro.hasFocus()
            valido = false
        } else {
            binding.mailRegistro.error = null
        }

        val password = binding.passRegistro.editText?.text.toString()
        val password2 = binding.pass2Registro.editText?.text.toString()
        //Toast.makeText(requireContext(), "${password} ${password2}", Toast.LENGTH_SHORT).show()

        if (!validarPassword(password, password2)) {
            valido = false
        } else {
            binding.passRegistro.error = null
            binding.pass2Registro.error = null
        }
        return valido
    }

    private fun validarEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun validarPassword( password: String, password2: String): Boolean {
        if (password.isEmpty()) {
            binding.passRegistro.error = "Campo requerido"
            return false
        }
       if (!Pattern.matches(".{8,15}", password)) {
            binding.passRegistro.error = "Mínimo: 8 caracteres, máximo 15"
            return false
        }
        if (!Pattern.matches(".*[A-Z].*", password)) {
            binding.passRegistro.error = "Mínimo una mayúscula"
            return false
        }
        if (Pattern.matches(".*\\s.*", password)) {
            binding.passRegistro.error = "No se permiten espacios"
            return false
        }
        if (!Pattern.matches(".*[a-z].*", password)) {
            binding.passRegistro.error = "Mínimo una minúscula "
            return false
        }
        if (!Pattern.matches(".*\\d.*", password)) {
            binding.passRegistro.error = "Mínimo 1 número"
            return false
        }
        if (!password.equals(password2)){
            binding.passRegistro.error = "La contraseña no coincide"
            binding.pass2Registro.error = "La contraseña no coincide"
            return false
        }
        return true
    }

    private fun createAccount(email: String, password: String) {
        if (!validarForm()) {
            return
        }
        //TODO showProgresBar()
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener( OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario registrado", Toast.LENGTH_SHORT).show()
                        (activity as LoginActivity).moveToMain()
                } else {
                        Toast.makeText(requireContext(), "Hubo un error, intentelo nuevamente", Toast.LENGTH_SHORT).show()
                    }
                    //TODO hideProgressBar()
                })
    }
}