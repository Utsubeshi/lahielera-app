package com.lahielera.app.ui.direcciones

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.UbicacionesFragmentBinding
import com.lahielera.app.model.Direccion

class UbicacionesFragment : Fragment(), UbicacionesAdapter.OnDireccionClickListener {

    private lateinit var viewModel: UbicacionesViewModel
    private lateinit var binding: UbicacionesFragmentBinding
    private lateinit var rvDireciones: RecyclerView
    private lateinit var adapter: UbicacionesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.ubicaciones_fragment,
                container,
                false
        )
        loadRecyclerView()
        showProgressBar()
        binding.botonAgregarDireccion.setOnClickListener {
            findNavController().navigate(UbicacionesFragmentDirections
                    .actionNavUbicacionesToNavDireccion())
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun loadRecyclerView() {
        rvDireciones = binding.rvDirecciones
        rvDireciones.layoutManager = LinearLayoutManager(
                this.context,
                LinearLayoutManager.VERTICAL,
                false)
        adapter = UbicacionesAdapter()
        rvDireciones.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UbicacionesViewModel::class.java)
        getDirecciones()
    }

    private fun getDirecciones() {
        viewModel.direcionesList.observe(viewLifecycleOwner, Observer { lista ->
            if (lista.isNullOrEmpty()) {
                //onEmptyList(true)
                hideProgressBar()
           } else {
                adapter.addData(lista, this)
                adapter.notifyDataSetChanged()
                //onEmptyList(false)
                hideAll()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getDirecciones()
    }

    override fun setPredeterminada(direccion: Direccion) {
        viewModel.setPredeterminada(direccion)
    }

    override fun eliminarDireccion(id: String) {
        viewModel.eliminar(id)
    }

    override fun editar(direccion: Direccion) {
        findNavController().navigate(UbicacionesFragmentDirections
                .actionNavUbicacionesToNavDireccion(direccion))
    }

    private fun showProgressBar() {
        binding.progressBarPerfil.isVisible = true
        binding.imgSinDireciones.isVisible = false
        binding.textSinDirecciones.isVisible = false
    }

    private fun hideProgressBar() {
        binding.progressBarPerfil.isVisible = false
        binding.imgSinDireciones.isVisible = true
        binding.textSinDirecciones.isVisible = true
    }

    private fun hideAll() {
        binding.progressBarPerfil.isVisible = false
        binding.imgSinDireciones.isVisible = false
        binding.textSinDirecciones.isVisible = false
    }
}