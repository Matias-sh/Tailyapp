package com.cocido.tailyapp.data.model

data class LoginResponse(
    val token: String,
    val user: UserInfo? = null
)

data class UserInfo(
    val id: Int,
    val email: String
)

