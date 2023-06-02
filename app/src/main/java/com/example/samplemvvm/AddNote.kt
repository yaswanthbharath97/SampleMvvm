package com.example.samplemvvm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.samplemvvm.Models.Note
import com.example.samplemvvm.databinding.ActivityAddNoteBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldnote: Note
    private var isUpdate = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldnote = intent.serializable<Note>("current_note") as Note
            binding.editTitle.setText(oldnote.title)
            binding.editNote.setText(oldnote.note)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.check.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val note_desc = binding.editNote.text.toString()

            if (title.isNotEmpty() || note_desc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                note = if (isUpdate) {
                    Note(oldnote.id, title, note_desc, formatter.format(Date()))
                } else {
                    Note(null, title, note_desc, formatter.format(Date()))
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNote, "Please enter notes", Toast.LENGTH_SHORT).show()
            }
        }


        binding.back.setOnClickListener {

           onBackPressedDispatcher.onBackPressed()

        }

    }


    @RequiresApi(Build.VERSION_CODES.P)
    @Suppress("DEPRECATION")
    inline fun <reified Note : Serializable> Intent.serializable(key: String): Note? {
        return getSerializableExtra(key) as? Note?
    }


}
