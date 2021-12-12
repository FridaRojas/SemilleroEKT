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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment
import com.example.agileus.utils.Constantes.broadlist
import com.google.android.material.bottomnavigation.BottomNavigationView


class BuzonFragment : Fragment() {
    var UserId = InitialApplication.preferenciasGlobal.recuperarIdSesion()
    var token= InitialApplication.preferenciasGlobal.recuperarTokenAuth()


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

        broadlist= UserId

//        Log.d("tokenAuth", tokenAuth)
  //      Log.d("User", idUsuario)
    //    Log.d("User id", InicioSesionFragment.id)



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
        (activity as HomeActivity).ocultarBtnAtras()

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false
    }
}