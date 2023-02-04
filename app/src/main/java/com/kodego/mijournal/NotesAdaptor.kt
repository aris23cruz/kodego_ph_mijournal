package com.kodego.mijournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.mijournal.databinding.RowItemNotesBinding

class NotesAdaptor (var notesModel: MutableList<NotesModel>): RecyclerView.Adapter<NotesAdaptor.NotesViewHolder>() {

    var onNotesDelete : ((NotesModel, Int) -> Unit)? = null

    inner class NotesViewHolder(var binding: RowItemNotesBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowItemNotesBinding.inflate(layoutInflater,parent,false)

        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesModel.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.binding.apply {
            tvNotesId.text = notesModel[position].id.toString()
            tvNotesTitle.text = notesModel[position].title
            tvNotesItem.text = notesModel[position].item
            btnNotesDelete.setOnClickListener(){
                onNotesDelete?.invoke(notesModel[position],position)
            }
        }
    }
}