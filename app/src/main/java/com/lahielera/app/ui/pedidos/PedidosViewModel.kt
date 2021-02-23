package com.lahielera.app.ui.pedidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.lahielera.app.model.Pedido


class PedidosViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()

    private var _pedidoList = MutableLiveData<ArrayList<Pedido>>()
    val pedidoList : LiveData<ArrayList<Pedido>>
        get() = _pedidoList

    init {
        getPedidosFromFireStore()
    }


    private fun getPedidosFromFireStore() {
        var data = arrayListOf<Pedido>()
        database.collection("Pedidos")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val pedido = doc.toObject(Pedido::class.java)
                    data.add(pedido)
                }
                _pedidoList.value = data
            }
    }

}