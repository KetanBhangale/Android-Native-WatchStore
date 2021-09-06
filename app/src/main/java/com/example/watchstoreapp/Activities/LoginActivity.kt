package com.example.watchstoreapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.watchstoreapp.R
import com.example.watchstoreapp.databinding.ActivityLoginBinding
import com.example.watchstoreapp.model.User
import com.example.watchstoreapp.utils.SharedPreferenceManager
import com.example.watchstoreapp.viewModel.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val storeViewModel: StoreViewModel by viewModels()
    lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var userEmail:String
    lateinit var  emailInput:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferenceManager = SharedPreferenceManager(this)
        userEmail = sharedPreferenceManager.getUserEmail()
        Log.i("userEmail",userEmail)
        if (userEmail != ""){
            binding.loginForm.profileLL.visibility = View.VISIBLE
            binding.loginForm.etemail.visibility = View.GONE
            binding.loginForm.createnewac.visibility = View.GONE
            binding.loginForm.emailText.text = userEmail
        }else{
            binding.loginForm.profileLL.visibility = View.GONE
            binding.loginForm.etemail.visibility = View.VISIBLE
            binding.loginForm.createnewac.visibility = View.VISIBLE
        }
        binding.loginForm.createnewac.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.loginForm.btnlogin.setOnClickListener {
            Log.i("userEmail inside",""+userEmail)
            emailInput = if (userEmail ==""){
                binding.loginForm.etemail.text.toString()
            }else{
                userEmail
            }
            val password = binding.loginForm.mypass.text.toString()
            doLogin(emailInput, password)
        }
    }

    private fun doLogin(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            binding.loginForm.etemail.error = "Please Enter Registered Email"
            binding.loginForm.etemail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            binding.loginForm.mypass.error = "Please Enter Valid PIN"
            binding.loginForm.mypass.requestFocus()
            return
        }
        progressbar.visibility = View.VISIBLE
        storeViewModel.getUserDetails(email, password)
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            //if(storeViewModel.user !=null) {
                storeViewModel.user.observe(this@LoginActivity, Observer { user ->
                    if (user.email=="1") {
                        Log.i("Invalid User", "")
                        progressbar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Invalid User Details!!!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.i("User Data in Login", user.toString())
                        if (userEmail == ""){
                            sharedPreferenceManager.addUserData(user)
                        }
                        progressbar.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })


        }

    }
}