package com.lahielera.app.model

data class Producto (val idProducto: String = "", val nombre: String = "", val marca: String = "", val urlImg: String = "", val precio: Double = 0.0) {
    override fun toString(): String {
        return "Producto(idProducto='$idProducto', nombre='$nombre', marca='$marca', urlImg='$urlImg', precio=$precio)"
    }
}