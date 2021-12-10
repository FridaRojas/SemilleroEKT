package com.example.agileus.ui.login.iniciosesion

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock.sleep
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
import com.example.agileus.ui.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Pattern

//, DialogoListen
class InicioSesionFragment : Fragment(){
    private var _binding: InicioSesionFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        var correoLogin : String=""
        var passwordLogin : String=""
        var status:Boolean=false
        var id:String = " "
        var idUser:String = ""
        var correo:String =" "
        var fechaInicio:String = " "
        var fechaTermino:String = " "
        var numeroEmpleado:String = " "
        var nombre : String = " "
        var password:String = " "
        var nombreRol:String = " "
        var opcionales:String = " "
        var token:String = " "
        var telefono:String = " "
        var statusActivo:String = " "
        var curp:String = " "
        var rfc:String = " "
        var idGrupo:String = ""
        var idsuperiorInmediato:String = " "
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
        //OCULTAR BOTÓN ATRÁS EN ONACTIVITYCREATED
        (activity as HomeActivity).ocultarBtnAtras()

        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)

        //AGREGADA para ocultar BottonNavigationView
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false

///////////////////////////////////////
        binding.btnLogin.setOnClickListener { validate() }
        // observer se dispara cuando finalice el servicio
        viewModel.inicioExitoso.observe(viewLifecycleOwner, {response ->
            //Log.d("respuesta inicio ", viewModel.inicioExitoso.toString())
            var x = response
            if(x)
            {
                //Log.d("xdata",x.toString())
             //   Toast.makeText(activity, "Inicio", Toast.LENGTH_SHORT).show()
            }
             else {
                //Log.d("xdata",x.toString())
            }

            //}
        })



    }

    private fun validate() {

        var progressBar = binding.progressLoading
        progressBar.visibility=View.VISIBLE
        binding.btnLogin.isEnabled=false


        var result = arrayOf(validateEmail(), validatePassword())

        if (false in result) {
            progressBar.visibility=View.INVISIBLE
            binding.btnLogin.isEnabled=true
            return
        }
        val usuario = Users(correoLogin, passwordLogin, TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)


        sleep(1000)

        if (status) {
            //Toast.makeText(activity, "Usuario Encontrado", Toast.LENGTH_SHORT).show()
            if(correoLogin != "rogelioL@gmail.com")
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            else
            {
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_buzonFragment2) }
             }

        else {
            startTimeCounter()
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
   //Barra de carga para inicio de login
    fun startTimeCounter() {
        var counter=0
        val progressBar = binding.progressLoading
        progressBar.visibility=View.VISIBLE
        object : CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.setProgress(counter++)//counter++
            }
            override fun onFinish() {
                Toast.makeText(activity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                progressBar.visibility=View.INVISIBLE
                binding.btnLogin.isEnabled=true


            }
        }.start()
    }

}