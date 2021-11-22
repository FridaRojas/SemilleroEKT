package com.example.agileus.ui.modulotareas.principalTareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.databinding.FragmentTasksBinding
import com.example.agileus.ui.modulotareas.listatareas.DashboardViewModel

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //RecyclerListaTareas
        taskViewModel.devuelveLista()

        taskViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.recyclerListaTareas.adapter = it
            binding.recyclerListaTareas.layoutManager = LinearLayoutManager(activity)
        })
    }
}