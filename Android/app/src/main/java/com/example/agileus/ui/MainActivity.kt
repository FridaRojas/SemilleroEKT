package com.example.agileus.ui

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.agileus.R
import com.example.agileus.databinding.ActivityMainBinding
import com.example.agileus.databinding.InicioSesionFragmentBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)


        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_activity_home))


    }

        override fun onSupportNavigateUp(): Boolean {
            val navController: NavController =
                findNavController(R.id.nav_host_fragment_activity_home)
            return navController.navigateUp() || super.onSupportNavigateUp()
        }
    }




