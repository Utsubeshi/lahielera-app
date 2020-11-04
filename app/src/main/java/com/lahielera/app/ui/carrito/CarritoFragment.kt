package com.lahielera.app.ui.carrito

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lahielera.app.R
import com.lahielera.app.database.ProductoDatabase
import com.lahielera.app.databinding.CarritoFragmentBinding
import com.lahielera.app.model.Producto

class CarritoFragment : Fragment(), CarritoAdapter.OnCarritoItemClickListener {

    private lateinit var rvCarrito: RecyclerView
    private lateinit var adapter: CarritoAdapter
    private lateinit var viewModel: CarritoViewModel
    private lateinit var binding: CarritoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.carrito_fragment, container,false)
        loadRecyclerView()
        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        val viewModelFactory = CarritoViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CarritoViewModel::class.java)
        binding.lifecycleOwner = this
        viewModel.productos.observe(viewLifecycleOwner, Observer { productos ->
            if (!productos.isNullOrEmpty()) {
                adapter.addData(productos, this)
                binding.totalCarrito.text = getString(R.string.formato_precio, getTotal(productos) )
            } else {
                rvCarrito.isVisible = false
                binding.totalCarrito.text = getString(R.string.carrito_vacio)
            }
        })
        binding.botonCheckout.setOnClickListener {
            findNavController().navigate(CarritoFragmentDirections.actionNavCarritoToCheckoutFragment())
        }
        return binding.root
    }

    private fun loadRecyclerView() {
        rvCarrito = binding.rvCarrito
        rvCarrito.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = CarritoAdapter()
        rvCarrito.adapter = adapter
    }

    private fun getTotal(productos: List<Producto>): Double {
        var total = 0.0
        for (producto in productos) {
            total = total.plus(producto.precio.times(producto.cantidad))
        }
        return total
    }

    override fun reducirCantidad(producto: Producto) {
        viewModel.update(producto)
    }

    override fun aumentarCantidad(producto: Producto) {
        viewModel.update(producto)
    }


    override fun eliminarDelCarrito(uid: String) {
        viewModel.eliminarDelCarrito(uid)
        view?.let {
            Snackbar.make(it, "Producto eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}