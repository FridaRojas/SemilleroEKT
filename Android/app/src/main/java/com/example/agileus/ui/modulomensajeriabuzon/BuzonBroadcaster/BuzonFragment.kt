package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.FragmentBuzonBinding
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.utils.isConnectedToThisServer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BuzonFragment : Fragment() {

companion object{
    var control=1
    var USERTYPE=""
}

    private var _binding: FragmentBuzonBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBuzonBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root


        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val host: String = "178.33.5.208 " // cambiar por la ip de sus pruebas

        if (isConnectedToThisServer("host")) {
            Log.i("Ping","PING SUCESSFULL")
        } else {
            Log.i("Ping","PING FAILED")
        }



        binding.mensajesrecibidos.setOnClickListener {
             control = 1
            findNavController().navigate(R.id.action_buzonFragment_to_buzonDetallesFragment)
        }

        binding.mensajescomunicados .setOnClickListener {
             control =2
            findNavController().navigate(R.id.action_buzonFragment_to_buzonDetallesFragment)
        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}