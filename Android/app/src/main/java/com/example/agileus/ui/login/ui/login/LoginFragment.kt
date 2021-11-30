package com.example.agileus.ui.login.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.agileus.config.InitialApplication
import com.example.agileus.databinding.FragmentLoginBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.provider.LoginProviderListener
import com.example.agileus.utils.Constantes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class LoginFragment : Fragment(){
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    /*lateinit var mShared: SharedPreferences
    lateinit var mDialogIn: android.app.AlertDialog*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("paso","a")
        viewModel.devuelveUser()
    }

    private fun goToLogin() {
        val correo = binding.username.text.toString()
        val password = binding.password.text.toString()

        if (correo.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                /*mDialogIn.show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)*/

            }
        }
    }


}


