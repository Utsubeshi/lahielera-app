package com.lahielera.app.ui.catalogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.lahielera.app.R
import com.lahielera.app.database.ProductoDatabase
import com.lahielera.app.databinding.FragmentCatalogoBinding
import com.lahielera.app.model.Categoria
import com.lahielera.app.model.Producto

class CatalogoFragment : Fragment(), CatalogoAdapter.OnProductosClickListener, CategoriasAdapter.OnCategoriaClickListener {

    private lateinit var rvCatalogo: RecyclerView
    private lateinit var rvCategorias: RecyclerView
    private lateinit var viewModel: CatalogoViewModel
    private lateinit var binding: FragmentCatalogoBinding
    private lateinit var adapter: CatalogoAdapter
    private lateinit var adapterCategorias: CategoriasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_catalogo,
            container,
            false)
        loadRecyclerView()
        loadCartegoriasRV()
        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        val viewModelFactory = CatalogoViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CatalogoViewModel::class.java)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductos()
        getCategorias()
    }

    private fun getProductos() {
        viewModel.productList.observe(viewLifecycleOwner, Observer { lista ->
            if (lista != null && lista.size > 0) {
                adapter.addData(lista, this)
            }
        })
    }

    private fun getCategorias() {
        viewModel.categorias.observe(viewLifecycleOwner, Observer { lista ->
            if (lista != null && lista.size > 0 ){
                adapterCategorias.addData(lista)
            }
        })
    }

    private fun loadRecyclerView() {
        rvCatalogo = binding.catalogoRv
        rvCatalogo.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        adapter = CatalogoAdapter()
        rvCatalogo.adapter = adapter
    }

    private fun loadCartegoriasRV() {
        rvCategorias = binding.categoriasRv
        rvCategorias.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        adapterCategorias = CategoriasAdapter()
        rvCategorias.adapter = adapterCategorias
    }

    override fun onProductoClick(producto: Producto) {
        findNavController().navigate(CatalogoFragmentDirections.actionNavCatalogoToDetalleProductoFragment(producto))
    }

    override fun onAddToCart(producto: Producto) {
        viewModel.agregarAlCarro(producto)
        view?.let {
            Snackbar.make(it, "Producto agregado: ${producto.marca} ${producto.nombre}", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.hide()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.show()
    }

    override fun getCategoria(categoria: String) {
        TODO("Not yet implemented")
    }
}