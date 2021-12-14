package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.databinding.BuzonUserFragmentBinding
import com.example.agileus.models.Datas
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.memsajes2
import java.util.*


class BuzonUserFragment : Fragment() {


    private var _binding: BuzonUserFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BuzonUserFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val space =InitialApplication.preferenciasGlobal.recuperarNombreSesion()


        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Buzon de $space "


        binding.mensajesrecibidos.isEnabled=false


        memsajes2.clear()
        binding.mensajesrecibidos.setOnClickListener {
            BuzonFragment.control = 1
            findNavController().navigate(R.id.action_buzonUserFragment_to_buzonDetallesUserFragment)
        }

        binding.mensajescomunicados .setOnClickListener {
            BuzonFragment.control =2
            findNavController().navigate(R.id.action_buzonUserFragment_to_buzonDetallesUserFragment)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as HomeActivity).ocultarBtnAtras()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}