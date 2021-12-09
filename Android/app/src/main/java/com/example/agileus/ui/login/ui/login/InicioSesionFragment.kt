package com.example.agileus.ui.login.ui.login

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
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.login.data.model.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.regex.Pattern


class InicioSesionFragment : Fragment() {
    private var _binding: InicioSesionFragmentBinding? = null
    private val binding get() = _binding!!
     var trigger=0


    companion object {

          var correoLogin : String=""
          var passwordLogin : String=""
          var status:Boolean=false
          var idUser:String=""
          var token:String=""
          var tokenPush:String=""
          var Rol:String = ""
          var userName:String=""
    }

    private lateinit var viewModel: InicioSesionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)
        _binding = InicioSesionFragmentBinding.inflate(inflater, container, false)

        val view: View = binding.root

        //Instanciar shared preferences

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)
        //AGREGADA para ocultar BottonNavigationView
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false
/////////////////NUEVO AGREGUE//////////////////////
        binding.btnLogin.setOnClickListener {
            validate()
        }
    }

    private fun validate() {

        var result = arrayOf(validateEmail(), validatePassword())
       // Log.d("Login", correoLogin)
       // Log.d("Login", passwordLogin)
        if (false in result) {
            return
        }
        val usuario = Users(correoLogin, passwordLogin, TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)

        if (status) {
            trigger = 0

            Toast.makeText(activity, "Usuario Encontrado ", Toast.LENGTH_SHORT).show()

            if(Rol !=  "BROADCAST" )
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            else{
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_buzonFragment)
            }
        }

        if (!status) {
            if (trigger == 0) {
                Toast.makeText(activity, "Presiona de nuevo para confirmar", Toast.LENGTH_SHORT)
                    .show()
            }
            if (trigger > 1 && !status) {
                Toast.makeText(activity, "Usuario No Encontrado", Toast.LENGTH_SHORT).show()

                /*
                if(trigger >3 )
                    {
                        Toast.makeText(activity, "Demasiado Intentos Fallidos, Cerrando Applicación", Toast.LENGTH_LONG).show()

                        Handler().postDelayed({
                            activity?.finish()
                        }, 3000)


                }

            }
            trigger++
*/
            }
        }
    }

    private fun validateEmail(): Boolean {
        val correo = binding.username.text?.toString()
        return if (correo!!.isEmpty()) {
            binding.username.error = "El campo no puede estar vacío"
            false
        }
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.username.error = "Por favor, ingresa un correo valido"
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
    /////////////////NUEVO AGREGUE///////////////////7
}

///////////////////////////////////////////////////////
        //if (preferenciasGlobal.validaSesionIniciada()){
    //    cambiarDeActivividad() }

        /*binding.btnLogin.setOnClickListener {
            val correo = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            //progresbar
            val visibility = if (binding.progressLoading.visibility == View.VISIBLE) {
                View.VISIBLE
            } else
                View.INVISIBLE
            binding.progressLoading.visibility = visibility

            if (correo.isEmpty()) {
                //preferenciasGlobal.iniciarSesion(correo, password, true)
                binding.username.error = "Se requiere ingresar un correo valido"
                binding.username.requestFocus()
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                //preferenciasGlobal.iniciarSesion(password, password, true)
                binding.password.error = "Se requiere ingresar una contraseña valida"
                binding.password.requestFocus()
                //SHARED
                return@setOnClickListener
            } else {
                /*if (correo.isNotEmpty() && password.isNotEmpty()) {
                    TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo)
                        .matches() && password!!.length > 6
                    Toast.makeText(activity, "Inicio Exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                 */
                val usuario = Users(correo, password.toInt(), TOKEN_KEY)
                viewModel.recuperarLogueo(usuario)
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            }
        }
    }
    }
         */
//////////////////////////////////////////////////


















//////////////////////////////////////////////////////////////////////////////////////////77
            /*val usuario = Users(correo, password.toInt(), TOKEN_KEY)
            viewModel.recuperarLogueo(usuario)
            findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home) */


    //COMENTAR para poner shared
    /*    binding.btnLogin.setOnClickListener {

            val correo = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            //progresbar
            val visibility = if (binding.progressLoading.visibility == View.VISIBLE) {
                View.VISIBLE
            } else
                View.INVISIBLE
            binding.progressLoading.visibility = visibility

            //SHARED PREFERENCS CONSULTAR


            if (correo.isEmpty()) {
                //preferenciasGlobal.iniciarSesion(correo, password, true)
                binding.username.error = "Se requiere ingresar un correo valido"
                binding.username.requestFocus()
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                //preferenciasGlobal.iniciarSesion(password, password, true)
                binding.password.error = "Se requiere ingresar una contraseña valida"
                binding.password.requestFocus()
                //SHARED
                return@setOnClickListener
            } else {
                /*if (correo.isNotEmpty() && password.isNotEmpty()) {
                    TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo)
                        .matches() && password!!.length > 6
                    Toast.makeText(activity, "Inicio Exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                 */
                val usuario = Users(id, correo, password.toInt(), TOKEN_KEY)
                viewModel.recuperarLogueo(usuario)
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
     */

///////////////////////////////////////////////////////7
        //login con shared
    /*    binding.btnLogin.setOnClickListener {
            var correo = requireView().findViewById<EditText>(R.id.username).text.toString()
            var password = requireView().findViewById<EditText>(R.id.password).text.toString()
        }
    }
    if InicioSesionViewModel.InicioSesionFragment(correo, password)
    {
        findNavController().navigate(R.id.navigation_home)
    }
}
     */







         // if (correo.isNotEmpty() && password.isNotEmpty()) {
           //     Toast.makeText(activity, "NO EXISTES", Toast.LENGTH_SHORT).show()
                /*if (correo.isNotEmpty() && password.isNotEmpty()) {
                    TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo)
                        .matches() && password!!.length > 6
                    Toast.makeText(activity, "Inicio Exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                 */

 /*    viewModel.inicioExitoso.observe(viewLifecycleOwner, Observer{
            if (it) {

                Toast.makeText(activity, "Exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_inicioSesionFragment_to_navigation_home)
            } else {

                Toast.makeText(activity, "Fallido", Toast.LENGTH_SHORT).show()
            }
        })

    }

     */


 /*      private fun goToLogin() {

        val correo = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        val usuario= Users(correo,password.toInt(), TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)

        }

 */


//////////////////////////////////////////////
/*
            val correo = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (correo.isEmpty()) {
                binding.username.error = "Se requiere ingresar un correo"
                binding.username.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.password.error = "Se requiere ingresar una contraseña"
                binding.password.requestFocus()
                return@setOnClickListener
            } else {

                /*if (correo.isNotEmpty() && password.isNotEmpty()) {
                    TextUtils.isEmpty(correo) && Patterns.EMAIL_ADDRESS.matcher(correo)
                        .matches() && password!!.length > 6
                    Toast.makeText(activity, "Inicio Exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                 */
                val usuario = Users(correo, password.toInt(), TOKEN_KEY)
                viewModel.recuperarLogueo(usuario)
                findNavController().navigate(com.example.agileus.R.id.action_inicioSesionFragment_to_navigation_home)

                }
 */