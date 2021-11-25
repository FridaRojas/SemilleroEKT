package com.example.agileus.ui.modulotareas.listatareas

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R

import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.databinding.FragmentTaskBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener


class TaskFragment : Fragment(), TaskDialogListener {



    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel

    var listStatus = listOf("Pendientes", "Iniciadas", "Revisión", "Terminadas", "Asignadas", "Canceladas")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
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

        //RecyclerListaTareas
        taskViewModel.devolverListaPorStatus()
        taskViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerTareas.adapter = it
            binding.recyclerTareas.layoutManager = LinearLayoutManager(activity)
        })

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
        when (status) {
            listStatus[0] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status1)
                binding.tituloTareas.text = getString(R.string.titleStatus1)
            }
            listStatus[1] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status2)
                binding.tituloTareas.text = getString(R.string.titleStatus2)
            }
            listStatus[2] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status3)
                binding.tituloTareas.text = getString(R.string.titleStatus3)
            }
            listStatus[3] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status4)
                binding.tituloTareas.text = getString(R.string.titleStatus4)
            }
            listStatus[4] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status5)
                binding.tituloTareas.text = getString(R.string.titleStatus5)
            }
            listStatus[5] -> {
                taskViewModel.statusRecycler.value = getString(R.string.status6)
                binding.tituloTareas.text = getString(R.string.titleStatus6)
            }
        }

        //taskViewModel.statusRecycler.value = "Iniciada"
        taskViewModel.devolverListaPorStatus()
        //Toast.makeText(activity, "${taskViewModel.statusRecycler.value}", Toast.LENGTH_SHORT).show()
    }
}