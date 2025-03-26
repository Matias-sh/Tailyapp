package com.cocido.tailyapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cocido.tailyapp.ui.feed.FeedActivity
import com.cocido.tailyapp.R

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val splashScreenImage: ImageView = findViewById(R.id.SplashScreenImage)

        // Inicialmente invisible y reducido
        splashScreenImage.scaleX = 0f
        splashScreenImage.scaleY = 0f
        splashScreenImage.alpha = 0f

        // Animación estilo mascota (salto y rebote)
        splashScreenImage.animate()
            .alpha(1f)                     // Aparece gradualmente
            .scaleX(1f)                    // Escalado horizontal normal
            .scaleY(1f)                    // Escalado vertical normal
            .translationYBy(-50f)          // Salto hacia arriba
            .rotationBy(360f)              // Giro completo alegre
            .setInterpolator(OvershootInterpolator()) // Rebote al final
            .setDuration(1200)             // Duración agradable (1.2 segundos)
            .start()

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit

        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}