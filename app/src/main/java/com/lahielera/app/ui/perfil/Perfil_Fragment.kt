package com.lahielera.app.ui.perfil

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lahielera.app.R
import com.lahielera.app.databinding.PerfilFragmentBinding
import com.lahielera.app.model.Usuario

class Perfil_Fragment : Fragment() {

    private lateinit var viewModel: PerfilViewModel
    private lateinit var binding: PerfilFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.perfil__fragment,
                container,
                false
        )
        //cargar spinner
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.documento_tipo,
                android.R.layout.simple_spinner_item

        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDocumentos.adapter = adapter
        }



        //
        auth = Firebase.auth
        binding.botonGuardarPerfil.setOnClickListener {
            saveUserData()
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        binding.perfilViewModel = viewModel
        onSaveUserEvent()
        setTipoDocumento()
    }

    private fun onSaveUserEvent() {
        viewModel.onSaveSucess.observe(viewLifecycleOwner, Observer { onSucess ->
            if (onSucess) {
                view?.let {
                    Snackbar.make(it, "Datos guardados exitosamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
        })
    }

    private fun validarForm(): Boolean{
        //TODO
        return true
    }

    private fun saveUserData() {
        if (!validarForm()){
            return
        }
        val usuario = Usuario()
        with(binding) {
            usuario.nombres = nombresPerfil.editText?.text.toString()
            usuario.apellidos = apellidosPerfil.editText?.text.toString()
            usuario.celular = celularPerfil.editText?.text.toString()
            usuario.documento = documentoPerfil.editText?.text.toString()
            usuario.tipoDocumento = spinnerDocumentos.selectedItemPosition
        }
        viewModel.saveUserOnFireStore(usuario)
    }

    private fun setTipoDocumento() {
        viewModel.usuario.observe(viewLifecycleOwner, Observer { user ->
            binding.spinnerDocumentos.setSelection(user.tipoDocumento)
        })
    }


}