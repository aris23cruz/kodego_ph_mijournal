package com.kodego.mijournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kodego.mijournal.databinding.FragmentLogInBinding


class LogIn : Fragment() {
    lateinit var binding : FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}