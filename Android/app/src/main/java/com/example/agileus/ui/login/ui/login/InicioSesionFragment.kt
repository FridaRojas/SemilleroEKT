package com.example.agileus.ui.login.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.agileus.R
import com.example.agileus.databinding.InicioSesionFragmentBinding
import com.example.agileus.ui.login.data.model.Users

//, DialogoListen

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

        val view:View = binding.root
        return view
        //return inflater.inflate(R.layout.inicio_sesion_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioSesionViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            goToLogin()
        }


        viewModel.inicioExitoso.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(activity, "Exitoso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Fallido", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun goToLogin() {
        val correo = binding.username.text.toString()
        val password = binding.password.text.toString()

        val usuario= Users("4@gmail.com", 123,"abc")
        viewModel.recuperarLogueo(usuario)

    }

}