package com.lahielera.app.ui.direcciones.direccion

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.lahielera.app.R
import com.lahielera.app.databinding.DireccionFragmentBinding
import com.lahielera.app.model.Direccion

class DireccionFragment : Fragment() {

    private lateinit var binding: DireccionFragmentBinding
    private lateinit var viewModel: DireccionViewModel
    private var direccion = Direccion()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.direccion_fragment,
                container,
                false
        )
        hideProgressBar()
        //Databinding de la direccion si es que llego alguna
        val direccionFragmentArgs by navArgs<DireccionFragmentArgs>()
        if (direccionFragmentArgs.direccion != null) {
            direccion = direccionFragmentArgs.direccion!!
            binding.direccion = direccion
        }
        binding.lifecycleOwner = this
        //boton que guarda o actualiza la data de la direccion
        binding.botonGuardarDireccion.setOnClickListener {
            showProgressBar()
            saveDireccion()
            binding.botonGuardarDireccion.isEnabled = false
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DireccionViewModel::class.java)
        onSaveSuccess()
    }

    private fun onSaveSuccess() {
        viewModel.onSaveSuccess.observe(viewLifecycleOwner, Observer { onSuccess ->
            if (onSuccess) {
                view?.let {
                    Snackbar.make(it, "Datos guardados exitosamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
                moveToMisDirecciones()
                hideProgressBar()
            }
        })
    }

    private fun validarForm(): Boolean {
        //TODO
        return true
    }

    private fun saveDireccion() {

        if (!validarForm()) return

        val direccion = Direccion()
        with(binding) {
            direccion.calle = locationDireccion.editText?.text.toString()
            direccion.referencia = locationReferencia.editText?.text.toString()
            direccion.distrito = locationDistrito.editText?.text.toString()
            direccion.provincia = locationProvincia.editText?.text.toString()
            direccion.departamento = locationDepartamento.editText?.text.toString()
            direccion.numero = locationNumero.editText?.text.toString()
        }
        if (this.direccion.id.isEmpty()) {
            viewModel.saveDirecion(direccion)
        } else {
            direccion.id = this.direccion.id
            viewModel.updateDireccion(direccion)
        }

    }

    private fun moveToMisDirecciones() {
        findNavController().navigate(DireccionFragmentDirections.actionNavDireccionToNavUbicaciones())
    }

    private fun showProgressBar() {
        binding.progressBarDireccion.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBarDireccion.isVisible = false
    }


}