package com.example.agileus.ui.moduloreportes.reportes

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.format.DateFormat
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.BuildConfig
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.utils.Constantes.tipo_grafica
import com.example.agileus.utils.Constantes.vista
import com.example.agileus.utils.Constantes.messageStadisticData
import com.example.agileus.databinding.ReporteMensajesFragmentBinding
import com.example.agileus.providers.ReportesListener
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.TEAM_ID_REPORTES
import com.example.agileus.utils.Constantes.fechaFinEstadisticas
import com.example.agileus.utils.Constantes.fechaIniEstadisticas
import com.example.agileus.utils.Constantes.idUsuarioEstadisticas
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import android.provider.DocumentsContract

import android.content.ContentUris
import android.database.Cursor

import android.os.Environment

import android.os.Build
import java.net.URI
import java.net.URISyntaxException


@RequiresApi(Build.VERSION_CODES.O)
class ReporteMensajesFragment : Fragment(), ReportesListener, FiltroReportesDialog.FiltroReportesDialogListener {

    private lateinit var reporteMensajesViewModel: ReporteMensajesViewModel
    private var _binding: ReporteMensajesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    //valores enteros de los datos de los mensajes
    private var enviados: Int = 0
    private var recibidos: Int = 0
    private var totales: Int = 0
    private var leidos: Int = 0
    private var enviados_B: Int = 0
    private var recibidos_B: Int = 0


    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_enviados:Float = 0.0f
    private var porcentaje_recibidos:Float = 0.0f
    private var porcentaje_leidos:Float = 0.0f

    private var fechaIniComp = fechaIniEstadisticas
    private var fechaFinComp = fechaFinEstadisticas
    private var userEstComp = idUsuarioEstadisticas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReporteMensajesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        reporteMensajesViewModel = ViewModelProvider(this).get(ReporteMensajesViewModel::class.java)
        solicitarPermisos()

        return root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).fragmentSeleccionado = getString(R.string.taskReportFragment)

        reporteMensajesViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())

        //Primer Grafica al cargar la vista
        reporteMensajesViewModel.listaEmpleadosAux.observe(viewLifecycleOwner, { list->
            messageStadisticData = list
            binding.progressLoadingR.visibility = View.GONE
            binding.btnFiltroReportes.visibility = View.VISIBLE
            if (messageStadisticData.size == 1){
                idUsuarioEstadisticas = preferenciasGlobal.recuperarIdSesion()
                binding.txtNombreReportes.setText(preferenciasGlobal.recuperarNombreSesion())
            }else if (idUsuarioEstadisticas == TEAM_ID_REPORTES && messageStadisticData.size > 1){
                idUsuarioEstadisticas = messageStadisticData[messageStadisticData.size - 1].id
                binding.txtNombreReportes.setText(messageStadisticData[messageStadisticData.size - 1].name)
            }
            empleadoUsuario.forEach {
                if (idUsuarioEstadisticas == it.id){
                    binding.txtNombreReportes.setText(it.name)
                    Log.d("idUsuarioEstadisticas", it.id)
                }
            }

        })

        reporteMensajesViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {
            //binding.txtNombreReportes.setText(Constantes.idUsuarioEstadisticas)

            empleadoUsuario.forEach {
                if (idUsuarioEstadisticas == it.id){
                    binding.txtNombreReportes.setText(it.name)
                    Log.d("idUsuarioEstadisticas", it.id)
                }
            }

            enviados = reporteMensajesViewModel.enviados.value.toString().toInt()
            recibidos = reporteMensajesViewModel.recibidos.value.toString().toInt()
            totales = reporteMensajesViewModel.totales.value.toString().toInt()
            leidos = reporteMensajesViewModel.leidos.value.toString().toInt()
            enviados_B = reporteMensajesViewModel.enviados_B.value.toString().toInt()
            recibidos_B = reporteMensajesViewModel.recibidos_B.value.toString().toInt()

            setStadisticName()
            cambiarGrafica(tipo_grafica)
        })

        binding.btnDownload.setOnClickListener {
            tomarScreenShot(view)
        }

        binding.btnFiltroReportes.setOnClickListener {
            val newFragment = FiltroReportesDialog(this)
            newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
        }

        binding.btnReportesTareas.setOnClickListener {
            val action = ReporteMensajesFragmentDirections.actionReporteMensajesFragmentToReporteTareasFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesMensajes to "report_slide")
            findNavController().navigate(action, extras)
        }
    }

        binding.barras.setOnClickListener {

            when(vista) {

                0 -> {

                    binding.barras.isVisible=false
                    binding.pie.isVisible=true
                    mostrargraficaBarras(2) //Aqui va la gráfica desglosada de mensajes
                    binding.pieChart.isVisible = false
                    binding.barChart.isVisible = true
                    vista = 0
                    tipo_grafica = 1

                }

                1 -> {

                    binding.barras.isVisible=false
                    binding.pie.isVisible=true
                    cambiarGrafica(2)
                    binding.pieChart.isVisible = false
                    binding.barChart.isVisible = true
                    vista = 1
                    tipo_grafica = 1

                }


            }
        }

        binding.pie.setOnClickListener {
            binding.barras.isVisible=true
            binding.pie.isVisible=false
            cambiarGrafica(0)
        }

        cambiarGrafica(tipo_grafica)
}

    fun setStadisticName(){
        messageStadisticData.forEach {
            if (idUsuarioEstadisticas == it.id){
                binding.txtNombreReportes.setText(it.name)
                Log.d("idUsuarioEstadisticas", it.id)
            }
        }
        if (binding.txtNombreReportes.text.isNullOrEmpty()){
            idUsuarioEstadisticas = TEAM_ID_REPORTES
            reporteMensajesViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaBarras(valor:Int) {

        barChart = binding.barChart

        setStadisticName()
        empleadoUsuario.forEach {
            if (idUsuarioEstadisticas == it.id){
                binding.txtNombreReportes.setText(it.name)
                Log.d("idUsuarioEstadisticas", it.id)
            }
        }

        binding.txtRangoFechaReportes.isVisible=false

        reporteMensajesViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteMensajesViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        //inicializacion de la grafica de barras y se agrega el objeto a desglosar para su visuzalización

        val sortedList = ArrayList(empleadoUsuario.sortedWith(compareBy { it.name }))

            setStadisticName()
        if (valor==1){
            graficabarrasBroadcastGrupal(sortedList)
        }
        if (valor==2){
            graficabarrasDesgloseMensajes(sortedList)
        }
        if (valor==3){
            graficabarrasDesgloseBroadcast(sortedList)
        }

    }

    private fun graficabarrasBroadcastGrupal(listaUsuarios: ArrayList<UserMessageDetailReport>) {

        binding.txtPrimerLegend.text = "Recibidos del Broadcast"

        binding.txtTercerLegend.text = "Enviados al Broadcast"

        binding.txtSegundoLegend.text = reporteMensajesViewModel.enviados_B.value.toString()

        binding.txtCuartoLegend.text = reporteMensajesViewModel.recibidos_B.value.toString()

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorSecondary))

        binding.colorlegend2.isVisible = false

        binding.colorlegend4.isVisible = false

        binding.txtDataPrimerLegend.text=""

        binding.txtDataSegundoLegend.text = ""

        binding.txtDataTercerLegend.text=""

        binding.txtDataCuartoLegend.text = ""


        val barChartView = binding.barChart

        val barWidth: Float = 0.15f //anchura de la barra
        val barSpace: Float = 0.10f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.5f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        var contador=0

        listaUsuarios.forEach {
            if(it.name == "Mi equipo"){
                xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), it.sendBroadcast.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.receivedBroadcast.toFloat())))
            }
        }

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.color = Color.parseColor("#66BB6A")
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.color = Color.parseColor("#87D169")
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        var barData = BarData(barDataSet1, barDataSet2)

        val xAxis = barChartView.getXAxis()
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)

        barChartView.setTouchEnabled(true)

        xAxis.setLabelCount(contador)
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(1f)
        barChartView.setVisibleXRangeMinimum(1f)
        barChartView.isDragEnabled = true

        //Y-axis
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(true)
        leftAxis.spaceTop = 1f
        leftAxis.axisMinimum = 0f

        barChartView.data = barData
        barChartView.setVisibleXRange(1f, 1f)

    }

    private fun graficabarrasDesgloseBroadcast(listaUsuarios: ArrayList<UserMessageDetailReport>) {

        binding.txtPrimerLegend.text = "Recibidos del Broadcast"

        binding.txtTercerLegend.text = "Enviados al Broadcast"

        binding.txtSegundoLegend.text = reporteMensajesViewModel.enviados_B.value.toString()

        binding.txtCuartoLegend.text = reporteMensajesViewModel.recibidos_B.value.toString()

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorSecondary))

        binding.colorlegend2.isVisible = false

        binding.colorlegend4.isVisible = false

        binding.txtDataPrimerLegend.text=""

        binding.txtDataSegundoLegend.text = ""

        binding.txtDataTercerLegend.text=""

        binding.txtDataCuartoLegend.text = ""


        val barChartView = binding.barChart

        val barWidth: Float = 0.15f //anchura de la barra
        val barSpace: Float = 0.10f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.5f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        var contador=0

        listaUsuarios.forEach {
            if ((it.name != "Mi equipo") && (it.name != "Mi información")){
                xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), it.sendBroadcast.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.receivedBroadcast.toFloat())))
            }
        }

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.color = Color.parseColor("#66BB6A")
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.color = Color.parseColor("#87D169")
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        var barData = BarData(barDataSet1, barDataSet2)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)

        barChartView.setTouchEnabled(true)

        xAxis.setLabelCount(contador)
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(2f)
        barChartView.setVisibleXRangeMinimum(2f)
        barChartView.isDragEnabled = true

        //Y-axis
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(true)
        leftAxis.spaceTop = 1f
        leftAxis.axisMinimum = 0f

        barChartView.data = barData
        barChartView.setVisibleXRange(1f, 2f)

    }

    private fun graficabarrasDesgloseMensajes(listaUsuarios: ArrayList<UserMessageDetailReport>) {

        binding.colorlegend1.isVisible = true
        binding.colorlegend2.isVisible = true
        binding.colorlegend3.isVisible = true
        binding.colorlegend4.isVisible = true

        binding.txtPrimerLegend.text = "Enviados"
        binding.txtSegundoLegend.text = "Recibidos"
        binding.txtTercerLegend.text = "Totales"
        binding.txtCuartoLegend.text = "Leídos"

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.white))
        binding.colorlegend4.setBackgroundColor(resources.getColor(R.color.colorGray))

        binding.txtDataPrimerLegend.text = reporteMensajesViewModel.enviados.value.toString()
        binding.txtDataSegundoLegend.text = reporteMensajesViewModel.recibidos.value.toString()
        binding.txtDataTercerLegend.text = reporteMensajesViewModel.totales.value.toString()
        binding.txtDataCuartoLegend.text = reporteMensajesViewModel.leidos.value.toString()

        val barChartView = binding.barChart

        val barWidth: Float = 0.12f //anchura de la barra
        val barSpace: Float = 0.08f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.4f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()
        var yValueGroup3 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet
        var barDataSet3: BarDataSet

        var contador=0

        listaUsuarios.forEach {

            //Estos son los datos de verdad

            if ((it.name != "Mi equipo") && (it.name != "Mi información")) {
                xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), it.send.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.received.toFloat())))
                yValueGroup3.add((BarEntry(contador.toFloat(), it.read.toFloat())))
            }

            //Estos son datos de prueba

                /*xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), 7f)))
                yValueGroup2.add((BarEntry(contador.toFloat(), 4f)))
                yValueGroup3.add((BarEntry(contador.toFloat(), 5f)))*/

            //Aquí terminan lños datos de prueba


        }


        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.color = Color.parseColor("#66BB6A")
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.color = Color.parseColor("#87D169")
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        barDataSet3 = BarDataSet(yValueGroup3, "")
        barDataSet3.color = Color.parseColor("#7F8182")
        barDataSet3.setDrawIcons(false)
        barDataSet3.setDrawValues(false)

        var barData = BarData(barDataSet1, barDataSet2, barDataSet3)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)


        barChartView.setTouchEnabled(true)

        xAxis.labelCount = contador
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(2f)
        barChartView.setVisibleXRangeMinimum(2f)
        barChartView.isDragEnabled = true

        //Y-axis
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(true)
        leftAxis.spaceTop = 1f
        leftAxis.axisMinimum = 0f

        barChartView.data = barData
        barChartView.setVisibleXRange(1f, 2f)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaPie() {

        pieChart = binding.pieChart

        binding.colorlegend1.isVisible = true
        binding.colorlegend2.isVisible = true
        binding.colorlegend3.isVisible = true
        binding.colorlegend4.isVisible = true

        setStadisticName()

        //binding.txtNombreReportes.setText(Constantes.idUsuarioEstadisticas)
        reporteMensajesViewModel.devuelvelistaReporte(this, Constantes.idUsuarioEstadisticas)

        reporteMensajesViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteMensajesViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {
            //binding.txtNombreReportes.setText(Constantes.idUsuarioEstadisticas)
            //Toast.makeText(context, reporteMensajesViewModel.cargaDatosExitosa.value.toString(), Toast.LENGTH_SHORT).show()

            setStadisticName()

            binding.txtRangoFechaReportes.isVisible=false

        binding.txtPrimerLegend.text = "Enviados"
        binding.txtSegundoLegend.text = "Recibidos"
        binding.txtTercerLegend.text = "Totales"
        binding.txtCuartoLegend.text = "Leídos"

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.white))
        binding.colorlegend4.setBackgroundColor(resources.getColor(R.color.colorGray))

        binding.txtDataPrimerLegend.text = reporteMensajesViewModel.enviados.value.toString()
        binding.txtDataSegundoLegend.text = reporteMensajesViewModel.recibidos.value.toString()
        binding.txtDataTercerLegend.text = reporteMensajesViewModel.totales.value.toString()
        binding.txtDataCuartoLegend.text = reporteMensajesViewModel.leidos.value.toString()

        porcentaje_enviados = obtenerPorcentajes(enviados, totales)
        porcentaje_recibidos = obtenerPorcentajes(recibidos, totales)
        porcentaje_leidos = obtenerPorcentajes(leidos, totales)

        initPieChart()//inicializacion de la grafica de pie
        //aquí se agregan los valores porcentuales para su visualización
        setDataToPieChart(porcentaje_enviados, porcentaje_recibidos, porcentaje_leidos)


        empleadoUsuario.forEach {
            if (idUsuarioEstadisticas == it.id){
                binding.txtNombreReportes.setText(it.name)
                Log.d("idUsuarioEstadisticas", it.id)
            }
        }

        reporteMensajesViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteMensajesViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

            Constantes.dataEmpleadoUsuario.forEach {
                if (idUsuarioEstadisticas == it.id){
                    binding.txtNombreReportes.setText(it.name)
                    Log.d("idUsuarioEstadisticas", it.id)
                }
            }

            binding.txtRangoFechaReportes.isVisible=false
    }

    //funcion regla de 3 para obtener un porcentage proporcional
    fun obtenerPorcentajes(dato_parcial: Int, dato_total: Int): Float {
        if (dato_total != 0) {
            return (dato_parcial * 100 / dato_total).toFloat()
        } else
        return 0.0f
    }

    private fun initPieChart() {

        pieChart.description.text = ""
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(true)

        //pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)

        // Eliminar cuadritos
        pieChart.getLegend().isEnabled = false

        //Remove center chart text
        pieChart.setDrawSliceText(false)
    }

    private fun setDataToPieChart(enviados: Float, recibidos: Float, leidos: Float) {
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(enviados, "Enviados"))
        dataEntries.add(PieEntry(recibidos, "Recibidos"))
        dataEntries.add(PieEntry(leidos, "Leídos"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))
        colors.add(resources.getColor(R.color.colorGray))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(0f)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFilterSelected() {
        Log.d("DateTASKFilter",  "User: ${idUsuarioEstadisticas}, iniCustom: ${fechaIniEstadisticas}, fecha: ${fechaFinEstadisticas}")

        if(idUsuarioEstadisticas == TEAM_ID_REPORTES){
            reporteMensajesViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
            fechaIniComp = fechaIniEstadisticas
            fechaFinComp = fechaFinEstadisticas
            userEstComp = idUsuarioEstadisticas
        }else{
            //binding.progressLoadingR.visibility = View.VISIBLE
            fechaIniComp = fechaIniEstadisticas
            fechaFinComp = fechaFinEstadisticas
            userEstComp = idUsuarioEstadisticas
            cambiarGrafica(tipo_grafica)
        }

        /*
        }else if (fechaIniComp != Constantes.fechaIniEstadisticas || fechaFinComp != Constantes.fechaFinEstadisticas ||
                userEstComp != Constantes.idUsuarioEstadisticas ){
            //binding.progressLoadingR.visibility = View.VISIBLE
            fechaIniComp = fechaIniEstadisticas
            fechaFinComp = fechaFinEstadisticas
            userEstComp = idUsuarioEstadisticas
            cambiarGrafica(tipo_grafica)
        }else{
            Toast.makeText(context, "Cargando", Toast.LENGTH_LONG).show()
        }
         */

}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun cambiarGrafica(valor: Int) {
        when (valor) {

            0 -> {

                if(binding.pie.isVisible){
                    binding.pie.isVisible=false
                    binding.barras.isVisible=true
                }

                mostrargraficaPie()
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false
                vista = 0
                tipo_grafica = 0
            }
            1 -> {

                mostrargraficaBarras(1)//Broadcast grupal
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true
                vista = 1
                tipo_grafica = 1

            }

            2 -> {

                mostrargraficaBarras(3)//Broadcast desglose
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true
                vista = 1
                tipo_grafica = 1

            }

            else->{

                mostrargraficaPie()
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false
                vista = 0
                tipo_grafica = 0

            }
        }
    }


    lateinit var pathImagen:String

    fun solicitarPermisos(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false) -> {
                    Log.d("No permisos", "Permisos Concedido")
                }
                permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false) -> {
                    Log.d("No permisos", "Permisos Concedido")
                } else -> {
                Toast.makeText(context, "Permisos necesarios para el guardado", Toast.LENGTH_SHORT).show()
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE))
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun tomarScreenShot(view:View){
        var fecha =  Date()
        var formato = DateFormat.format("yyyy-MM-dd_hh:mm:ss",fecha)


        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.getDrawingCache())
        view.isDrawingCacheEnabled = false


        if (Build.VERSION.SDK_INT >= 29) {
            //SC take and save
            var screeenReportUri = bitmap.saveImage(activity as HomeActivity)
            val screeenReport = getPath(activity as HomeActivity, screeenReportUri!!).toString()
            Log.e("RMF filePath", screeenReport)
            Toast.makeText(context, "Información guardada en galeria", Toast.LENGTH_SHORT).show()
        }else{
            try{
                val dirPath  = Environment.getExternalStorageDirectory().toString() + "/agileus"
                val fileDir = File(dirPath)
                if(!fileDir.exists()){
                    val mkdir = fileDir.mkdir()
                }
                val path = "$dirPath/agileus-$formato.jpeg"
                pathImagen = path

                view.isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(view.getDrawingCache())
                view.isDrawingCacheEnabled = false
                val imageFile = File(path)
                var fileOutputStream = FileOutputStream(imageFile)
                val calidad = 100
                bitmap.compress(Bitmap.CompressFormat.JPEG,calidad,fileOutputStream)
                //val bytes =  ByteArrayOutputStream()
                //fileOutputStream.write(bytes.toByteArray())
                fileOutputStream.flush()
                fileOutputStream.close()

                binding.imgCaptura.setImageBitmap(bitmap)

                val content = binding.imgCaptura as View
                content.isDrawingCacheEnabled = true

                val bitmapShare = content.drawingCache
                val root = Environment.getExternalStorageDirectory()
                val cachePath = File(root.absolutePath + "/AgileUs/report_${SystemClock.uptimeMillis()}")
                try {
                    cachePath.createNewFile()
                    val ostream = FileOutputStream(cachePath)
                    bitmapShare.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                    ostream.close()
                } catch (e: java.lang.Exception) {
                    Log.e("Error mapa","Al crear bitmap")
                    e.printStackTrace()
                }
                val imageUri = FileProvider.getUriForFile(Objects.requireNonNull(activity as HomeActivity),
                    BuildConfig.APPLICATION_ID + ".provider", imageFile)

                Handler(Looper.getMainLooper()).postDelayed({
                    var share =  Intent(Intent.ACTION_SEND)
                    share.type = "image/*"
                    share.putExtra(Intent.EXTRA_STREAM, imageUri)
                    startActivity(Intent.createChooser(share,"Compartir captura"))
                },2000)
            }catch (e:Exception){
                Log.e("Error Captura",e.message.toString())
                e.printStackTrace()
            }

        }

    }


    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, uri)) {

                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor?.moveToFirst()!!) {
                    return cursor.getString(column_index)
                }
            } catch (e: java.lang.Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AgileUs")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "report_${SystemClock.uptimeMillis()}")

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.d("URI", values.toString())
            return uri
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}