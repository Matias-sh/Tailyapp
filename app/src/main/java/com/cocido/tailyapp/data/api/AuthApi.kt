package com.cocido.tailyapp.data.api

import com.cocido.tailyapp.data.model.GoogleTokenRequest
import com.cocido.tailyapp.data.model.LoginRequest
import com.cocido.tailyapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/auth/google-mobile")
    suspend fun loginWithGoogleMobile(@Body tokenRequest: GoogleTokenRequest): Response<LoginResponse>
}
