package com.example.agileus.ui.modulomensajeriabuzon.b

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.BuzonUserFragmentBinding
import com.example.agileus.databinding.FragmentBuzonBinding
import androidx.appcompat.widget.Toolbar


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

        val toolbar: Toolbar = binding.toolbar
        toolbar.setNavigationIcon(com.example.agileus.R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
   //         requireActivity().onBackPressed()
            findNavController().navigate(R.id.action_buzonUserFragment_to_navigation_home)
            //          Toast.makeText(requireContext(),"hola",Toast.LENGTH_SHORT).show()
        })

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