package com.kodego.mijournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kodego.mijournal.databinding.RowItemScheduleBinding

class ScheduleAdaptor(var scheduleModel: MutableList<ScheduleModel>): RecyclerView.Adapter<ScheduleAdaptor.ScheduleViewHolder>() {

    var onUpdateEvent : ((ScheduleModel, Int, Int)-> Unit)? = null
    var onDeleteEvent : ((ScheduleModel, Int)-> Unit)? = null

    inner class ScheduleViewHolder(var binding: RowItemScheduleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowItemScheduleBinding.inflate(layoutInflater, parent, false)

        return ScheduleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return scheduleModel.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.binding.apply {
            tvEventName.text = scheduleModel[position].event
            tvSchedule.text = scheduleModel[position].schedule
            tvIdSched.text = scheduleModel[position].id.toString()
            btnUpdateEvent.setOnClickListener(){
                onUpdateEvent?.invoke(scheduleModel[position], position, scheduleModel[position].id)
            }
            btnDeleteEvent.setOnClickListener(){
                onDeleteEvent?.invoke(scheduleModel[position],position)
            }
        }
    }
}