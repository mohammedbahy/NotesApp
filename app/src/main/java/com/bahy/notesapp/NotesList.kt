package com.bahy.notesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahy.notesapp.databinding.ActivityNotesListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class NotesList : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private lateinit var adapter: NotesAdapter
    private val notes = mutableListOf<Pair<String,Note>>()

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotesAdapter(notes) { docId, note ->
            val intent = Intent(this, ListItem::class.java)
            intent.putExtra("noteIndex", docId)
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
        loadNotes()
    }

    private fun loadNotes(){
        db.collection("users").document(userId).collection("notes")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                notes.clear()
                for (doc in result) {
                    val note = doc.toObject<Note>()
                    notes.add(Pair(doc.id, note))
                }
                adapter.notifyDataSetChanged()
            }
    }
}
