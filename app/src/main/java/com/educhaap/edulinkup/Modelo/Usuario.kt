package com.educhaap.edulinkup.Modelo

data class Usuario(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    val provider: String? = null,
    var mensajeNoLeido: Int = 0,
    var ultimoMensaje: String? = null
)
