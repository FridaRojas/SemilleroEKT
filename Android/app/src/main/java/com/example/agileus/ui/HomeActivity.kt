package com.example.agileus.ui

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.os.SystemClock.sleep
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.databinding.ActivityHomeBinding
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationFragment

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var fragmentSeleccionado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //sleep(3000)


    //Token Notificaciones
        val token = InitialApplication.preferenciasToken.recuperarToken()
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


        if(preferenciasGlobal.validaSesionIniciada()){
           // Toast.makeText(applicationContext, "${preferenciasGlobal.validaSesionIniciada()}", Toast.LENGTH_SHORT).show()
        }


        //Ocultar BottomNavigationBar en pantallas específicas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.formularioCrearTareasFragment -> hideBottomNav(navView)
                R.id.detalleNivelAltoFragment -> hideBottomNav(navView)
                R.id.listContactsFragment -> hideBottomNav(navView)
                R.id.userConversationFragment -> hideBottomNav(navView)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(fragmentSeleccionado){
            getString(R.string.fragmentSeleccionadoCrearTarea) -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.navigation_dashboard)
            }
            getString(R.string.fragmentSeleccionadoDetallesTarea) -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.navigation_dashboard)
            }
            "Contactos" -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.navigation_home)
            }
           R.string.chats_name.toString()-> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.navigation_home)
            }
            R.string.contacts_name.toString() -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.listContactsFragment)
            }
            getString(R.string.taskReportFragment) -> {
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_home).navigate(R.id.reporteMensajesFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //Ocultar flecha atras del inicio de sesión
    fun ocultarBtnAtras() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}

