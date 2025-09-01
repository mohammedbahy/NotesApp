package com.bahy.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bahy.notesapp.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.alreadyUser.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val conPassword = binding.confirmPassword.text.toString()

            if (email.isBlank() || password.isBlank() || conPassword.isBlank())
                Toast.makeText(this, "Fill Required Fields", Toast.LENGTH_SHORT).show();
            else if (password.length < 6)
                Toast.makeText(this, "Short password", Toast.LENGTH_SHORT).show()
            else if (password != conPassword)
                Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            else {
                binding.loadingProgress.isVisible = true

                //Sign up logic
                signUp(email,password)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun verifyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email!", Toast.LENGTH_SHORT).show();
                    binding.loadingProgress.isVisible=false
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
            }
    }
}