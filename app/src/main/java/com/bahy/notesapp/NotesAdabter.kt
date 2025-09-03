package com.bahy.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bahy.notesapp.databinding.ActivityNoteItemBinding

class NotesAdapter(private val notes: MutableList<Note>, private val userId: String, private val onNoteClick: (Note, Int) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(val binding: ActivityNoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ActivityNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.noteTitleTextView.text = note.title
        holder.binding.noteContentTextView.text = note.content

        holder.itemView.setOnClickListener {
            onNoteClick(note, position)
        }

        holder.binding.menuButton.setOnClickListener {
            val popup = PopupMenu(holder.itemView.context, holder.binding.menuButton)
            popup.menuInflater.inflate(R.menu.note_item_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {

                        notes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, notes.size)
                        NotesStorage.saveNotes(holder.itemView.context, userId, notes)
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
