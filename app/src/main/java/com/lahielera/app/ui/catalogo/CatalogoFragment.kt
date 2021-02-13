package com.lahielera.app.ui.catalogo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.lahielera.app.model.Producto
import java.util.*

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
        val viewModelFactory = CatalogoViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CatalogoViewModel::class.java)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductos()
        getCategorias()
        setHasOptionsMenu(true)
        isEmptySearch()
        isLoading()
    }

    private fun getProductos() {
        viewModel.productList.observe(viewLifecycleOwner, Observer { lista ->
            //if (lista != null && lista.size > 0) {
            adapter.addData(lista, this)
            rvCatalogo.invalidate()
            // }
        })
    }

    private fun isEmptySearch() {
        viewModel.isEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            binding.catalogoVacio.isVisible = isEmpty
            binding.catalogoRv.isVisible = !isEmpty
        })
    }

    private fun isLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })
    }

    private fun getCategorias() {
        viewModel.categorias.observe(viewLifecycleOwner, Observer { lista ->
            if (lista != null && lista.size > 0) {
                adapterCategorias.addData(lista, this)
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
        rvCategorias.layoutManager = LinearLayoutManager(this.context,
                LinearLayoutManager.HORIZONTAL,
                false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_seach)
        val searchView: SearchView = searchItem.actionView as SearchView

        //cerrar el teclado al presionar la X en el searchview
        val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
        closeButton.setOnClickListener {
            searchItem.collapseActionView()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //Log.i("Change","Llego al querytextchange")
                val query = newText.toLowerCase(Locale.getDefault())
                val productos = viewModel.filtrarProductos(query)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.show()
    }

    override fun getCategoria(categoria: String) {
        viewModel.productosPorCategoria(categoria)
    }

    private fun showProgressBar() {
        binding.progressBarCatalogo.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBarCatalogo.isVisible = false
    }

    //Cerrar teclado en fragments o activitys
//    fun Fragment.hideKeyboard() {
//        view?.let { activity?.hideKeyboard(it) }
//    }

//    fun Activity.hideKeyboard() {
//        hideKeyboard(currentFocus ?: View(this))
//    }
//
//    fun Context.hideKeyboard(view: View) {
//        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//    }
}