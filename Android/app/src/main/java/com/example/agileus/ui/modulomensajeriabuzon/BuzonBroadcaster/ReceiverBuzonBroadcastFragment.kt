package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.databinding.ReceiverBuzonBroadcastFragmentBinding

class ReceiverBuzonBroadcastFragment : Fragment() {


    private lateinit var viewModel: ReceiverBuzonBroadcastViewModel
    private var _binding: ReceiverBuzonBroadcastFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ReceiverBuzonBroadcastFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceiverBuzonBroadcastViewModel::class.java)



            viewModel.devuelvebuzon2()

           viewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerBuzon.adapter = it
            binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
