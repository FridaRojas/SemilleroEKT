package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R

import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.databinding.FragmentDashboardBinding
import com.example.agileus.ui.HomeActivity


class TaskFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    var listStatus = listOf("Pendientes", "Completadas", "Asignadas")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler Status
        var adaptadorStatus = StatusTasksAdapter(listStatus)
        binding.recyclerStatusTareas.adapter = adaptadorStatus
        binding.recyclerStatusTareas.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL ,false)
        }

        //Mostrar Fragments de acuerdo al estado
        val transaction = (activity as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorTareas, TaskListFragment())
        transaction.commit()

        //Btn Crear tareas
        binding.btnCrearTarea.setOnClickListener {
            it.findNavController().navigate(R.id.formularioCrearTareasFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}