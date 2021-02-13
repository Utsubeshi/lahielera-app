package com.lahielera.app.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.app.LoginActivity
import com.lahielera.app.MainActivity
import com.lahielera.app.R
import com.lahielera.app.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        binding.googleLogin.setOnClickListener {
            signIn()
        }

        binding.registrarse.setOnClickListener{
            irAlRegistro()
        }

        binding.botonLogin.setOnClickListener {
            signIn(binding.mailLogin.editText?.text.toString(),
                    binding.passLogin.editText?.text.toString())
        }
        return binding.root
    }

    private fun irAlRegistro() {
       findNavController().navigate(LoginFragmentDirections.actionNavLoginToNavRegistro())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity().applicationContext, gso)
        auth = Firebase.auth
    }

    // GOOGLE SIGN IN
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                //TODO sera necesario?
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //val account = task.getResult(ApiException::class.java)!!
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuth: " + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Hubo un error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                Log.d(TAG, "fallo la wea")
            }
        })
    }

    //Ingreso con mail y pass
    private fun signIn(email: String, password: String) {
        if (!validarForm()) {
            return
        }
        //TODO showProgressBar
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Bienvenido!", Toast.LENGTH_SHORT).show()
                        (activity as LoginActivity).moveToMain()
                    } else {
                        Toast.makeText(requireContext(), "Correo o password no validos", Toast.LENGTH_SHORT).show()
                    }
                    //TODO hideProgressBar
                })
    }

    private fun validarForm(): Boolean{
        var valido = true
        val email = binding.mailLogin.editText?.text.toString()
        if (TextUtils.isEmpty(email) || !validarEmail(email)) {
            binding.mailLogin.error = "Ingrese un correo electronico"
            valido = false
        } else {
            binding.mailLogin.error = null
        }

        val password = binding.passLogin.editText?.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passLogin.error = "Ingrese una contraseña"
            binding.passLogin.hasFocus()
            valido = false
        } else {
            binding.passLogin.error = null
        }
        return valido
    }

    private fun validarEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}