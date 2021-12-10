package com.example.agileus.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Log.d("Login", InicioSesionFragment.correoLogin)
        //Log.d("Login", InicioSesionFragment.passwordLogin)
        // Log.d("Login", idUser)

        //Token Notificaciones
        val token = InitialApplication.preferenciasGlobal.recuperarToken()
        Log.d("token", token)


        //Navegación
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.reporteMensajesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        findViewById<BottomNavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        //Ocultar BottomNavigationBar en pantallas específicas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.formularioCrearTareasFragment -> hideBottomNav(navView)
                R.id.detalleNivelAltoFragment -> hideBottomNav(navView)
                else -> showBottomNav(navView)
            }
        }

    }

    private fun showBottomNav(nav_view: BottomNavigationView) {
        nav_view.visibility = View.VISIBLE
    }

    private fun hideBottomNav(nav_view: BottomNavigationView) {
        nav_view.visibility = View.GONE
    }

}

