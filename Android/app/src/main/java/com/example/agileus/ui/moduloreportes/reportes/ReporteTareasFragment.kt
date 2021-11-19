package com.example.agileus.ui.moduloreportes.reportes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agileus.R

class ReporteTareasFragment : Fragment() {

    companion object {
        fun newInstance() = ReporteTareasFragment()
    }

    private lateinit var viewModel: ReporteTareasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reporte_tareas_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReporteTareasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}