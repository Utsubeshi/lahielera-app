package com.lahielera.app.ui.carrito

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.lahielera.app.R
import com.lahielera.app.database.ProductoDatabase
import com.lahielera.app.databinding.CarritoFragmentBinding

class CarritoFragment : Fragment() {

    private lateinit var viewModel: CarritoViewModel
    private lateinit var binding: CarritoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.carrito_fragment, container,false)

        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        val viewModelFactory = CarritoViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(CarritoViewModel::class.java)
        binding.setLifecycleOwner(this)

        binding.carritoViewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(CarritoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}