package com.cocido.tailyapp.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val persona: Persona
)

data class Persona(
    val nombre: String,
    val apellido: String,
    val telefono: String? = null // Asegúrate de que teléfono sea opcional si no se ingresa
)
