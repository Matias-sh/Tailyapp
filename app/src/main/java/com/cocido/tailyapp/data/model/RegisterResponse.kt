package com.cocido.tailyapp.data.model

data class RegisterResponse(
    val user: User,
    val access_token: String,
    val refresh_token: String
)

data class User(
    val id: Int,
    val email: String,
    val persona: Persona
)
