package com.example.agileus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agileus.R
import com.example.agileus.databinding.InicioSesionFragmentBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: InicioSesionFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InicioSesionFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}