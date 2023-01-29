package com.kodego.mijournal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kodego.mijournal.databinding.ActivityMainBinding
import com.kodego.mijournal.databinding.FragmentLogInBinding
import com.kodego.mijournal.databinding.FragmentSignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
//const val REQUEST_CODE_SIGN_IN = 0
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var loginBinding: FragmentLogInBinding
    private lateinit var signUpBinding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        loginBinding = FragmentLogInBinding.inflate(layoutInflater)
        signUpBinding = FragmentSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        auth.currentUser?.let {

        }
        val login = LogIn()
        val signup = SignUp()

        supportFragmentManager.beginTransaction().apply{
            replace(binding.frameLayout.id, login)
            commit()
        }

        binding.btnFragLogin.setOnClickListener(){
            supportFragmentManager.beginTransaction().apply{
                replace(binding.frameLayout.id, login)
                commit()
            }
        }
        binding.btnFragSignup.setOnClickListener(){
            supportFragmentManager.beginTransaction().apply{
                replace(binding.frameLayout.id, signup)
                commit()
            }
        }

        loginBinding.btnLogin.setOnClickListener(){
            loginUser()
        }
        
        signUpBinding.btnSignup.setOnClickListener(){
            signupUser()
        }
        
        signUpBinding.btnGoogleIcon.setOnClickListener(){
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, options)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Successfully logged in", Toast.LENGTH_LONG).show()
                }
            } catch(e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun signupUser() {
        val email = signUpBinding.etEmailSignup.text.toString()
        val password = signUpBinding.etPasswordSignup.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        val user = auth.currentUser
        if (user == null){
            Toast.makeText(this@MainActivity,"You are not logged in", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@MainActivity,"You are logged in", Toast.LENGTH_LONG).show()
        }
    }

    private fun loginUser(){
        val email = loginBinding.etEmailLogin.text.toString()
        val password = loginBinding.etPasswordLogin.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}