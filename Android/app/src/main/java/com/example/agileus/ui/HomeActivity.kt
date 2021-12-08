package com.example.agileus.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.databinding.ActivityHomeBinding
import com.example.agileus.ui.login.ui.login.InicioSesionFragment
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.ui.login.InicioSesionViewModel.Companion.usersByBoss
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoTareaCreadaExitosamente

/*class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Todo Login
        val token = InitialApplication.preferenciasGlobal.recuperarToken()
        Log.d("token", token)
        //Todo Login
        InitialApplication.preferenciasGlobal.recuperarToken()

        val navView: BottomNavigationView = binding.navView
        //binding.navView.visibility= View.INVISIBLE

            //

            val navController = findNavController(R.id.nav_host_fragment_activity_home)
            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.reporteMensajesFragment
                )
            )



            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            //        val navController = navHostFragment.navController
            //      findViewById<BottomNavigationView>(R.id.bottom_nav)
            //        .setupWithNavController(navController)

            //recuperarPublicaciones()
        }
    }

 */

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Login", InicioSesionFragment.correoLogin)
        Log.d("Login", InicioSesionFragment.passwordLogin)
        Log.d("Login", idUser)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.reporteMensajesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Ocultar navegación
        findViewById<BottomNavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.formularioCrearTareasFragment -> hideBottomNav(navView)
                R.id.detalleNivelAltoFragment -> hideBottomNav(navView)
                else -> showBottomNav(navView)
            }
        }
    }

    private fun showBottomNav(nav_view:BottomNavigationView) {
        nav_view.visibility = View.VISIBLE
    }

    private fun hideBottomNav(nav_view:BottomNavigationView) {
        nav_view.visibility = View.GONE

    }
//        val navController = navHostFragment.navController
        //      findViewById<BottomNavigationView>(R.id.bottom_nav)
        //        .setupWithNavController(navController)

        //recuperarPublicaciones()
    }
}
