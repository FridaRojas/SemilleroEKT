package com.example.agileus.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.userServiceLog
import com.example.agileus.databinding.ActivityLoginBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.model.User
import com.example.agileus.ui.login.ui.login.services.GetUserDao
import com.example.agileus.webservices.apis.UserServiceApi
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
