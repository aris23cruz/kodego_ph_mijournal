package com.kodego.mijournal.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.mijournal.DatabaseHelper
import com.kodego.mijournal.NotesAdaptor
import com.kodego.mijournal.NotesModel
import com.kodego.mijournal.databinding.AddNotesDialogBinding
import com.kodego.mijournal.databinding.FragmentNotesBinding


class FragmentNotes : Fragment() {

    lateinit var binding: FragmentNotesBinding
    lateinit var adaptor: NotesAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(layoutInflater)
        val databaseHelper = DatabaseHelper(requireContext())
        val dailyNotes : MutableList<NotesModel> = databaseHelper.getAllDataNotes()
        adaptor = NotesAdaptor(dailyNotes)

        adaptor.onNotesDelete = { item : NotesModel, position : Int ->
            val databaseHelper = DatabaseHelper(requireContext())
            databaseHelper.deleteNotesOne(item)
            adaptor.notesModel.removeAt(position)
            adaptor.notifyDataSetChanged()
        }
        binding.rvNotes.adapter = adaptor
        binding.rvNotes.layoutManager = GridLayoutManager(context,2)

        binding.flabAddNotes.setOnClickListener(){
            showAddNotesDialog()
        }
        return binding.root
    }

    private fun showAddNotesDialog() {
        val dialog = Dialog(requireContext())
        val binding: AddNotesDialogBinding = AddNotesDialogBinding.inflate(layoutInflater)
        val notesEntry = DatabaseHelper(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnAddNotes.setOnClickListener(){
            lateinit var notesModel: NotesModel
            try {
                val title = binding.etNotesTitle.text.toString()
                val item = binding.etNotesItem.text.toString()

                notesModel = NotesModel(0,title, item)
                //add new entry to recyclerview
                adaptor.notesModel.add(notesModel)
                adaptor.notifyDataSetChanged()
                //add new entry to database
                notesEntry.addNOtesOne(notesModel)
                dialog.dismiss()

                Toast.makeText(context,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(context,"Try again", Toast.LENGTH_LONG).show()
            }
        }
    }
}