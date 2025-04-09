package com.cocido.tailyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cocido.tailyapp.ui.feed.FeedActivity
import com.cocido.tailyapp.R
import com.cocido.tailyapp.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            Log.i("LoginActivity", "✅ AuthCode recibido: $authCode")

            if (!authCode.isNullOrEmpty()) {
                Log.i("LoginActivity", "Llamando ViewModel con authCode")
                loginViewModel.loginWithGoogleMobile(authCode)
            } else {
                Toast.makeText(this, "No se pudo obtener el auth code", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("LoginActivity", "❌ Google sign in failed: ${e.message}", e)
            Toast.makeText(this, "Fallo autenticación con Google", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val btnGoogle = findViewById<FrameLayout>(R.id.btnGoogle)

        googleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions())

        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {
                Log.i("LoginActivity", "Token recibido del ViewModel: ${it.token}")
                Toast.makeText(this, "✅ Login exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, FeedActivity::class.java))
                finish()
            }
        }

        loginViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "❌ $it", Toast.LENGTH_LONG).show()
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvForgotPassword.setOnClickListener {

        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(getString(R.string.google_client_id))
            .build()
    }
}
