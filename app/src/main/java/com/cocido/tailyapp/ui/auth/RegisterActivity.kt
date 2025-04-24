package com.cocido.tailyapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cocido.tailyapp.R
import com.bumptech.glide.Glide
import com.cocido.tailyapp.data.api.RetrofitInstance
import com.cocido.tailyapp.data.model.RegisterRequest
import com.cocido.tailyapp.data.model.RegisterResponse
import com.cocido.tailyapp.data.model.Persona // Asegúrate de usar el modelo correcto
import com.cocido.tailyapp.ui.feed.FeedActivity
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordRepeat: EditText
    private lateinit var etPhone: EditText  // Campo para teléfono
    private lateinit var btnRegister: Button
    private lateinit var ivProfilePic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPasswordRepeat = findViewById(R.id.etPasswordRepeat)
        etPhone = findViewById(R.id.etPhone)  // Referencia al campo teléfono
        btnRegister = findViewById(R.id.btnRegister)
        ivProfilePic = findViewById(R.id.ivProfilePic)

        // Obtener los datos enviados desde LoginActivity (por ejemplo, nombre y correo)
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")

        // Rellenar campos con los datos de la cuenta de Google
        email?.let { etEmail.setText(it) }
        name?.let { etName.setText(it) }

        ivProfilePic.setOnClickListener {
            openImagePicker()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()  // Obtener teléfono
            val password = etPassword.text.toString()
            val passwordRepeat = etPasswordRepeat.text.toString()

            if (password == passwordRepeat) {
                // Llamada al backend para registrar el nuevo usuario
                registerUser(name, surname, email, phone, password)
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función que registra el usuario usando los datos de la interfaz
    private fun registerUser(name: String, surname: String, email: String, phone: String, password: String) {
        lifecycleScope.launch {
            try {
                // Creamos el request de registro con los datos del formulario
                val persona = Persona(name, surname, phone) // Agregamos teléfono
                val request = RegisterRequest(email, password, persona)

                // Hacemos la llamada al API para registrar el usuario
                val response: Response<RegisterResponse> = RetrofitInstance.api.register(request)

                // Verificamos si la respuesta fue exitosa
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        // Si el registro es exitoso, mostramos un mensaje y vamos al FeedActivity
                        Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, FeedActivity::class.java))
                        finish()
                    }
                } else {
                    // Si hubo un error en el registro, mostramos un mensaje de error
                    Toast.makeText(this@RegisterActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Si ocurre un error, mostramos un mensaje de error
                Toast.makeText(this@RegisterActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Abre el selector de imágenes
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val selectedImage = data?.data
            selectedImage?.let {
                Glide.with(this).load(it).transform(com.bumptech.glide.load.resource.bitmap.CircleCrop()).into(ivProfilePic)
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1001
    }
}

