package com.lahielera.app.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.io.Serializable

@Entity(tableName = "producto")
data class Producto (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "idFirebase")
    @DocumentId
    var uid: String? = "",

    @ColumnInfo(name = "nombre")
    var nombre: String? = "",

    @ColumnInfo(name = "marca")
    var marca: String? = "",

    @ColumnInfo(name = "urlImg")
    var urlImg: String? = "",

    @ColumnInfo(name = "precio")
    var precio: Double = 0.0,

    @Ignore
    var descripcion: String? = "") : Serializable {


    override fun toString(): String {
        return "Producto(idProducto='$uid', nombre='$nombre', marca='$marca', urlImg='$urlImg', precio=$precio)"
    }
}