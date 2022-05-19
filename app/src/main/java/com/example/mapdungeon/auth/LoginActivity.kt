package com.example.mapdungeon.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapdungeon.databinding.ActivityLoginBinding


class LoginActivity: AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
    }
}