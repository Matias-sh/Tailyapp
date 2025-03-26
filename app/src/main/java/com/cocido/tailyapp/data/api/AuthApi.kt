package com.cocido.tailyapp.data.api

import com.cocido.tailyapp.data.model.GoogleAuthCodeRequest
import com.cocido.tailyapp.data.model.LoginRequest
import com.cocido.tailyapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/google-auth-code")
    suspend fun loginWithGoogleMobile(
        @Body request: GoogleAuthCodeRequest
    ): Response<LoginResponse>
}
