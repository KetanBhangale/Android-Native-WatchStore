package com.example.watchstoreapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.watchstoreapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_full_screen.*

@AndroidEntryPoint
class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
        val imgPath = intent.getStringExtra("imgPath")
        Glide.with(this).load(imgPath).into(image)
        close.setOnClickListener {
            finish()
        }
    }
}