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
import com.google.android.material.snackbar.Snackbar
import com.lahielera.app.R
import com.lahielera.app.database.ProductoDatabase
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
        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        viewModelFactory = DetalleProductoViewModelFactory(productoFragmentArgs.producto, dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetalleProductoViewModel::class.java)
        showProducto()
        binding.cantidadAumentar.setOnClickListener{
            viewModel.aumentarCantidad()
        }

        binding.cantidadReducir.setOnClickListener{
            if (viewModel.cantidad.value!! > 1) {
                viewModel.reducirCantidad()
            }
        }

        viewModel.cantidad.observe(viewLifecycleOwner, Observer { cantidad ->
            binding.detalleCantidad.text = cantidad.toString()
        })

        binding.detalleAddToCart.setOnClickListener {
            addToCart()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun showProducto() {
        viewModel.producto.observe(viewLifecycleOwner, Observer { producto ->
            if (producto != null) {
                with(binding) {
                    picasso.load(producto.urlImg)
                            .into(detalleImagen)
                    detalleMarca.text = producto.marca
                    detalleDescripcion.text = producto.descripcion
                    detalleNombre.text = producto.nombre
                    val precio = "S/ ${producto.precio}"
                    detallePrecio.text = precio
                }
            }
        })
    }

    private fun addToCart(){
        viewModel.agregarAlCarrito()
        viewModel.producto.observe(viewLifecycleOwner, Observer {producto ->
            view?.let {
                Snackbar.make(it, "Producto agregado: ${producto.marca} ${producto.nombre}", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        })
    }
}