package com.lahielera.app.ui.payment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lahielera.app.R
import com.lahielera.app.culqui.culqui.Card
import com.lahielera.app.culqui.culqui.Token
import com.lahielera.app.culqui.culqui.TokenCallback
import com.lahielera.app.culqui.validation.Validation
import com.lahielera.app.database.ProductoDatabase
import com.lahielera.app.database.ProductoDatabaseDAO
import com.lahielera.app.databinding.PaymentFragmentBinding
import com.lahielera.app.model.Pedido
import com.lahielera.app.model.Producto
import com.lahielera.app.model.Usuario
import com.lahielera.app.notification.PusherClient
import com.lahielera.app.ui.carrito.CarritoViewModelFactory
import com.lahielera.app.ui.perfil.PerfilViewModel
import org.json.JSONObject
import java.lang.Exception
import java.math.BigDecimal
import kotlin.properties.Delegates


class PaymentFragment ( ) : Fragment() {

    private lateinit var viewModel: PaymentViewModel
    private lateinit var binding: PaymentFragmentBinding
    private lateinit var validation: Validation
    private lateinit var viewModelPeril: PerfilViewModel
    private lateinit var auth: FirebaseAuth
    private var pedido = Pedido()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.payment_fragment,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = ProductoDatabase.getInstance(application).productoDatabaseDAO
        val viewModelFactory = PaymentViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
        viewModel.productos.observe( viewLifecycleOwner, Observer { productos ->
            pedido.pedido = productos as ArrayList<Producto>
        })
        validation = Validation()
        binding.progressBarCulqui.isVisible = false
        with(binding) {
            cvvTarjeta.isEnabled = false
        }
        auth = Firebase.auth
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments != null) {
            val paymentFragmentArgs by navArgs<PaymentFragmentArgs>()
            var total = BigDecimal(paymentFragmentArgs.total.toDouble())
            pedido.total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
            Log.d("TAG", pedido.total.toString())
            pedido.envio = paymentFragmentArgs.envio
            binding.botonPagarCulqui.text = getString(R.string.formato_pagar, paymentFragmentArgs.total )
        }
        with(binding) {
            numeroTarjeta.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s!!.isEmpty()) cvvTarjeta.isEnabled = true
                }

                override fun afterTextChanged(s: Editable?) {
                    val text = numeroTarjeta.editText?.text.toString()
                    if (s!!.isEmpty()) numeroTarjeta.error = ""
                    if (validation.luhn(text))
                        numeroTarjeta.error = null
                    else
                        numeroTarjeta.error = "Campo requerido"
                    val cvv = validation.bin(text, tipoTarjeta)
                    if (cvv > 0) {
                        cvvTarjeta.editText?.filters = arrayOf<InputFilter>(LengthFilter(cvv))
                        cvvTarjeta.isEnabled = true
                    } else {
                        cvvTarjeta.isEnabled = false
                        //TODO posible error
                        cvvTarjeta.editText?.text = null
                    }
                }

            })
            anioTarjeta.editText?.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

                override fun afterTextChanged(s: Editable?) {
                    val text = anioTarjeta.editText?.text.toString()
                    if (validation.year(text))
                        anioTarjeta.error = "Campo requerido"
                    else
                        anioTarjeta.error = null
                }

            })

            mesTarjeta.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(s: Editable?) {
                    val text = mesTarjeta.editText?.text.toString()
                    if(validation.month(text))
                        mesTarjeta.error = "Campor requerido"
                    else
                        mesTarjeta.error = null
                }
            })

            botonPagarCulqui.setOnClickListener {
                progressBarCulqui.isVisible = true
                val card: Card = Card(
                        numeroTarjeta.editText?.text.toString(),
                        cvvTarjeta.editText?.text.toString(),
                        mesTarjeta.editText?.text.toString().toInt(),
                        ("20" + anioTarjeta.editText?.text.toString()).toInt(),
                        emailTarjeta.editText?.text.toString()
                )
                val token = Token("pk_test_Kd3gwqwwXEHBBj9r")
                token.createToken(requireContext(), card, object : TokenCallback {
                    override fun onSuccess(token: JSONObject?) {
                        try {
                            registrarPedido(token.toString())
                        } catch (e: Exception) {
                            progressBarCulqui.isVisible = false
                        }
                        progressBarCulqui.isVisible = false
                    }

                    override fun onError(error: Exception?) {
                        progressBarCulqui.isVisible = false
                        Toast.makeText(requireContext(), "Hubo un error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun registrarPedido(token: String) {
        pedido.clienteID = auth.currentUser?.uid ?: ""
        pedido.token = token.substring(24, 49)
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Pedidos").add(pedido)
            .addOnSuccessListener { docRef ->
                var pusherClient = PusherClient(requireContext())
                pusherClient.connect(docRef.id)
                Toast.makeText(requireContext(), "Gracias por su compra!", Toast.LENGTH_SHORT).show()
                viewModel.onClear()
                findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToNavCatalogo())

            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}