package com.kodego.mijournal.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kodego.mijournal.fragments.FragmentDiary
import com.kodego.mijournal.fragments.FragmentNotes
import com.kodego.mijournal.fragments.FragmentSchedule

internal class MyAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                FragmentDiary()
            }
            1 -> {
                FragmentSchedule()
            }
            2 -> {
                FragmentNotes()
            }
            else -> getItem(position)
        }
    }
}