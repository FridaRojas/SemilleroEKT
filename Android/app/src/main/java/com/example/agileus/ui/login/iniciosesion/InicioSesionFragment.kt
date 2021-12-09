package com.example.agileus.ui.login.iniciosesion

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.config.MySharedPreferences.Companion.TOKEN_KEY
import com.example.agileus.databinding.InicioSesionFragmentBinding
import com.example.agileus.models.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Pattern
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.login.dialog.CerrarSesionDialog
import com.example.agileus.ui.login.dialog.DialogoListen

//, DialogoListen
class InicioSesionFragment : Fragment(){
    private var _binding: InicioSesionFragmentBinding? = null
    private val binding get() = _binding!!
    var trigger=0

    //commit

    companion object {
        var correoLogin : String=""
        var passwordLogin : String=""
        var status:Boolean =false
        var idUser:String = ""
        var rol:String = ""
        var idnombre:String = ""
        var idGrupo:String = ""
        var tokenAuth: String = ""
    }

    private lateinit var viewModel: InicioSesionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)
        _binding = InicioSesionFragmentBinding.inflate(inflater, container, false)

        val view: View = binding.root

        return view
    }

    @SuppressLint("FragmentBackPressedCallback")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)

        //appBar: AppBar( title: Text("App Bar without Back Button"), automaticallyImplyLeading: false, ),

        //AGREGADA para ocultar BottonNavigationView
        //val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        //navBar.isVisible = false

///////////////////////////////////////
        binding.btnLogin.setOnClickListener { validate()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsersByBoss()
    }

    private fun validate() {

        var result = arrayOf(validateEmail(), validatePassword())

        if (false in result) {
            return
        }
        val usuario = Users(correoLogin, passwordLogin, TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)
        binding.progressLoading.isVisible = true


        if (status) {
            //Log.d("Login", correoLogin)
            //Log.d("Login", passwordLogin)
            //Log.d("Login", idUser)
            trigger = 0
            Toast.makeText(activity, "Usuario Encontrado", Toast.LENGTH_SHORT).show()
            if(correoLogin != "rogelioL@gmail.com")
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            else
            {
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_buzonFragment2) }
        }

        if (!status)
        {
            if(trigger == 0 )
            {Toast.makeText(activity, "Presiona de Nuevo para Confirmar", Toast.LENGTH_SHORT).show()
            }
            if (trigger >1 && !status) {
                Toast.makeText(activity, "Usuario No Encontrado", Toast.LENGTH_SHORT).show()

                if(trigger >3 )
                {
                    Toast.makeText(activity, "Demasiados Intentos Fallidos, Cerrando Applicación", Toast.LENGTH_LONG).show()
                        //val newFragment = CerrarSesionDialog(this)
                        //activity?.supportFragmentManager?.let { it -> newFragment.show(it, "Destino") }
                    Handler().postDelayed({
                        activity?.finish()
                    }, 3000)

                }

            }
            trigger++

        }
    }

    private fun validateEmail(): Boolean {
        val correo = binding.username.text?.toString()
        return if (correo!!.isEmpty()) {
            binding.username.error = "El campo no puede estar vacío"
            false
        }
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.username.error = "Por favor, ingresa un correo válido"
            false
        } else {
            binding.username.error = null
            correoLogin = correo
            true
        }
    }
    private fun validatePassword() : Boolean {
        val password = binding.password.text?.toString()
        // VALIDAR PASSWORD CON CARACTERES ESPECIALES
        val passwordRegex = Pattern.compile(
            "^" +   // declaración de inicio
                    "(?=.*[0-9])" +                     //Contener al menos un dígito
                    //"(?=.*[a-z])" +                     //Contener al menos ua letra minúscula
                    //"(?=.*[A-Z])" +                     //Contener al menos una letra mayúscula
                    //"(?=.*[@#$%^&+=])" +                //Contener al menos un caracter especial
                    "(?=\\S+$)" +                       //No tener espacios vacios o blancos
                    ".{3,}" +                           //Contener al menos 4 caracteres
                    "$"      // cierre
        )
        return if (password!!.isEmpty()) {
            binding.password.error = "El campo no puede estar vacío"
            false
        } else if (!passwordRegex.matcher(password).matches()) {
            binding.password.error = "La contraseña es demasiado débil"
            false
        } else {
            binding.password.error = null
            passwordLogin = password
            true
        }

    }

    /*override fun siDisparar(motivo: String) {
        findNavController().navigate(R.id.inicioSesionFragment)
        //Toast.makeText(activity, motivo, Toast.LENGTH_SHORT).show()
    }

    override fun noDisparar(motivo: String) {
        Toast.makeText(activity, motivo, Toast.LENGTH_SHORT).show()

    }

     */
    ////////////////////////////////////
}
