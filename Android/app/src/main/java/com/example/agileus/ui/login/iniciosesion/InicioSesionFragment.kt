package com.example.agileus.ui.login.iniciosesion

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.config.MySharedPreferences.Companion.TOKEN_KEY
import com.example.agileus.databinding.InicioSesionFragmentBinding
import com.example.agileus.models.Data
import com.example.agileus.models.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Pattern

//, DialogoListen
class InicioSesionFragment : Fragment(){
    private var _binding: InicioSesionFragmentBinding? = null
    private val binding get() = _binding!!
    var dataLogin = Data()
    //var trigger=0

    var idUsuario = ""
    var correoSession = ""
    var passwordSession = ""

    companion object {
        var status:Boolean =false
        var idUser:String = ""
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsersByBoss()

        binding.btnLogin.setOnClickListener {
            //validate()
            var result = arrayOf(validateEmail(), validatePassword())
            correoSession = binding.email.text.toString()
            passwordSession = binding.password.text.toString()

            if(false in result){
                Toast.makeText(activity, "Correo y/o contraseña incorrecta", Toast.LENGTH_SHORT).show()
            }else{
                val usuario = Users(correoSession, passwordSession, TOKEN_KEY)
                viewModel.recuperarLogueo(usuario)
                if (status) {
                    Log.d("Login", correoSession)
                    Log.d("Login", passwordSession)
                    Log.d("Login", idUsuario)
                    //Toast.makeText(activity, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                    var nombre = preferenciasGlobal.recuperarNombreSesion()
                    Toast.makeText(activity, "$nombre", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Login", "Usuario no encontrado")
                }
            }
        }
    }

    /*
    private fun validate() {
        var result = arrayOf(validateEmail(), validatePassword())

        if (false in result) {
            return
        }
        val usuario = Users(password, passwordLogin, TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)
        //binding.progressLoading.isVisible = true

        if (status) {
            Log.d("Login", correoLogin)
            Log.d("Login", passwordLogin)
            Log.d("Login", idUser)

            findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

            //trigger = 0
            //Toast.makeText(activity, "Usuario Encontrado", Toast.LENGTH_SHORT).show()

            if(correoLogin != "rogelioL@gmail.com")
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            else {
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_buzonFragment2) }
             }
        else {
                Toast.makeText(activity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }*/

    private fun validateEmail(): Boolean {
        val correo = binding.email.text?.toString()
        return if (correo!!.isEmpty()) {
            binding.email.error = "El campo no puede estar vacío"
            false
        }
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.email.error = "Por favor, ingresa un correo válido"
            false
        } else {
            binding.email.error = null
            correoSession = correo
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
            binding.password.error = "La contraseña es incorrecta"
            false
        } else {
            binding.password.error = null
            passwordSession = password
            true
        }

    }

}
