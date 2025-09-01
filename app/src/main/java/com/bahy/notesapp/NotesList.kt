package com.bahy.notesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bahy.notesapp.databinding.ActivityNotesListBinding
import java.util.zip.Inflater

class NotesList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rv: RecyclerView = binding.notesRecyclerView

    }
}