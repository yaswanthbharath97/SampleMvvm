package com.example.samplemvvm.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplemvvm.Database.NoteDao
import com.example.samplemvvm.Database.NoteDatabase
import com.example.samplemvvm.Database.NoteRepository
import kotlinx.coroutines.launch

abstract class NoteViewModel(application: Application):AndroidViewModel(application) {

    private val repository:NoteRepository

    val allnotes :LiveData<List<Note>>

    init {
        val dao=NoteDatabase.getDatabase(application).getNoteDao()
        repository= NoteRepository(dao)
        allnotes=repository.allNotes
    }


    fun deleteNote(note: Note)=viewModelScope.launch {
        repository.delete(note)
    }



}