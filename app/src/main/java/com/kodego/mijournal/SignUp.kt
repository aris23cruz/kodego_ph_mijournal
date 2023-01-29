package com.kodego.mijournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kodego.mijournal.databinding.FragmentSignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SignUp : Fragment() {

    lateinit var binding : FragmentSignUpBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        auth.currentUser?.let {

        }
        binding.btnSignup.setOnClickListener(){
            signupUser()
        }
        return binding.root
    }
    private fun signupUser() {
        val email = binding.etEmailSignup.text.toString()
        val password = binding.etPasswordSignup.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}