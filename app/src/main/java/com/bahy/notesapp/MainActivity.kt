package com.bahy.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bahy.notesapp.databinding.ActivityMainBinding
import com.bahy.notesapp.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.forgetPass.setOnClickListener{
            binding.loadingProgress.isVisible= true
            val emailAddress = binding.email.toString()

            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.loadingProgress.isVisible=false
                        Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show();
                    }
                }

        }

        binding.createAccount.setOnClickListener{
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }

        binding.signInBtn.setOnClickListener{
            val email= binding.email.text.toString()
            val password= binding.pass.text.toString()

            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Fill Required Fields", Toast.LENGTH_SHORT).show();
            else if (password.length < 6)
                Toast.makeText(this, "Short password", Toast.LENGTH_SHORT).show()
            else {
                binding.loadingProgress.isVisible = true

                //Sign in logic
                signIn(email,password)
            }
        }

    }

    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.loadingProgress.isVisible=false
                    if(auth.currentUser!!.isEmailVerified)
                    {
                        startActivity(Intent(this,NotesList::class.java))
                        finish()
                    }
                    else
                        Toast.makeText(this, "Check Your Email!!!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT,).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
                startActivity(Intent(this, NotesList::class.java))
                finish()
        }
    }
}