package com.lahielera.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.io.Serializable

@Entity(tableName = "producto")
data class Producto(

        @PrimaryKey(autoGenerate = true)
    @Exclude
        var id: Long = 0L,

        @ColumnInfo(name = "idFirebase")
        @DocumentId
        var uid: String? = "",

        @ColumnInfo(name = "cantidad")
        var cantidad: Int = 1,

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
        return "Producto(id=$id, uid=$uid, cantidad=$cantidad, nombre=$nombre, marca=$marca, urlImg=$urlImg, precio=$precio, descripcion=$descripcion)"
    }
}