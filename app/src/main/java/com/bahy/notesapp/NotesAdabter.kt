package com.bahy.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bahy.notesapp.databinding.ActivityNoteItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesAdapter(
    private val notes: MutableList<Pair<String, Note>>,
    private val onNoteClick: (String, Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(val binding: ActivityNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ActivityNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val (docId, note) = notes[position]
        holder.binding.noteTitleTextView.text = note.title
        holder.binding.noteContentTextView.text = note.content

        holder.itemView.setOnClickListener {
            onNoteClick(docId, note)
        }

        holder.binding.menuButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.binding.menuButton)
            popup.menuInflater.inflate(R.menu.note_item_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {
                        val db = FirebaseFirestore.getInstance()
                        val userId = FirebaseAuth.getInstance().currentUser!!.uid

                        db.collection("users").document(userId)
                            .collection("notes").document(docId)
                            .delete()

                        notes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, notes.size)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

    }

    override fun getItemCount(): Int = notes.size
}
