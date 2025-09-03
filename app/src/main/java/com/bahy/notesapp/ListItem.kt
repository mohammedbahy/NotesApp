package com.bahy.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bahy.notesapp.databinding.ActivityListItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class ListItem : AppCompatActivity() {

    private lateinit var binding: ActivityListItemBinding
    private var noteIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteIndex = intent.getIntExtra("noteIndex", -1).takeIf { it != -1 }
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")

        if (noteTitle != null || noteContent != null) {
            binding.noteTitleEditText.setText(noteTitle)
            binding.noteContentEditText.setText(noteContent)
        }

        binding.saveNoteButton.setOnClickListener {
            val title = binding.noteTitleEditText.text.toString()
            val content = binding.noteContentEditText.text.toString()

            if (title.isNotEmpty() || content.isNotEmpty()) {
                val userId = Firebase.auth.currentUser?.uid ?: return@setOnClickListener
                val notes = NotesStorage.loadNotes(this, userId).toMutableList()

                if (noteIndex != null) {
                    notes[noteIndex!!] = Note(title, content, System.currentTimeMillis())
                } else {
                    val note = Note(title, content, System.currentTimeMillis())
                    notes.add(note)
                }

                NotesStorage.saveNotes(this, userId, notes)
            }
            finish()
        }

    }
}
