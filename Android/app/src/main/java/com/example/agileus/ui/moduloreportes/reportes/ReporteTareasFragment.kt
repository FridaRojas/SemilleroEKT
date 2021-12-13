package com.example.agileus.ui.moduloreportes.reportes
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.DateFormat
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.agileus.databinding.ReporteTareasFragmentBinding
import com.example.agileus.providers.ReportesListener
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.taskStadisticData
import com.example.agileus.utils.Constantes.fechaFinEstadisticas
import com.example.agileus.utils.Constantes.fechaIniEstadisticas
import com.example.agileus.utils.Constantes.idUsuarioEstadisticas
import com.example.agileus.utils.Constantes.tipo_grafica
import com.example.agileus.utils.Constantes.vista
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList

class ReporteTareasFragment : Fragment(), ReportesListener, FiltroReportesDialog.FiltroReportesDialogListener  {

    private lateinit var reporteTareasViewModel: ReporteTareasViewModel
    private var _binding: ReporteTareasFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    //valores enteros de los datos de los mensajes
    private var terminadas: Int = 0
    private var pendientes: Int = 0
    private var iniciada: Int = 0
    private var revision: Int = 0
    private var totales: Int = 0
    private var leidas: Int = 0

    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_termidas:Float = 0.0f
    private var porcentaje_pendientes:Float = 0.0f
    private var porcentaje_iniciada:Float = 0.0f
    private var porcentaje_revision:Float = 0.0f
    private var porcentaje_leidas:Float = 0.0f

    private var fechaIniComp = Constantes.fechaIniEstadisticas
    private var fechaFinComp = Constantes.fechaFinEstadisticas
    private var userEstComp = Constantes.idUsuarioEstadisticas


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteTareasViewModel = ViewModelProvider(this).get(ReporteTareasViewModel::class.java)
        _binding = ReporteTareasFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())

        reporteTareasViewModel.listaEmpleadosAux.observe(viewLifecycleOwner, { list->
            taskStadisticData = list
            binding.progressLoadingR.visibility = View.GONE
            binding.btnFiltroReportes.visibility = View.VISIBLE
            if (taskStadisticData.size == 1){
                idUsuarioEstadisticas = preferenciasGlobal.recuperarIdSesion()
                binding.txtNombreReportes.setText(preferenciasGlobal.recuperarNombreSesion())
            }else if (idUsuarioEstadisticas == Constantes.TEAM_ID_REPORTES && taskStadisticData.size > 1){
                idUsuarioEstadisticas = taskStadisticData[taskStadisticData.size - 1].id
                binding.txtNombreReportes.setText(taskStadisticData[taskStadisticData.size - 1].name)
            }
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

        binding.btnReportesMensajes.setOnClickListener {
            val action = ReporteTareasFragmentDirections.actionReporteTareasFragmentToReporteMensajesFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesTareas to "report_slide")
            findNavController().navigate(action,  extras)
        }

        //cambiarGrafica(tipo_grafica)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setStadisticName(){
        binding.txtNombreReportes.setText("")
        taskStadisticData.forEach {
            if (idUsuarioEstadisticas == it.id){
                binding.txtNombreReportes.setText(it.name)
                Log.d("idUsuarioEstadisticas", it.id)
            }
        }
        if (binding.txtNombreReportes.text.isNullOrEmpty()){
            idUsuarioEstadisticas = Constantes.TEAM_ID_REPORTES
            reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaPie() {
        pieChart=binding.pieChart
        setStadisticName()

        binding.txtRangoFechaReportes.isVisible=false
        binding.txtRangoFechaReportes.setText(fechaIniEstadisticas + " " + fechaFinEstadisticas)

        reporteTareasViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)
        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteTareasViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {
            binding.colorlegend2.isVisible=true
            binding.colorlegend4.isVisible=true

            binding.txtPrimerLegend.text="Pendientes"
            binding.txtSegundoLegend.text="Iniciadas"
            binding.txtTercerLegend.text="Revisión"
            binding.txtCuartoLegend.text="Terminadas"

            binding.colorlegend1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            binding.colorlegend2.setBackgroundColor(resources.getColor(R.color.colorSecondary))
            binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorGray))
            binding.colorlegend4.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))

            pendientes = reporteTareasViewModel.pendientes.value.toString().toInt()
            binding.txtDataPrimerLegend.text = pendientes.toString()

            iniciada = reporteTareasViewModel.iniciadas.value.toString().toInt()
            binding.txtDataSegundoLegend.text = iniciada.toString()

            revision = reporteTareasViewModel.revision.value.toString().toInt()
            binding.txtDataTercerLegend.text = revision.toString()

            terminadas = reporteTareasViewModel.terminadas.value.toString().toInt()
            binding.txtDataCuartoLegend.text = terminadas.toString()

            totales = reporteTareasViewModel.totales.value.toString().toInt()
            porcentaje_termidas = obtenerPorcentajes(terminadas, totales)
            porcentaje_pendientes = obtenerPorcentajes(pendientes, totales)
            porcentaje_leidas = obtenerPorcentajes(leidas, totales)

            porcentaje_revision = obtenerPorcentajes(revision, totales)
            porcentaje_iniciada = obtenerPorcentajes(iniciada, totales)
            porcentaje_leidas = obtenerPorcentajes(leidas, totales)

            initPieChart()//inicializacion de la grafica de pie
            //aquí se agregan los valores porcentuales para su visualización
            setDataToPieChart(porcentaje_pendientes, porcentaje_iniciada, porcentaje_revision, porcentaje_termidas)

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaBarras() {

        barChart=binding.barChart
        setStadisticName()

        //binding.txtRangoFechaReportes.isVisible=true
        //binding.txtRangoFechaReportes.setText(fechaIniEstadisticas + " " + fechaFinEstadisticas)


        reporteTareasViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteTareasViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {
            binding.txtPrimerLegend.text="Finalizadas a tiempo"
            binding.txtDataPrimerLegend.text=""

            binding.colorlegend2.isVisible = false
            binding.txtSegundoLegend.text = reporteTareasViewModel.aTiempo.value.toString()
            binding.txtDataSegundoLegend.text = ""

            binding.txtTercerLegend.text="Finalizadas fuera de tiempo"
            binding.txtDataTercerLegend.text=""
            //binding.txtDataTercerLegend.text = reporteTareasViewModel.terminadas.value.toString()
            //terminadas = reporteTareasViewModel.terminadas.value.toString().toInt()
            binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorSecondary))

            binding.txtDataCuartoLegend.text = ""
            binding.colorlegend4.isVisible = false
            binding.txtCuartoLegend.text = reporteTareasViewModel.fueraTiempo.value.toString()

            pendientes = reporteTareasViewModel.aTiempo.value.toString().toInt()
            terminadas = reporteTareasViewModel.fueraTiempo.value.toString().toInt()
            initBarChart(terminadas.toFloat(),pendientes.toFloat())//inicializacion de la grafica de barras
            // y se agregan los valores porcentuales para su visualización

        })
    }

    //funcion regla de 3 para obtener un porcentage proporcional
    fun obtenerPorcentajes(dato_parcial:Int, dato_total:Int):Float{
        if(dato_total!=0) {
            return (dato_parcial*100/dato_total).toFloat()
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

    private fun setDataToPieChart(pendientes:Float,iniciadas:Float,revision:Float,terminadas:Float){
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(pendientes, "Pendientes"))
        dataEntries.add(PieEntry(iniciadas, "Iniciadas"))
        dataEntries.add(PieEntry(revision, "Revision"))
        dataEntries.add(PieEntry(terminadas, "Terminadas"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))
        colors.add(resources.getColor(R.color.colorGray))
        colors.add(resources.getColor(android.R.color.holo_orange_dark))

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

    private fun initBarChart(enviados:Float,recibidos:Float) {

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(.5f, enviados))
        entries.add(BarEntry(1.5f, recibidos))

        val barDataSet = BarDataSet(entries, "")
        //barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))


        val data = BarData(barDataSet)
        barChart.data = data
        data.setBarWidth(0.3f);//Reducir el ancho de las barras
        barDataSet.colors = colors
        data.setValueTextSize(0f)

        //hide grid lines
        barChart.axisLeft.setDrawGridLines(true)
        barChart.xAxis.setDrawGridLines(true)
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.isEnabled=false


        //remove right y-axis
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = true

        //forzar a que la barra izquierda de la gráfica, muestre por valores enteros
        barChart.axisLeft.setGranularity(1.0f);
        barChart.axisLeft.setGranularityEnabled(true); // Required to enable granularity


        barChart.setTouchEnabled(false)

        //remove legend
        barChart.legend.isEnabled = false
        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(1000)


        //draw chart
        barChart.invalidate()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun cambiarGrafica(valor:Int) {

        when (valor) {
            1 -> {
                mostrargraficaBarras()
                binding.barChart.isVisible=true
                binding.pieChart.isVisible=false
                vista = 1
                tipo_grafica=1
            }else -> {
                mostrargraficaPie()
                binding.pieChart.isVisible=true
                binding.barChart.isVisible=false
                vista = 0
                tipo_grafica=0
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFilterSelected() {
        Log.d("DateTASKFilter",  "User: ${idUsuarioEstadisticas}, iniCustom: ${fechaIniEstadisticas}, fecha: ${fechaFinEstadisticas}")

        if(idUsuarioEstadisticas == "TEAM_ID_CREATED_BY_MOD_REPORT"){
            reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
        }else {
            cambiarGrafica(tipo_grafica)
        }

    }




    lateinit var pathImagen:String
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
            //Toast.makeText(context, "Compartiendo...", Toast.LENGTH_SHORT).show()

            val imageFile = File(screeenReport)
            val imageUri = FileProvider.getUriForFile(activity as HomeActivity,
                BuildConfig.APPLICATION_ID + ".provider", imageFile)

            Handler(Looper.getMainLooper()).postDelayed({
                var share =  Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                share.putExtra(Intent.EXTRA_STREAM, imageUri)
                startActivity(Intent.createChooser(share,"Compartir captura"))
            },500)
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
                val imageUri = FileProvider.getUriForFile(
                    Objects.requireNonNull(activity as HomeActivity),
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
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}