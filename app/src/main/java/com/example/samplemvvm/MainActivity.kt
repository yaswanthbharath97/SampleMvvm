package com.example.samplemvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.samplemvvm.Adapter.NoteAdapter
import com.example.samplemvvm.Database.NoteDatabase
import com.example.samplemvvm.Models.Note

import com.example.samplemvvm.Models.NoteViewModel
import com.example.samplemvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NoteAdapter
    lateinit var selectedNote:Note

  //  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
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allnotes?.observe(this,{
                list ->
            list?.let{
                adapter.updateList(list)
            }
        })

        database = NoteDatabase.getDatabase(this)


    }

    private fun initUI() {

        binding.noteRecyclerview.setHasFixedSize(true)
        binding.noteRecyclerview.layoutManager=StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

        adapter= NoteAdapter(this,this)

        binding.noteRecyclerview.adapter=adapter

    }
}