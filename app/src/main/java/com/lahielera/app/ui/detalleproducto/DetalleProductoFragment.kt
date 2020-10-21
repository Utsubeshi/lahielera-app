package com.lahielera.app.ui.detalleproducto

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.lahielera.app.R
import com.lahielera.app.databinding.DetalleProductoFragmentBinding
import com.squareup.picasso.Picasso

class DetalleProductoFragment : Fragment() {

    private lateinit var binding: DetalleProductoFragmentBinding
    private lateinit var viewModel: DetalleProductoViewModel
    private lateinit var viewModelFactory: DetalleProductoViewModelFactory
    private val picasso = Picasso.get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.detalle_producto_fragment,
                container,
                false)
        val productoFragmentArgs by navArgs<DetalleProductoFragmentArgs>()
        viewModelFactory = DetalleProductoViewModelFactory(productoFragmentArgs.producto)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetalleProductoViewModel::class.java)
        showProducto()
    }

    private fun showProducto() {
        viewModel.producto.observe(viewLifecycleOwner, Observer { producto ->
            with(binding) {
                picasso.load(producto.urlImg)
                        .into(detalleImagen)
                detalleMarca.text = producto.marca
                detalleDescripcion.text = producto.descripcion
                detalleNombre.text = producto.nombre
                val precio = "S/ ${producto.precio}"
                detallePrecio.text = precio
            }
        })
    }
}