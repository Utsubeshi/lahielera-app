package com.lahielera.app.ui.splash

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.lahielera.app.LoginActivity
import com.lahielera.app.MainActivity
import com.lahielera.app.R
import com.lahielera.app.databinding.DetalleProductoFragmentBinding
import com.lahielera.app.databinding.SplashFragmentBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(), CoroutineScope {

    private lateinit var viewModel: SplashViewModel
    val picasso = Picasso.get()
    private lateinit var binding: SplashFragmentBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.splash_fragment,
            container,
            false
        )
        picasso.load("https://i.imgur.com/X7I9VZU.png")
            .into(binding.logoImagen)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hasSession()
    }

    private fun hasSession() {
        launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                viewModel.hasSesion.observe(viewLifecycleOwner, Observer { hasSession ->
                    if (hasSession) {
                        (activity as LoginActivity).moveToMain()
                        //moveToLogin()
                    } else {
                        moveToLogin()
                    }
                })
            }
        }
    }

    private fun moveToLogin(){
        findNavController().navigate(SplashFragmentDirections.actionNavSplashToNavLogin())
    }
}