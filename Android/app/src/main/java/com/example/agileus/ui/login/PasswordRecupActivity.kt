package com.example.agileus.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agileus.databinding.ActivityPasswordRecupBinding
import com.google.firebase.auth.FirebaseAuth

class PasswordRecupActivity : AppCompatActivity() {
    lateinit var binding: ActivityPasswordRecupBinding
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordRecupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //mDialog = SpotsDialog.Builder().setContext(this).setMessage("Espera un momento").build()

        binding.btnGuardarPass.setOnClickListener {
            clickRegister()
        }
    }

    //despues de declarar el toolbar en el xml de register que es <include, aqui copiamos las demás líneas jaladas de la clase selectOptionAuthActivity.kt (recolectar lo del toolbar) e importar los miembros

    private fun clickRegister() {
       // val name = binding.correo.text.toString()
        val email = binding.correo.text.toString()
        val password = binding.password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                register()
            } else {
                Toast.makeText(
                    applicationContext,
                    "La constraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //mDialog.dismiss()
        } else {
            Toast.makeText(
                applicationContext,
                "Faltan datos, favor de ingresarlos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun register() {

    }

}