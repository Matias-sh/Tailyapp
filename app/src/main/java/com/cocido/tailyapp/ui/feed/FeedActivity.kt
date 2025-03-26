package com.cocido.tailyapp.ui.feed

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cocido.tailyapp.R
import com.cocido.tailyapp.data.model.Post
import com.cocido.tailyapp.ui.auth.LoginActivity

class FeedActivity : AppCompatActivity() {

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        feedRecyclerView = findViewById(R.id.feedRecyclerView)
        feedRecyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (token.isNullOrEmpty()) {
            // Usuario no logueado, ir al login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Usuario logueado, continuar
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feed)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TEMP: Datos falsos
        val posts = listOf(
            Post("Tincho Sosa", "Formosa", "Reencuentro",
                "Por fin encontré a Skibidi Sigma...",
                "https://i.pinimg.com/736x/fb/c4/82/fbc4827aa2fbffe857b06354c95e3ae3.jpg", 20),
            Post("Lucas Britez", "Formosa", "Mascota Perdida",
                "Hola, quería avisar que este gato...",
                "https://media.tenor.com/ldNjzyrqeIMAAAAe/gato-meme.png", 4)
        )

        feedAdapter = FeedAdapter(posts)
        feedRecyclerView.adapter = feedAdapter
    }
}
