package com.lahielera.app.ui.carrito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.ItemCarritoBinding
import com.lahielera.app.model.Producto
import com.squareup.picasso.Picasso

class CarritoAdapter(): RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    private var data: List<Producto> = listOf()
    private val picasso = Picasso.get()
    private lateinit var listener: OnCarritoItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView = LayoutInflater.from(parent.context)
               .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = data[position]
        with(holder) {
            picasso.load(producto.urlImg).into(binding.imagenProducto)
            binding.producto = producto
            binding.itemEliminar.setOnClickListener {
                listener.eliminarDelCarrito(producto.uid.toString())
            }
            binding.itemMas.setOnClickListener {
                producto.cantidad = producto.cantidad.plus(1)
                listener.aumentarCantidad(producto)
            }
            binding.itemMenos.setOnClickListener {
                if (producto.cantidad > 1 )
                    producto.cantidad = producto.cantidad.minus(1)
                    listener.reducirCantidad(producto)
            }


        }
    }

    override fun getItemCount() = data.size

    fun addData(listaCarrito: List<Producto>, clickListener: OnCarritoItemClickListener) {
        this.data = listaCarrito
        this.listener = clickListener
        notifyDataSetChanged()
    }


    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCarritoBinding.bind(itemView)
    }

    interface OnCarritoItemClickListener {
        fun reducirCantidad(producto: Producto)
        fun aumentarCantidad(producto: Producto)
        fun eliminarDelCarrito(uid: String)
    }
}