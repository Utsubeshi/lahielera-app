package com.lahielera.app.ui.pedidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.ItemPedidoBinding
import com.lahielera.app.model.Pedido
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class PedidosAdapter () : RecyclerView.Adapter<PedidosAdapter.ViewHolder>() {

    private  var data: ArrayList<Pedido>  = arrayListOf<Pedido>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = data[position]
        val df: DateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        val detallePedido = pedido.pedido
        var detalle = ""
        for (producto in detallePedido) {
            detalle +=  "\nProducto: ${producto.nombre}\n" +
                    "Cantidad: ${producto.cantidad}\n" +
                    "Precio unitario: ${producto.precio}\n"
        }
        with(holder.binding) {
            pedidoFecha.text = df.format(pedido.fecha)
            pedidoTotal.text = pedido.total
            pedidoEstado.text =  when(pedido.estado) {
                1 -> "En proceso"
                2 -> "Entregado"
                3 -> "Cancelado"
                else -> "Otro"
            }
            txtPedido.text = detalle
        }
    }

    fun loadData(data: ArrayList<Pedido>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemPedidoBinding.bind(itemView)
    }
}