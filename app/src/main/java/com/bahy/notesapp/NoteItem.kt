package com.bahy.notesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bahy.notesapp.databinding.ActivityNoteItemBinding

class NoteItem : AppCompatActivity() {
    private lateinit var binding: ActivityNoteItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linearLayout.setOnClickListener{

        }

    }
}