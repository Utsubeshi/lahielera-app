package com.lahielera.app.model

import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

data class Pedido (
    var clienteID: String = "",
    var token: String = "",
    var fecha: Date = Date(),
    var pedido: ArrayList<Producto> = arrayListOf(),
    var envio: Int = 0,
    var total: String = "",
    var estado: Int = 1) {}