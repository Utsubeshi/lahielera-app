package com.lahielera.app.model

import android.os.Parcel
import android.os.Parcelable

data class Producto (val idProducto: String? = "",
                     val nombre: String? = "",
                     val marca: String? = "",
                     val urlImg: String? = "",
                     val precio: Double = 0.0,
                     val descripcion: String? = "") : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString()) {
    }

    override fun toString(): String {
        return "Producto(idProducto='$idProducto', nombre='$nombre', marca='$marca', urlImg='$urlImg', precio=$precio)"
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto {
            return Producto(parcel)
        }

        override fun newArray(size: Int): Array<Producto?> {
            return arrayOfNulls(size)
        }
    }


}