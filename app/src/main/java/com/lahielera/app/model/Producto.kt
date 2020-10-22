package com.lahielera.app.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Producto (val idProducto: String? = "",
                     val nombre: String? = "",
                     val marca: String? = "",
                     val urlImg: String? = "",
                     val precio: Double = 0.0,
                     val descripcion: String? = "") : Serializable {


    override fun toString(): String {
        return "Producto(idProducto='$idProducto', nombre='$nombre', marca='$marca', urlImg='$urlImg', precio=$precio)"
    }
}