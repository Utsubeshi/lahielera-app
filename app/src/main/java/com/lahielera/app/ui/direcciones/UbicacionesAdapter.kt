package com.lahielera.app.ui.direcciones

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.ItemDireccionBinding
import com.lahielera.app.model.Direccion
import com.lahielera.app.model.Producto

class UbicacionesAdapter : RecyclerView.Adapter<UbicacionesAdapter.ViewHolder>() {

    private  var data: ArrayList<Direccion>  = arrayListOf<Direccion>()
    private lateinit var listener: OnDireccionClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UbicacionesAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_direccion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UbicacionesAdapter.ViewHolder, position: Int) {
        val direccion = data[position]
        with(holder) {
           if (direccion.esPredeterminada)
               binding.direccionPredeterminada.visibility = View.VISIBLE
            else
               binding.direccionPredeterminada.visibility = View.GONE
            binding.direccion = direccion
            binding.textViewOptions.setOnClickListener {
                val menu  = PopupMenu(holder.itemView.context, binding.textViewOptions)
                menu.inflate(R.menu.menu_direccion)
                menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        return when (p0?.itemId) {
                            R.id.action_predeterminada -> {
                                listener.setPredeterminada(direccion)
                                true
                            }
                            R.id.action_eliminar -> {
                                listener.eliminarDireccion(direccion.id)
                                true
                            }
                            R.id.action_editar -> {
                                listener.editar(direccion)
                                true
                            }
                            else -> false
                        }
                    }
                })
                menu.show()
            }
        }
    }

    override fun getItemCount() = data.size

    fun addData(lista: ArrayList<Direccion>, listener: OnDireccionClickListener) {
        this.data = lista
        this.listener = listener
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDireccionBinding.bind(itemView)
    }

    interface OnDireccionClickListener {
        fun setPredeterminada(direccion: Direccion)
        fun eliminarDireccion (id: String)
        fun editar(direccion: Direccion)
    }
}