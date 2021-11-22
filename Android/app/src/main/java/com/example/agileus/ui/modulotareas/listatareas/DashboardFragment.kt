package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.FragmentDashboardBinding
import com.example.agileus.ui.HomeActivityViewModel

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//          //  textView.text = it
//        })
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardViewModel.devuelveLista()

        dashboardViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.recyclerListaTareas.adapter = it
            binding.recyclerListaTareas.layoutManager = LinearLayoutManager(activity)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}