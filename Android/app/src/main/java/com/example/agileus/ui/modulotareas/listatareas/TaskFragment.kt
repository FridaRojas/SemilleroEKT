package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R

import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.databinding.FragmentDashboardBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.creartareas.CrearTareasViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener


class TaskFragment : Fragment(), TaskDialogListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel

    var listStatus = listOf("Pendientes", "Completadas", "Asignadas")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler Status
        var adaptadorStatus = StatusTasksAdapter(listStatus, this)
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

    override fun getTaskByStatus(status: String) {
        Toast.makeText(activity, "$status", Toast.LENGTH_SHORT).show()
    }

}