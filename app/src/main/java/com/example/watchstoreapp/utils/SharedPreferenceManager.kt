package com.example.watchstoreapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.watchstoreapp.model.User


class SharedPreferenceManager(context: Context) {

    var sharedpreferences: SharedPreferences =  context.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE)

    fun addUserData(user: User){
        try {
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putString("userName", user.name)
            editor.putString("userEmail", user.email)
            editor.putString("userMobile", user.mobile)
            editor.commit()
        }catch (e:Exception){
            Log.i("Error", e.printStackTrace().toString())
        }
    }

    fun getUserEmail(): String{
        return sharedpreferences.getString("userEmail","")!!
    }

    fun getUserData():Array<String>{
        val name = sharedpreferences.getString("userName","")!!
        val email = sharedpreferences.getString("userEmail","")!!
        val mobile = sharedpreferences.getString("userMobile","")!!
        return arrayOf(name, email, mobile)
    }
}