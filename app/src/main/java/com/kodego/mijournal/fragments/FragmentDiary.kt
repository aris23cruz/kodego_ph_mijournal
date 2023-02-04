package com.kodego.mijournal.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.mijournal.DatabaseHelper
import com.kodego.mijournal.DiaryAdaptor
import com.kodego.mijournal.StoryDetailActivity
import com.kodego.mijournal.StoryModel
import com.kodego.mijournal.databinding.AddDialogueBinding
import com.kodego.mijournal.databinding.FragmentDiaryBinding
import com.kodego.mijournal.databinding.UpdateDialogBinding


class FragmentDiary : Fragment() {

    lateinit var binding: FragmentDiaryBinding
    lateinit var adapter : DiaryAdaptor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryBinding.inflate(layoutInflater)
        //data source
        val databaseHelper = DatabaseHelper(requireContext())
        val dailyStory : MutableList<StoryModel> = databaseHelper.getAllData()

        adapter = DiaryAdaptor(dailyStory)

        adapter.onStoryDelete = { item: StoryModel, position: Int ->
            val databaseHelper = DatabaseHelper(requireContext())
            databaseHelper.deleteOne(item)

            adapter.storyModel.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        adapter.onDiaryClick = {
            val intent = Intent(requireContext(), StoryDetailActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("date",it.date)
            intent.putExtra("story",it.story)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.btnAddStory.setOnClickListener(){
            showAddDialogue()

        }
        adapter.onStoryUpdate = { text: StoryModel, position: Int, id:Int ->
            showUpdateDialogue(text, position,id)
        }
        return binding.root
    }
    private fun showUpdateDialogue(text: StoryModel, position: Int, id:Int) {
        val dialog = Dialog(requireContext())
        val binding : UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        val diaryUpdate = DatabaseHelper(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnDialogUpdate.setOnClickListener(){
            lateinit var storyModel: StoryModel
            try {
                val title = binding.etDialogTitle.text.toString()
                val date = binding.etDialogDate.text.toString()
                val story = binding.etDialogStory.text.toString()

                //update entry to recyclerview
                adapter.storyModel[position].title = title
                adapter.storyModel[position].date = date
                adapter.storyModel[position].story = story

                //update entry to database
                storyModel = StoryModel(0, title, date, story)
                diaryUpdate.updateOne(storyModel,id)
                adapter.notifyDataSetChanged()
                dialog.dismiss()

                Toast.makeText(context,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(context,"Try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddDialogue() {
        val dialog = Dialog(requireContext())
        val binding : AddDialogueBinding = AddDialogueBinding.inflate(layoutInflater)
        val diaryEntry = DatabaseHelper(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnAddDiary.setOnClickListener(){
            lateinit var storyModel: StoryModel
            try {
                val title = binding.etAddTitle.text.toString()
                val date = binding.etAddDate.text.toString()
                val story = binding.etAddStory.text.toString()

                storyModel = StoryModel(-1, title, date, story)
                //add new entry to recyclerview
                adapter.storyModel.add(storyModel)
                adapter.notifyDataSetChanged()
                //add new entry to database
                diaryEntry.addOne(storyModel)
                dialog.dismiss()

                Toast.makeText(context,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(context,"Try again", Toast.LENGTH_LONG).show()
            }
        }
    }

}