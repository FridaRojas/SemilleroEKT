package com.example.agileus.ui.login.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.core.util.PatternsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.config.MySharedPreferences.Companion.TOKEN_KEY
import com.example.agileus.databinding.InicioSesionFragmentBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.model.Users
import com.example.agileus.ui.modulomensajeria.listacontactos.ListConversationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.MainScope
import java.util.regex.Pattern


class InicioSesionFragment : Fragment() {
    private var _binding: InicioSesionFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = InicioSesionFragment()
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


        binding.btnLogin.setOnClickListener {

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