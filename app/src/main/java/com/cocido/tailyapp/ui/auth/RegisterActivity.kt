package com.cocido.tailyapp.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.cocido.tailyapp.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var ivProfilePic: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivProfilePic = findViewById(R.id.ivProfilePic)

        // Asegúrate de que el listener esté correctamente configurado
        ivProfilePic.setOnClickListener {
            openImagePicker()
        }
    }

    // Abre el selector de imágenes
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Maneja la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                // Carga la imagen seleccionada en el ImageView
                Glide.with(this)
                    .load(it)
                    .transform(com.bumptech.glide.load.resource.bitmap.CircleCrop())
                    .into(ivProfilePic)
            } ?: run {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1001
    }
}