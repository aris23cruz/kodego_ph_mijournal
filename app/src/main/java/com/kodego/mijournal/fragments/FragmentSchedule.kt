package com.kodego.mijournal.fragments


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.mijournal.DatabaseHelper
import com.kodego.mijournal.ScheduleAdaptor
import com.kodego.mijournal.ScheduleModel
import com.kodego.mijournal.databinding.AddEventDialogBinding
import com.kodego.mijournal.databinding.FragmentScheduleBinding
import com.kodego.mijournal.databinding.UpdateEventDialogBinding
import java.util.*


class FragmentSchedule : Fragment() {

    lateinit var binding: FragmentScheduleBinding
    lateinit var adapter: ScheduleAdaptor
    lateinit var dateTimePicker : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)
        val databaseHelper = DatabaseHelper(requireContext())
        val dailySchedule: MutableList<ScheduleModel> = databaseHelper.getAllDataSchedule()
        adapter = ScheduleAdaptor(dailySchedule)

        adapter.onDeleteEvent = { item : ScheduleModel, position: Int ->
            val databaseHelper = DatabaseHelper(requireContext())
            databaseHelper.deleteEventOne(item)
            adapter.scheduleModel.removeAt(position)
            adapter.notifyDataSetChanged()
        }
        binding.rvEventList.adapter = adapter
        binding.rvEventList.layoutManager = LinearLayoutManager(context)

        binding.btnNewEvent.setOnClickListener(){
            showAddEventDialog()
        }

        adapter.onUpdateEvent = { text: ScheduleModel, position: Int, id:Int ->
            showEventUpdateDialog(text, position, id)
        }
        return binding.root
    }

    private fun showAddEventDialog() {
        val dialog = Dialog(requireContext())
        val binding: AddEventDialogBinding = AddEventDialogBinding.inflate(layoutInflater)
        val eventEntry = DatabaseHelper(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnAddEventDialog.setOnClickListener(){
            lateinit var scheduleModel: ScheduleModel
            try {
//                Toast.makeText(context,dateTimePicker,Toast.LENGTH_LONG).show()
                val event = binding.etAddEventName.text.toString()
                val schedule = dateTimePicker

                scheduleModel = ScheduleModel(0,event, schedule)
                //add new entry to recyclerview
                adapter.scheduleModel.add(scheduleModel)
                adapter.notifyDataSetChanged()
                //add new entry to database
                eventEntry.addEventOne(scheduleModel)
                Toast.makeText(context,""+scheduleModel.schedule,Toast.LENGTH_LONG).show()
                dialog.dismiss()

                Toast.makeText(context,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(context,"Try again", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnAddCalendarPicker.setOnClickListener(){
            setAddDate()
        }

    }

    private fun setAddDate() {
        dateTimePicker = ""
        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)



        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->

                dateTimePicker = "${monthOfYear+1}/$dayOfMonth/$year $hour:$minute"

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true)
            timePickerDialog.show()
        },date, month, day)
        datePickerDialog.show()

    }

    private fun showEventUpdateDialog(text: ScheduleModel, position: Int, id:Int){
        val dialog = Dialog(requireContext())
        val binding : UpdateEventDialogBinding = UpdateEventDialogBinding.inflate(layoutInflater)
        val dailySchedule = DatabaseHelper(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnUpdateEventDialog.setOnClickListener(){
            lateinit var scheduleModel : ScheduleModel
            try {
                val event = binding.etUpdateEvent.text.toString()
                val schedule = dateTimePicker

                adapter.scheduleModel[position].event = event
                adapter.scheduleModel[position].schedule = schedule

                scheduleModel = ScheduleModel(0, event, schedule)
                dailySchedule.updateEventOne(scheduleModel,id)
                adapter.notifyDataSetChanged()
                dialog.dismiss()

                Toast.makeText(context,"Have a wonderful day", Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(context,"Try again", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnUpdateCalendar.setOnClickListener(){
            setUpdateDate(text, position, id)
        }

    }

    private fun setUpdateDate(text: ScheduleModel, position: Int, id:Int) {
        dateTimePicker = ""
        val calendar = Calendar.getInstance()
        val date = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->

                dateTimePicker = "${monthOfYear+1}/$dayOfMonth/$year $hour:$minute"

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true)
            timePickerDialog.show()
        }, date, month, day)
        datePickerDialog.show()
    }


}