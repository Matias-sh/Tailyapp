package com.cocido.tailyapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.cocido.tailyapp.data.api.RetrofitInstance
import com.cocido.tailyapp.data.model.GoogleAuthCodeRequest
import com.cocido.tailyapp.data.model.LoginRequest
import com.cocido.tailyapp.data.model.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val context = application.applicationContext

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                Log.d("LoginViewModel", "Login con email - código: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i("LoginViewModel", "Body login email: $body")

                    if (!body?.token.isNullOrEmpty()) {
                        guardarToken(body!!.token)
                        _loginResponse.postValue(body)
                    } else {
                        _errorMessage.postValue("Token vacío o nulo")
                    }
                } else {
                    _errorMessage.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error de red login email: ${e.message}")
                _errorMessage.postValue("Error de conexión")
            }
        }
    }

    fun loginWithGoogleMobile(code: String) {
        viewModelScope.launch {
            try {
                Log.i("LoginViewModel", "Llamando API loginWithGoogleMobile con código: $code")
                val response = RetrofitInstance.api.loginWithGoogleMobile(GoogleAuthCodeRequest(code))
                Log.d("LoginViewModel", "Login Google - código: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i("LoginViewModel", "Body login Google: $body")

                    if (!body?.token.isNullOrEmpty()) {
                        guardarToken(body!!.token)
                        _loginResponse.postValue(body)
                    } else {
                        _errorMessage.postValue("Token vacío o nulo desde Google")
                    }
                } else {
                    _errorMessage.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error de red login Google: ${e.message}")
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    private fun guardarToken(token: String) {
        Log.i("LoginViewModel", "Guardando token en SharedPreferences: $token")
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val success = prefs.edit().putString("token", token).commit()
        Log.i("LoginViewModel", "¿Token guardado exitosamente?: $success")
    }
}
