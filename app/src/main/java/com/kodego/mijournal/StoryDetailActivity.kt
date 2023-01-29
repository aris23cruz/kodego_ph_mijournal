package com.kodego.mijournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kodego.mijournal.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityStoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title: String? = intent.getStringExtra("title")
        val date: String? = intent.getStringExtra("date")
        val story: String? = intent.getStringExtra("story")

        binding.tvTitle2.text = title
        binding.tvStory2.text = story
        binding.tvDate2.text = date
    }
}