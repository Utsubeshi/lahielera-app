package com.lahielera.app.model

data class Usuario (
        var nombres: String  = "",
        var apellidos: String  = "",
        var tipoDocumento: Int = 0,
        var documento: String  = "",
        var celular: String  = "",
        var direccion: Direccion = Direccion()
)