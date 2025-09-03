package com.bahy.notesapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object NotesStorage {
    private val gson = Gson()

    fun loadNotes(context: Context, userId: String): MutableList<Note> {
        val file = File(context.filesDir, "notes_${userId}.json")
        if (!file.exists()) return mutableListOf()

        val json = file.readText()
        val type = object : TypeToken<MutableList<Note>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun saveNotes(context: Context, userId: String, notes: List<Note>) {
        val file = File(context.filesDir, "notes_${userId}.json")
        val json = gson.toJson(notes)
        file.writeText(json)
    }
}
