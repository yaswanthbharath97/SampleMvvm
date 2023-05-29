package com.example.samplemvvm.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.samplemvvm.Models.Note
import com.example.samplemvvm.R
import kotlin.random.Random

class NoteAdapter(private val context: Context, val listener: NotesItemClickListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    private val NoteList = ArrayList<Note>()
    private val fullList= ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.listitem, parent, false)
        )
    }




    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val currentNoteList = NoteList[position]
        holder.title.text = currentNoteList.title
        holder.title.isSelected = true
        holder.notes_tv.text = currentNoteList.note
        holder.date.text = currentNoteList.date
        holder.date.isSelected = true

        holder.notes_view.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(),null))

        holder.notes_view.setOnClickListener{
            listener.onItemClicked(NoteList[holder.adapterPosition])
        }

        holder.notes_view.setOnLongClickListener{
            listener.onLongItemClicked(NoteList[holder.adapterPosition],holder.notes_view)
            true
        }
    }


    override fun getItemCount(): Int {
        return NoteList.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val notes_view = itemView.findViewById<CardView>(R.id.NoteCardView)
        val title = itemView.findViewById<TextView>(R.id.title_text)
        val notes_tv = itemView.findViewById<TextView>(R.id.text_note)
        val date = itemView.findViewById<TextView>(R.id.note_date)


    }


    private fun randomColor(): Int {
        val list_color=  ArrayList<Int>()
        list_color.add(R.color.Notecolor1)
        list_color.add(R.color.Notecolor2)
        list_color.add(R.color.Notecolor3)
        list_color.add(R.color.Notecolor4)
        list_color.add(R.color.Notecolor5)
        list_color.add(R.color.Notecolor6)
        
        val seed=System.currentTimeMillis().toInt()
        val randomIndex= Random(seed).nextInt(list_color.size)
        return list_color[randomIndex]

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:List<Note>)
    {
        fullList.clear()
        fullList.addAll(newList)

        NoteList.clear()
        NoteList.addAll(newList)

        notifyDataSetChanged()
    }



    @SuppressLint("NotifyDataSetChanged")
    fun filterList(search: String)
    {
        NoteList.clear()

        for (item in fullList)

        {
            if(item.title?.lowercase()?.contains(search.lowercase())==true||
                    item.note?.lowercase()?.contains(search.lowercase())==true)
            {
                NoteList.add(item)
            }

        }
        notifyDataSetChanged()
    }

    interface NotesItemClickListener
    {
        fun onItemClicked(note:Note)
        fun onLongItemClicked(note: Note, notesView: CardView)
    }
}