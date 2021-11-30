package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.FragmentTasksBinding

class TaskListFragment : Fragment() {

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
        taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerListaTareas
        taskViewModel.devuelveLista()

        taskViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.recyclerTareasStatus.adapter = it
            binding.recyclerTareasStatus.layoutManager = LinearLayoutManager(activity)
        })


    }
}