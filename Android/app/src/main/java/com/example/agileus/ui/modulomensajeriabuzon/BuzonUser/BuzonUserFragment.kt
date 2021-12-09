package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.BuzonUserFragmentBinding
import com.example.agileus.ui.login.ui.login.InicioSesionFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Buzon de ${InicioSesionFragment.userName}"


        binding.mensajesrecibidos.isEnabled=false

        binding.mensajesrecibidos.setOnClickListener {
            BuzonFragment.control = 1
            findNavController().navigate(R.id.action_buzonUserFragment_to_buzonDetallesUserFragment)
        }

        binding.mensajescomunicados .setOnClickListener {
            BuzonFragment.control =2
            findNavController().navigate(R.id.action_buzonUserFragment_to_buzonDetallesUserFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}