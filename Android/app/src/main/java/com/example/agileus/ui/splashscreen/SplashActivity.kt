package com.example.agileus.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.agileus.R
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoAceptar
import com.example.agileus.webservices.NetworkConnection
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, androidx.lifecycle.Observer { isConnected->

            if (isConnected){

                hideSystemUI()

                val intent = Intent(applicationContext, HomeActivity::class.java)

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                    finish()

                },3000)
            }
            else{
                val newFragment = DialogoAceptar(getString(R.string.nohayconexioninternet))
                newFragment.show(
                    (this).supportFragmentManager,
                    getString(R.string.logTareas)
                )
            }
        })



    }


    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}