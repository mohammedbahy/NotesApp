package com.bahy.notesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val user = auth.currentUser

            if (user != null && user.isEmailVerified) {
                startActivity(Intent(this, NotesList::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()
        }, 2000)
    }
}
