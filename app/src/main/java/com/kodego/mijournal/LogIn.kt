package com.kodego.mijournal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kodego.mijournal.databinding.FragmentLogInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider

const val REQUEST_CODE_SIGN_IN = 0

class LogIn : Fragment() {
    lateinit var binding : FragmentLogInBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        auth.currentUser?.let {

        }
        binding.btnLogin.setOnClickListener(){
            loginUser()
        }
        return binding.root

    }
    private fun loginUser(){
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
    private fun checkLoggedInState() {
        val user = auth.currentUser
        if (user == null){
            Toast.makeText(context,"You are not logged in", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context,"You are logged in", Toast.LENGTH_LONG).show()
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
        }
    }


}