package com.bahy.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahy.notesapp.databinding.ActivityNotesListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class NotesList : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var notes: MutableList<Note>
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = Firebase.auth.currentUser?.uid ?: return

        notes = NotesStorage.loadNotes(this, userId)
        val rv: RecyclerView = binding.notesRecyclerView

        adapter = NotesAdapter(notes, userId) { note, position ->
            val intent = Intent(this, ListItem::class.java)
            intent.putExtra("noteIndex", position)
            intent.putExtra("noteTitle", note.title)
            intent.putExtra("noteContent", note.content)
            startActivity(intent)
        }


        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = adapter

        binding.addNoteFab.setOnClickListener {
            val intent = Intent(this, ListItem::class.java)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        notes.clear()
        notes.addAll(NotesStorage.loadNotes(this, userId))
        adapter.notifyDataSetChanged()
    }
}
