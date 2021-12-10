package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.databinding.ReceiverBuzonBroadcastFragmentBinding
import com.example.agileus.models.Contacts
import com.example.agileus.models.Datos
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReceiverBuzonBroadcastFragment : Fragment() {


    private lateinit var viewModel: ReceiverBuzonBroadcastViewModel
    private var _binding: ReceiverBuzonBroadcastFragmentBinding? = null
    private val binding get() = _binding!!


    companion object{
        var  listas=ArrayList<Datos>()
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ReceiverBuzonBroadcastFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Buzon Enviados Broadcast"

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(ReceiverBuzonBroadcastViewModel::class.java)

        viewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerBuzon.adapter = it
            binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)

        })

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false
        (activity as HomeActivity).ocultarBtnAtras()





        viewModel.devuelvebuzon2()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}
