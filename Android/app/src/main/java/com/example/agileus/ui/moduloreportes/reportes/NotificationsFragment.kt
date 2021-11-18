package com.example.agileus.ui.moduloreportes.reportes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.databinding.FragmentNotificationsBinding
import com.github.mikephil.charting.data.LineData

import com.github.mikephil.charting.data.LineDataSet

import android.R
import android.R.attr

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import android.R.attr.y





class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart: LineChart
        val lineDataSet: LineDataSet

// Enlazamos al XML
        lineChart = binding.lineChart
        //view.findViewById(R.id.lineChart)

// Creamos un set de datos
        val lineEntries = ArrayList<Entry>()
        for (i in 0..10) {
            var y = ((Math.random() * 8).toInt() + 1).toFloat()
            lineEntries.add(Entry(i.toFloat(), attr.y.toFloat()))
        }
// Unimos los datos al data set
        lineDataSet = LineDataSet(lineEntries, "Platzi")

// Asociamos al gráfico
        val lineData = LineData()
        lineData.addDataSet(lineDataSet)
        lineChart.data = lineData

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}