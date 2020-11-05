package com.lahielera.app.ui.checkout

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lahielera.app.R
import com.lahielera.app.database.ProductoDatabase
import com.lahielera.app.databinding.CheckoutFragmentBinding
import com.lahielera.app.model.Producto
import kotlin.math.ceil

class CheckoutFragment : Fragment() {

    private lateinit var bindindg: CheckoutFragmentBinding
    private lateinit var viewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bindindg = DataBindingUtil.inflate(
                inflater,
                R.layout.checkout_fragment,
                container,
                false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        val viewModelFactory = CheckoutViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CheckoutViewModel::class.java)
        bindindg.editarClienteButton.setOnClickListener {
            editarPerfil()
        }
        bindindg.editarDireccionButton.setOnClickListener {

        }
        bindindg.agregarCambiarDireccion.setOnClickListener {
            moveToDirecciones()
        }
        return bindindg.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.productos.observe(viewLifecycleOwner, Observer { productos ->
            val subtTotal = getSubTotal(productos)
            bindindg.subtotalCheckout.text = getString(R.string.formato_subtotal, subtTotal)
            val envio = ceil(subtTotal * 0.1)
            bindindg.envioCheckout.text = getString(R.string.formato_envio, envio )
            val total = envio + subtTotal
          bindindg.totalCheckout.text = getString(R.string.formato_total, total)
        })
        viewModel.usuario.observe(viewLifecycleOwner, Observer {
            bindindg.clienteCheckout.text = it.getNombreCompleto()
            bindindg.celularCheckout.text = it.celular
        })
        viewModel.direccion.observe(viewLifecycleOwner, Observer {direccion ->
            if (direccion.esPredeterminada) {
                bindindg.direccionCheckout.text = direccion.getDireccionCompleta()
                bindindg.distritoCheckout.text = direccion.distrito
            } else {
                bindindg.direccionCheckout.text = getString(R.string.sin_direccion_prederterminada)
                bindindg.distritoCheckout.text = ""
            }
            //TODO ver como deshabilitar el boton
            // bindindg.botonPagar.isEnabled = (direccion != null)
        })
        viewModel.hasDireccion.observe(viewLifecycleOwner, Observer { hasDireccion ->
            if (hasDireccion) {
                bindindg.editarDireccionButton.isEnabled = hasDireccion
                bindindg.botonPagar.isEnabled = hasDireccion
                bindindg.editarDireccionButton.alpha = 1.0F
            } else {
                bindindg.editarDireccionButton.isEnabled = hasDireccion
                bindindg.editarDireccionButton.alpha = 0.2F
                bindindg.botonPagar.isEnabled = hasDireccion
            }
        })
    }

    private fun editarPerfil() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToNavPerfil())
    }

    private fun moveToDirecciones() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToNavUbicaciones())
    }

    private fun editarDireccion() {
        findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToNavDireccion())
    }

    private fun getSubTotal(productos: List<Producto>): Double {
        var total = 0.0
        for (producto in productos) {
            total = total.plus(producto.precio.times(producto.cantidad))
        }
        return total
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
        bindindg.direccionCheckout.text = getString(R.string.sin_direccion_prederterminada)
        bindindg.distritoCheckout.text = ""
    }
}