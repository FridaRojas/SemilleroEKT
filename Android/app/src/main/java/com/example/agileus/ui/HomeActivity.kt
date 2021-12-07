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
import com.example.agileus.databinding.ActivityHomeBinding
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser

/*class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


//        val navController = navHostFragment.navController
        //      findViewById<BottomNavigationView>(R.id.bottom_nav)
        //        .setupWithNavController(navController)

        //recuperarPublicaciones()
    }
}
