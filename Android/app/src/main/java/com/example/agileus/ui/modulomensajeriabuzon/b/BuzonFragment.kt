package com.example.agileus.ui.modulomensajeriabuzon.b

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.databinding.FragmentBuzonBinding

class BuzonFragment : Fragment() {

companion object{
    var control=1
}

    private var _binding: FragmentBuzonBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        binding.mensajesrecibidos.setOnClickListener {
             control = 1
            findNavController().navigate(R.id.action_buzonFragment_to_buzonDetallesFragment)
        }

        binding.mensajescomunicados .setOnClickListener {
             control=2
            findNavController().navigate(R.id.action_buzonFragment_to_buzonDetallesFragment)
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}