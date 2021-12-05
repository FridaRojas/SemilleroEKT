package com.example.agileus.ui.login.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.config.MySharedPreferences.Companion.TOKEN_KEY
import com.example.agileus.databinding.InicioSesionFragmentBinding
import com.example.agileus.ui.login.data.model.Users
import com.google.android.material.bottomnavigation.BottomNavigationView


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
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)
        //AGREGADA
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false

        binding.btnLogin.setOnClickListener {
            val correo = binding.username.text.toString()
            val password = binding.password.text.toString()

            //val usuario = Users(correo, password.toInt(), TOKEN_KEY)
            //viewModel.recuperarLogueo(usuario)

            val visibility = if (binding.progressLoading.visibility == View.GONE) {
                View.VISIBLE
            } else
                View.GONE
            binding.progressLoading.visibility = visibility


            if (correo.isEmpty()) {
                binding.username.error = "Se requiere ingresar un correo valido"
                binding.username.requestFocus()
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                binding.password.error = "Se requiere ingresar una contraseña valida"
                binding.password.requestFocus()
                return@setOnClickListener
            } else {
                Toast.makeText(activity, "Exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_inicioSesionFragment_to_navigation_home)


                viewModel.inicioExitoso.observe(viewLifecycleOwner, Observer {
                    if (it) {

                        Toast.makeText(activity, "Exitoso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_inicioSesionFragment_to_navigation_home)
                    } else {

                        Toast.makeText(activity, "Fallido", Toast.LENGTH_SHORT).show()
                    }
                })

            }


            /*private fun goToLogin() {
        val correo = binding.username.text.toString()
        val password = binding.password.text.toString()

        val usuario= Users(correo,password.toInt(), TOKEN_KEY)
        viewModel.recuperarLogueo(usuario)

    }
*/
        }
    }
}