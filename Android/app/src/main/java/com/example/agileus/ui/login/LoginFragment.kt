package com.example.agileus.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.FragmentLoginBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.utils.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var mShared: SharedPreferences
    lateinit var mDialogIn: android.app.AlertDialog
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    //AGREGADA
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnLogin.setOnClickListener {
           goToLogin()
        }

    }

    private fun goToLogin() {
        val correo = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (correo.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                mDialogIn.show()
                mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        //Toast.makeText( applicationContext,"Logueo correctamente", Toast.LENGTH_SHORT).show()
                        var tipo_usuario = mShared.getString(Constantes.URL_LOGIN, " ")
                        if (tipo_usuario.equals(Constantes.URL_LOGIN)) {
                            //val action = LoginFragment.actionDosFragmentToUnoFragment(numero = 4)
                            //findNavController().navigate(action)

                            /*val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                             */

                        } else {
                            /*val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                             */
                        }

                    }
                    else{
                        //Toast.makeText(applicationContext,"Usuario o password incorrectos", Toast.LENGTH_SHORT).show()
                    }
                    mDialogIn.dismiss()
                }
            }
            else{
               // Toast.makeText(applicationContext,"La contraseña debe ser de al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }
        }
        else{
          //  Toast.makeText(applicationContext,"Los campos de usuario y login son obligatorios", Toast.LENGTH_SHORT).show()
        }
    }
}