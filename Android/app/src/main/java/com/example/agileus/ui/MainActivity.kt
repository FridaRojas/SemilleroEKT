package com.example.agileus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.agileus.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment_activity_home))



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController:NavController=findNavController(R.id.nav_host_fragment_activity_home)
        return  navController.navigateUp() || super.onSupportNavigateUp()
    }


}


