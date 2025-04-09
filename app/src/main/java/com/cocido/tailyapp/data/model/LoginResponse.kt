package com.cocido.tailyapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val token: String,

    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    val user: UserInfo? = null
)

data class UserInfo(
    val id: Int,
    val email: String,
    val persona: Persona? = null,
    val mascotas: List<Mascota>? = null
)

data class Persona(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val telefono: String?
)

data class Mascota(
    val id: Int? = null,
    val nombre: String? = null,
    // Podés expandir esto según lo que el backend devuelva
)
