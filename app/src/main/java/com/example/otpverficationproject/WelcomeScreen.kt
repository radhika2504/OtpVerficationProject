package com.example.otpverficationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val welcomeLoginButton: Button = findViewById (R.id.welcomeLoginButton)
        welcomeLoginButton.setOnClickListener{
            val intent=Intent(this, MessageScreen::class.java)
            startActivity(intent)
        }
    }
}