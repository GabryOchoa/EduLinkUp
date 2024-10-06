package com.educhaap.edulinkup.Modelo

data class Usuario(
    val uid: String? = null,
    val email: String? = null,
    val rol: String? = null,
    val nombreCompleto: String? = null,
    val provider: String? = null,
    val firstName: String? = null,
    val secondName: String? = null,
    val firstLastName: String? = null,
    val secondLastName: String? = null,
    var institucion: Institucion? = null,
    var carrera: Carrera? = null,
    var mensajeNoLeido: Int = 0,
    var ultimoMensaje: String? = null


)
