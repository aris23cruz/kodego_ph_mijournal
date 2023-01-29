package com.kodego.mijournal

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.mijournal.databinding.ActivityHomeBinding
import com.kodego.mijournal.databinding.AddDialogueBinding
import com.kodego.mijournal.databinding.RowItemBinding
import com.kodego.mijournal.databinding.UpdateDialogBinding


class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    lateinit var adapter : DiaryAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //data source
        val databaseHelper = DatabaseHelper(applicationContext)
        val dailyStory : MutableList<StoryModel> = databaseHelper.getAllData()
//        val dailyStory : MutableList<StoryModel> = mutableListOf<StoryModel>()
//        dailyStory.add(StoryModel(20,"title","date","story"))
        adapter = DiaryAdaptor(dailyStory)

        adapter.onStoryDelete = {item: StoryModel, position: Int ->
            val databaseHelper = DatabaseHelper(applicationContext)
            databaseHelper.deleteOne(item)

            adapter.storyModel.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        adapter.onDiaryClick = {
            val intent = Intent(this, StoryDetailActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("date",it.date)
            intent.putExtra("story",it.story)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.btnAddStory.setOnClickListener(){
            showAddDialogue()

        }
        adapter.onStoryUpdate = { text: StoryModel, position: Int ->
            showUpdateDialogue(text, position)
        }
    }

    private fun showUpdateDialogue(text: StoryModel, position: Int) {
        val dialog = Dialog(this)
        val binding : UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        val diaryUpdate = DatabaseHelper(applicationContext)
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
                adapter.notifyDataSetChanged()
                //update entry to database
                storyModel = StoryModel(-1, title, date, story)
                diaryUpdate.updateOne(storyModel, 0)
                dialog.dismiss()

                Toast.makeText(applicationContext,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(applicationContext,"Try again", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showAddDialogue() {
        val dialog = Dialog(this)
        val binding : AddDialogueBinding = AddDialogueBinding.inflate(layoutInflater)
        val diaryEntry = DatabaseHelper(applicationContext)
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

                Toast.makeText(applicationContext,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(applicationContext,"Try again", Toast.LENGTH_LONG).show()
            }
        }
    }
}