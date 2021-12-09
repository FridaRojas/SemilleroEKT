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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView


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
//        getActivity()?.setTitle("Buzon Broadcaster");
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Buzon Broadcast"


        binding.mensajesrecibidos.setOnClickListener {
             control = 1
            findNavController().navigate(R.id.action_buzonFragment_to_buzonDetallesFragment)
        }

        binding.mensajescomunicados.setOnClickListener {
            // control =2
            findNavController().navigate(R.id.action_buzonFragment_to_receiverBuzonBroadcastFragment)
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false
    }
}