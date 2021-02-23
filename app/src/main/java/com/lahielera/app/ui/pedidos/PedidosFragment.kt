package com.lahielera.app.ui.pedidos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lahielera.app.R
import com.lahielera.app.databinding.PedidosFragmentBinding
import kotlinx.android.synthetic.main.carrito_fragment.*

class PedidosFragment : Fragment() {


    private lateinit var viewModel: PedidosViewModel
    private lateinit var binding: PedidosFragmentBinding
    private lateinit var rvPedidos: RecyclerView
    private lateinit var adapter: PedidosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = PedidosFragmentBinding.inflate(inflater, container, false )
        rvPedidos = binding.rvPedidos
        rvPedidos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = PedidosAdapter()
        rvPedidos.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PedidosViewModel::class.java)
        viewModel.pedidoList.observe(viewLifecycleOwner, Observer { pedidos ->
            if (pedidos.isNotEmpty()) {
                adapter.loadData(pedidos)
            }
        })

    }

}