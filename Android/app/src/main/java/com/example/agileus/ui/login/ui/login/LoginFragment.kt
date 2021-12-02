package com.example.agileus.ui.login.ui.login


import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.FragmentLoginBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.dao.LoginDao
import com.example.agileus.ui.login.dialog.DialogoListen
import com.example.agileus.ui.login.dialog.RecuperaPasswordDialog


class LoginFragment : Fragment(), DialogoListen {
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding = FragmentLoginBinding.inflate(inflater)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            //it.findNavController().navigate(R.id.btnLogin)
            var correo = binding.username.text.toString()
            var password = binding.password.text.toString()

            if(password >= 6.toString()){

                it.findNavController().navigate(R.id.btnLogin)
                Toast.makeText(activity, "Ingreso exitoso...", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "Contraseña y/o correo incorrecto", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnRecuperarPassword.setOnClickListener {
            //it.findNavController().navigate(R.id.btnRecuperarPassword)
            abrirAgregarPass()
        }

            /*binding.btnLogin.setOnClickListener {


                viewModel.devuelveUser()
            }

            //olvidar pass
            binding.btnRecuperarPassword.setOnClickListener {
                findNavController().navigate(R.id.btnRecuperarPassword)
            }*/

    }

    private fun abrirAgregarPass() {
            val newFragment = RecuperaPasswordDialog(this)
            newFragment.show((activity as HomeActivity).supportFragmentManager, "Contraseña Recuperada")
        }



    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun editRecuperarPassword(correo: String, password: String) {
        //TODO("Not yet implemented")
        viewModel = ViewModelProvider(this).get()
        Toast.makeText(activity, "Edición de password exitoso", Toast.LENGTH_SHORT).show()
    }

}