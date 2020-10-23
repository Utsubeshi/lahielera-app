package com.lahielera.app.ui.catalogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.FragmentCatalogoBinding
import com.lahielera.app.databinding.ItemProductoBinding
import com.lahielera.app.model.Producto
import com.squareup.picasso.Picasso

class CatalogoAdapter (): RecyclerView.Adapter<CatalogoAdapter.ViewHolder>() {

    private  var data: ArrayList<Producto>  = arrayListOf<Producto>()
    private val picasso = Picasso.get()
    private lateinit var listener: OnProductosClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val producto = data[position]
            binding.productoNombre.text = producto.nombre
            binding.productoMarca.text = producto.marca
            picasso.load(producto.urlImg)
                    .into(binding.productoImagen)
            val precio = "S/ ${producto.precio}"
            binding.productoPrecio.text = precio
            binding.productoImagen.setOnClickListener{
                listener.onProductoClick(producto)
            }
            binding.addToCart.setOnClickListener{
                listener.onAddToCart(producto)
            }
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemProductoBinding.bind(itemView)
    }

    fun addData( listaProducto: ArrayList<Producto>, onProductosClickListener: OnProductosClickListener) {
        this.data = listaProducto
        this.listener = onProductosClickListener
        notifyDataSetChanged()
    }

    interface OnProductosClickListener {
        fun onProductoClick(producto: Producto)

        fun onAddToCart(producto: Producto)
    }
}