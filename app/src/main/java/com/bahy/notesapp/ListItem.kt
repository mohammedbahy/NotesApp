package com.bahy.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bahy.notesapp.databinding.ActivityListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListItem : AppCompatActivity() {

    private lateinit var binding: ActivityListItemBinding
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    private var noteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getStringExtra("noteIndex")
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
                val note = hashMapOf(
                    "title" to title,
                    "content" to content,
                    "timestamp" to System.currentTimeMillis()
                )

                if (noteId != null) {
                    db.collection("users").document(userId)
                        .collection("notes").document(noteId!!)
                        .set(note)
                } else {
                    db.collection("users").document(userId)
                        .collection("notes")
                        .add(note)
                }
            }
            finish()
        }

    }
}
