package com.lahielera.app.ui.direcciones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.ItemDireccionBinding
import com.lahielera.app.model.Direccion
import com.lahielera.app.model.Producto

class UbicacionesAdapter : RecyclerView.Adapter<UbicacionesAdapter.ViewHolder>() {

    private  var data: ArrayList<Direccion>  = arrayListOf<Direccion>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UbicacionesAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_direccion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UbicacionesAdapter.ViewHolder, position: Int) {
        val direccion = data[position]
        with(holder) {
            binding.direccion = direccion
        }
    }

    override fun getItemCount() = data.size

    fun addData(lista: ArrayList<Direccion>) {
        this.data = lista
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDireccionBinding.bind(itemView)
    }
}