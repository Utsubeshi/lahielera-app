package com.lahielera.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Direccion (
        @DocumentId
        var id: String = "",
        var calle: String = "",
        var numero: String = "",
        var referencia: String = "",
        var distrito: String = "",
        var provincia: String = "",
        var departamento: String = "",
        @Exclude
        var esPredeterminada: Boolean = false
) : Serializable {
    @Exclude
    fun getDireccionCompleta(): String {
        return "${this.calle} ${this.numero}"
    }
}