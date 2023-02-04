package com.kodego.mijournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.mijournal.databinding.RowItemBinding


class DiaryAdaptor (var storyModel: MutableList<StoryModel>): RecyclerView.Adapter<DiaryAdaptor.StoryViewHolder>(){

    var onStoryDelete : ((StoryModel, Int) -> Unit)? = null
    var onStoryUpdate : ((StoryModel,Int,Int)-> Unit)? = null
    var onDiaryClick : ((StoryModel)-> Unit)? = null

    inner class StoryViewHolder(var binding: RowItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(layoutInflater,parent,false)

        return StoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyModel.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.binding.apply {
            tvId.text = storyModel[position].id.toString()
            tvTitle.text = storyModel[position].title
            tvDate.text = storyModel[position].date
            tvStory.text = storyModel[position].story
            btnUpdate.setOnClickListener(){
                onStoryUpdate?.invoke(storyModel[position],position, storyModel[position].id)
            }
            btnDelete.setOnClickListener(){
                onStoryDelete?.invoke(storyModel[position],position)
            }

        }
        holder.itemView.setOnClickListener(){
            onDiaryClick?.invoke(storyModel[position])
        }
    }
}