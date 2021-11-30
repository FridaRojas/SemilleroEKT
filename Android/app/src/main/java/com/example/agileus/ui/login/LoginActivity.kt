package com.example.agileus.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.userServiceLog
import com.example.agileus.databinding.ActivityLoginBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.model.User
import com.example.agileus.ui.login.ui.login.services.GetUserDao
import com.example.agileus.webservices.apis.UserServiceApi
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    //lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignIn = findViewById<Button>(R.id.btnSignUp)
*/
        /*binding.btnLogin.setOnClickListener {
            goToLoginUser()
            btnLogin.setOnClickListener { callServiceGetUsers() }

        }

        binding.btnSignUp.setOnClickListener {
            goToPassword()
        }
    }*/

        /*private fun goToLoginUser() {
        binding.btnLogin.text = getString(R.string.login_login_button)

    }

    private fun goToPassword() {
        val email = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (email.trim().isNotEmpty() ) {
            userServiceLog.getRecuperaDatos(" ", " ")
            cambiarDeActivividad()
        }
    }

    private fun cambiarDeActivividad() {
        val intento = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intento)
        finish()
    }


    fun callServiceGetUsers(): List<User> {
        //peti a realizar
        val userService: UserServiceApi = GetUserDao.getUserDao().create(UserServiceApi::class.java)
        //llamada al metodo declarada en interfaz, metodo de llamada a retrofit.
        val result: Call<List<User>> = userService.getRecuperaDatos(" ", " ")
        //generar peticiones sobre servidor para responder y en este caso la llamada la haremos asyncrona por el metodo enqueue

        var userListResponse: Response<List<User>> = result.execute()
        var listaUsuarios: List<User> = emptyList()
        if (userListResponse.isSuccessful) {
            if (userListResponse.body() != null) {
                listaUsuarios = userListResponse.body()!!
            }
        } else {
            Log.e("Error consumo", userListResponse.message())
        }
        return listaUsuarios

    }*/


    }
}
