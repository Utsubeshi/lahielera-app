package com.lahielera.app.ui.catalogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.ItemCategoriaBinding
import com.lahielera.app.model.Categoria
import com.lahielera.app.model.Producto
import com.squareup.picasso.Picasso

class CategoriasAdapter : RecyclerView.Adapter<CategoriasAdapter.ViewHolder>() {

    private  var data: ArrayList<Categoria>  = arrayListOf<Categoria>()
    private lateinit var listener: OnCategoriaClickListener
    private val picasso = Picasso.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categoria, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoria = data[position]
        with(holder) {
            picasso.load(categoria.imgUrl)
                    .into(binding.categoriaImg)
            binding.categoriaNombre.text = categoria.nombre
            binding.categoriaImg.setOnClickListener {
                listener.getCategoria(categoria.nombre)
            }
        }
    }

    fun addData(listaCategorias: ArrayList<Categoria>, listener: OnCategoriaClickListener) {
        this.data = listaCategorias
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCategoriaBinding.bind(itemView)
    }

    interface OnCategoriaClickListener {
        fun getCategoria(categoria: String)
    }
}