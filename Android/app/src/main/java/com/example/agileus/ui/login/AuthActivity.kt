package com.example.agileus.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.agileus.R
import com.example.agileus.databinding.ActivityAuthBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.utils.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    lateinit var mShared: SharedPreferences

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mDialogIn: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //mDialogIn = SpotsDialog.Builder().setContext(this).setMessage("Espera un momento").build()
        //ponerle evento al boton

     /*   mShared =
            applicationContext.getSharedPreferences(Constantes.USER_KEY, Context.MODE_PRIVATE)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

      */

        //si ya tiene cuanta en db
        binding.btnLogin.setOnClickListener {
            login()
        }
        /*binding.btnLogin.setOnClickListener {
            goToLogin()
        }

         */

        //si quiere recuperar passcword de su cuenta
        binding.btnRegister.setOnClickListener {
            goToPassword()
        }
    }



    private fun goToPassword() {
        var tipo_usuario = mShared.getString(Constantes.USER_KEY, " ")
        if (tipo_usuario.equals(Constantes.USER_KEY)) {

            val intent = Intent(applicationContext, PasswordRecupActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(applicationContext, PasswordRecupActivity::class.java)
            startActivity(intent)
        }
    }
       /* val intent = Intent(applicationContext, PasswordRecupActivity::class.java)
        startActivity(intent)
    }

        */


    /*private fun goToLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

     */
    private fun login() {
        val correo = binding.correo.text.toString()
        val password = binding.password.text.toString()

        if (correo.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                mDialogIn.show()
                mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        //Toast.makeText( applicationContext,"Logueo correctamente", Toast.LENGTH_SHORT).show()
                        var tipo_usuario = mShared.getString(Constantes.USER_KEY, " ")
                        if (tipo_usuario.equals(Constantes.USER_KEY)) {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                        } else {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Usuario o password incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    mDialogIn.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "La contraseña debe ser de al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Los campos de usuario y login son obligatorios",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}