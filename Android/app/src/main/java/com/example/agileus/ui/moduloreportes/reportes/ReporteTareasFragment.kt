package com.example.agileus.ui.moduloreportes.reportes
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.ReporteTareasFragmentBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class ReporteTareasFragment : Fragment() {

    private lateinit var reporteTareasViewModel: ReporteTareasViewModel
    private var _binding: ReporteTareasFragmentBinding? = null
    private lateinit var pieChart: PieChart
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteTareasViewModel =
            ViewModelProvider(this).get(ReporteTareasViewModel::class.java)

        _binding = ReporteTareasFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = binding.pieChart

        initPieChart()

        setDataToPieChart()

        reporteTareasViewModel.devuelvelistaReporte()

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)

        })


        binding.btnReportesMensajes.setOnClickListener {
            val action = ReporteTareasFragmentDirections.actionReporteTareasFragmentToReporteMensajesFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesTareas to "report_slide")
            findNavController().navigate(action,  extras)
        }

    }


    private fun initPieChart() {
        pieChart.setUsePercentValues(false)
        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(false)
        //adding padding
        pieChart.setUsePercentValues(false)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)
        //pieChart.legend.isEnabled.
        //pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        //pieChart.setDrawSliceText(false);
        pieChart.legend.isWordWrapEnabled = false
        pieChart.getLegend().setEnabled(false)

    }

    private fun setDataToPieChart() {
        pieChart.setUsePercentValues(false)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(72f, "Android"))
        dataEntries.add(PieEntry(26f, "Ios"))
        dataEntries.add(PieEntry(2f, "Other"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))//"#4DD0E1"
        colors.add(resources.getColor(R.color.colorSecondary))//"#FFF176"
        colors.add(resources.getColor(R.color.colorGray))//"#FF8A65"

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(0f)
        //pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1000, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)

        //add text in center
        pieChart.setDrawCenterText(false);

        pieChart.invalidate()

    }
}