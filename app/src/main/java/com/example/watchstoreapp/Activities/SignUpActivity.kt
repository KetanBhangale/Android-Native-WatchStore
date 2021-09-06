package com.example.watchstoreapp.Activities

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.watchstoreapp.databinding.ActivitySignUpBinding
import com.example.watchstoreapp.model.User
import com.example.watchstoreapp.viewModel.StoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val storeViewModel: StoreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupForm.gotoLogin.setOnClickListener {
           finish()
        }
        binding.signupForm.buttonAcount.setOnClickListener {
            val name = binding.signupForm.editName.text.toString()
            val email = binding.signupForm.editEmail.text.toString()
            val mobile = binding.signupForm.editMobile.text.toString()
            val pass = binding.signupForm.editPass.text.toString()
            createNewAcount(name, email, mobile, pass)
        }

    }
    private fun createNewAcount(name: String, email: String, mobile:String, pass: String) {
        if (TextUtils.isEmpty(name)) {
            binding.signupForm.editName.error = "Please Enter Name"
            binding.signupForm.editName.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            binding.signupForm.editEmail.error = "Please Enter Email"
            binding.signupForm.editEmail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.signupForm.editMobile.error = "Please Enter Mobile"
            binding.signupForm.editMobile.requestFocus()
            return
        }
        if (TextUtils.isEmpty(pass)) {
            binding.signupForm.editPass.error = "Please Enter PIN"
            binding.signupForm.editPass.requestFocus()
            return
        }

        val user = User("$mobile",name, email,pass, mobile)
        storeViewModel.addUser(user)
        GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            showDialog("Registration Completed. Go to Login Page.")
        }


    }

    private fun showDialog(message:String){
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Watch Store App")
                setMessage(message)
                setPositiveButton("Login",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        finish()
                    })

            }
            builder.create()
        }
        alertDialog?.show()
    }
}