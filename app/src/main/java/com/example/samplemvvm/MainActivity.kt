package com.example.samplemvvm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.samplemvvm.Adapter.NoteAdapter
import com.example.samplemvvm.Database.NoteDatabase
import com.example.samplemvvm.Models.Note
import com.example.samplemvvm.Models.NoteViewModel
import com.example.samplemvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NoteAdapter.NotesItemClickListener,PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NoteAdapter
    lateinit var selectedNote: Note


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode==Activity.RESULT_OK){
            val note=result.data?.getSerializableExtra("note") as? Note
            if(note != null){
                viewModel.updateNote(note)
            }

        }
    }




    //  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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)


        viewModel.allnotes?.observe(this, { list ->
            list?.let {
                adapter.updateList(list)
            }
        })

        database = NoteDatabase.getDatabase(this)


    }

    private fun initUI() {

        binding.noteRecyclerview.setHasFixedSize(true)
        binding.noteRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NoteAdapter(this, this)
        binding.noteRecyclerview.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }

            }
        binding.addNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
        val intent=Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)

    }

    override fun onLongItemClicked(note: Note, notesView: CardView) {
        selectedNote=note
        popUpDisplay(notesView)

    }

    private fun popUpDisplay(notesView: CardView) {
        val popup=PopupMenu(this,notesView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_menu)

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
      if(item?.itemId==R.id.delete_note)
      {
          viewModel.deleteNote(selectedNote)
          return true
      }
        return false
    }
}

