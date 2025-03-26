package com.cocido.tailyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocido.tailyapp.data.api.RetrofitInstance
import com.cocido.tailyapp.data.model.GoogleAuthCodeRequest
import com.cocido.tailyapp.data.model.LoginRequest
import com.cocido.tailyapp.data.model.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginResponse.postValue(response.body())
                    Log.d("LOGIN", "Token recibido: ${response.body()?.token}")
                } else {
                    _errorMessage.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión")
            }
        }
    }

    fun loginWithGoogleMobile(code: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.loginWithGoogleMobile(GoogleAuthCodeRequest(code))
                if (response.isSuccessful) {
                    _loginResponse.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

}
