package com.example.agileus.ui.login.data.viewuno

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.agileus.R
import com.example.agileus.ui.login.ui.login.LoginViewModelUno

// TOKEN
//cerrar sesion x tiempo determinado
class SignInFragment : Fragment() {
    lateinit var navController: NavController
    val loginviewModel: LoginViewModelUno by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        //Puse el inicioSesionFragment pero deberia ser el Sign_In que cree
        view.findViewById<Button>(R.id.inicioSesionFragment).setOnClickListener {
            logged()
        }

        //Puse eso pero debía ser: Sign_Up y abajo action_SignIn_to_signUp
        view.findViewById<Button>(R.id.inicioSesionFragment).setOnClickListener {
            navController.navigate(R.id.action_inicioSesionFragment_to_navigation_home)
        }
    }

    private fun logged() {
        var user = requireView().findViewById<EditText>(R.id.username).text.toString()
        var pass = requireView().findViewById<EditText>(R.id.password).text.toString()

        //if (loginViewModel.signIn(user, pass)){
        if (loginviewModel.signIn(user, pass)){
            navController.navigate(R.id.action_inicioSesionFragment_to_navigation_home) // deberia ser (R.id.action_signIn_to_home2)
        }
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInFragment().apply {

            }
    }
}