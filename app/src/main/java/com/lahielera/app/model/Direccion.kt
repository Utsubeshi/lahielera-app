package com.lahielera.app.model

import com.google.firebase.firestore.DocumentId

data class Direccion (
        @DocumentId
        var id: String = "",
        var calle: String = "",
        var numero: String = "",
        var referencia: String = "",
        var distrito: String = "",
        var provincia: String = "",
        var departamento: String = "",
        var esPredeterminada: Boolean = false
) {
    fun getDireccionCompleta(): String {
        return "${this.calle} ${this.numero}"
    }
}