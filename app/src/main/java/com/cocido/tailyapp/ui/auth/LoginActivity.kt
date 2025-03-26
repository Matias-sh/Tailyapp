package com.cocido.tailyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cocido.tailyapp.ui.home.MainActivity
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
                loginViewModel.loginWithGoogleMobile(authCode)
            } else {
                Toast.makeText(this, "No se pudo obtener el auth code", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("LoginActivity", "❌ Google sign in failed (code ${e.statusCode}): ${e.message}", e)
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

        // Observa el login con email/contraseña o con Google
        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {
                // Guardar token
                getSharedPreferences("auth", MODE_PRIVATE)
                    .edit()
                    .putString("token", it.token)
                    .apply()

                Toast.makeText(this, "✅ Login exitoso", Toast.LENGTH_SHORT).show()

                // Ir a MainActivity
                startActivity(Intent(this, MainActivity::class.java))
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
            // TODO: Implementar recuperación
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
