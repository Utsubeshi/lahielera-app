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

class UbicacionesFragment : Fragment() {

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

        binding.botonAgregarDireccion.setOnClickListener {
            //moveToNuevaDireccion()
            findNavController().navigate(UbicacionesFragmentDirections.actionNavUbicacionesToNavDireccion())
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
                onEmptyList(true)
           } else {
                adapter.addData(lista)
                adapter.notifyDataSetChanged()
                onEmptyList(false)
            }
        })
    }

    private fun onEmptyList (isVisible: Boolean) {
        with(binding) {
            imgSinDireciones.isVisible = isVisible
            textSinDirecciones.isVisible = isVisible
        }
    }

    override fun onResume() {
        super.onResume()
        getDirecciones()
    }
}