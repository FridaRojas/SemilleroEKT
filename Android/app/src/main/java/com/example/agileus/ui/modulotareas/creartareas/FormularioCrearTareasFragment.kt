package com.example.agileus.ui.modulotareas.creartareas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.models.DataPersons
import com.example.agileus.models.Message
import com.example.agileus.models.Tasks
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import com.example.agileus.utils.Constantes
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import kotlin.collections.ArrayList
import java.io.File


class FormularioCrearTareasFragment : Fragment(), DialogoFechaListener {
    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!

    lateinit var conversationviewModel  : ConversationViewModel
    lateinit var asignarTareaViewModel  : CrearTareasViewModel
    /*  *** Fb Storage ***  */
    lateinit var firebaseProvider       : FirebaseProvider
    lateinit var mStorageInstance       : FirebaseStorage
    lateinit var mStorageReference      : StorageReference
    lateinit var resultLauncherArchivo  : ActivityResultLauncher<Intent>
    /*  *** Fb Storage ***  */

    lateinit var listaPersonas              : ArrayList<DataPersons>
    lateinit var personasAsignadasAdapter   : ArrayAdapter<String>
    lateinit var prioridadAdapter           : ArrayAdapter<String>
    lateinit var listaPrioridades           : Array<String>
    lateinit var nombrePersonaAsignada      : String
    lateinit var prioridadAsignada          : String
    lateinit var idPersonaAsignada          : String

    var listaN = ArrayList<String>()
    var idsuperiorInmediato : String = "618d9c26beec342d91d747d6"
    var fechaInicio         : String = ""
    var fechaFin            : String = ""
    var uriPost             : String = ""
    var anioInicio          : Int? = null
    var anioFin             : Int? = null
    var mesInicio           : Int? = null
    var mesFin              : Int? = null
    var diaInicio           : Int? = null
    var diaFin              : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFormularioCrearTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaPrioridades = resources.getStringArray(R.array.prioridad_array)    // spiner lista de prioridades archivo strings.xml
        asignarTareaViewModel = ViewModelProvider(this).get()
        conversationviewModel = ViewModelProvider(this).get()
        firebaseProvider  = FirebaseProvider()
        mStorageInstance = FirebaseStorage.getInstance()                           /*  *** Instancias Fb Storage ***  */
        mStorageReference = mStorageInstance.getReference("Documentos")     /*  *** Instancias Fb Storage ***  */


        setUpUiAsignarTareas() /*  *** spiners ***  */

        resultLauncherArchivo=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                val data:Intent?=result.data
                if(data!= null){
                    try{

                        var returnUri = data?.data!!
                        val uriString = data.toString()
                        val myFile = File(uriString).name
                        //val myFile = getRealPathFromURI(requireContext(), returnUri)
                        binding.btnAdjuntarArchivo.text= "Archivo seleccionado"
                        Log.d("mensaje","PDF: $myFile")
                        firebaseProvider.subirPdfFirebase(returnUri, Constantes.referenciaTareas, "tarea$idsuperiorInmediato${(0..999).random()}")
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        Log.e("mensaje", "File not found. ${e.message}");
                    }
                }
            }else{
                Toast.makeText(context,"No se selecciono archivo",Toast.LENGTH_LONG).show()
            }
        }
        firebaseProvider.obs.observe(viewLifecycleOwner,{
            uriPost = it
        })

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {
            // Guardar datos
            nombrePersonaAsignada = (binding.spinnerPersonaAsignada.getEditText() as AutoCompleteTextView).text.toString()
            prioridadAsignada = (binding.spinnerPrioridad.getEditText() as AutoCompleteTextView).text.toString()
            //Toast.makeText(activity, "$nombrePersonaAsignada & $prioridadAsignada", Toast.LENGTH_SHORT).show()

            // Obtiene el numero de empleado de la persona seleccionada
            listaPersonas.forEach(){
                if(nombrePersonaAsignada == it.nombre){
                    idPersonaAsignada= it.numeroEmpleado
                }
            }

            if(fechaInicio=="" || fechaFin=="" ||
                binding.edtAgregaTitulo.text.toString().isNullOrEmpty()||
                binding.edtDescripcion.text.toString().isNullOrEmpty()){
                    Toast.makeText(activity as HomeActivity, "Faltan datos por agregar", Toast.LENGTH_SHORT).show()
            }else{
                // VALIDAR INICIO Y FIN FECHAS
                if(anioInicio!!<=anioFin!!){

                    if(anioInicio!!<anioFin!!){                     // 2021 < 2022
                        operacionIsert()
                    }

                    if(anioInicio==anioFin){                        // 2021 == 2021
                        if (mesInicio==mesFin){                     // Si mes inicio es igual a mes fin del mismo año
                            if (diaInicio!!<=diaFin!!){             // Es un dia menor o igual del mismo mes
                                operacionIsert()
                            }else if(diaInicio!!>diaFin!!){
                                Toast.makeText( context,
                                    "Fecha de inicio no puede ser mayor a fecha de vencimiento",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        if(mesInicio!!<mesFin!!){                   // Mes inicio es menor que mes fin. NO IMPORTA EL DIA
                            operacionIsert()
                        }else if (mesInicio!!>mesFin!!){
                            Toast.makeText( context,
                                "Fecha de inicio no puede ser mayor a fecha de vencimiento",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText( context,
                        "Fecha de inicio no puede ser mayor a fecha de vencimiento",
                        Toast.LENGTH_SHORT).show()
                }
                // VALIDAR INICIO Y FIN FECHAS
            }
        }
        /* Boton Crear tarea  */

        binding.btnAdjuntarArchivo.setOnClickListener {
            val intentPdf= Intent()
            intentPdf.setAction(Intent.ACTION_GET_CONTENT)
            intentPdf.type = "application/pdf"                     // Filtra para archivos pdf
            resultLauncherArchivo.launch(intentPdf)
        }
        binding.edtFechaInicio.setOnClickListener {
            abrirDialogoFecha(1)
        }
        binding.edtFechaFin.setOnClickListener {
            abrirDialogoFecha(2)
        }
    }

    // *** FUNCIONES ***

    /*fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(
            contentUri!!, proj,
            null, null, null
        )
        val column_index = cursor
            ?.getColumnIndexOrThrow(MediaStore.EXTRA_MEDIA_TITLE)
        cursor!!.moveToFirst()
        return cursor.getString(column_index!!)
        //.Images.Media.DATA
    }*/

    fun operacionIsert(){
        val tarea: Tasks
        val titulo      = binding.edtAgregaTitulo.text
        val descripcion = binding.edtDescripcion.text

        tarea = Tasks(
            "619696aa2ae47f99bde6e1e7",                  // id_grupo
            idsuperiorInmediato,
            "Armando Manzanero",
            idPersonaAsignada,                  // Numero de empleado de la persona seleccionada
            nombrePersonaAsignada,              // Nombre de subordinado seleccionado
            fechaInicio,                        // Fecha Inicio
            fechaFin,                           // Fecha Fin
            titulo.toString(),                  // Titulo de la tarea
            descripcion.toString(),             // Descripcion
            prioridadAsignada,                  // Prioridad
            "pendiente",
            uriPost                            // Url de archivo pdf subido a firebase
        )

        /*Toast.makeText(activity as HomeActivity,
            "Datos to POST = " +
                "Titulo: $titulo, " +
                "Prioridad: ${mPrioridad.toString().lowercase()}, " +
                "Nombre persona asignada: $nombrePersonaAsignada, " +
                "Fecha inicio: $fechaInicio, " +
                "Fecha fin: $fechaFin, "+
                "Url pdf: $uriPost, "+
                "Descripcion: $descripcion ",
            Toast.LENGTH_LONG).show()*/

        asignarTareaViewModel.postTarea(tarea)

        // Enviar tarea a la conversacion grupal
        val mensajeTareas = Message(Constantes.id,"618b05c12d3d1d235de0ade0-618d9c26beec342d91d747d6-618e8743c613329636a769aa","",
            "Se asigno la tarea: ${titulo.toString()} a $nombrePersonaAsignada",Constantes.finalDate)
        conversationviewModel.mandarMensaje(Constantes.idChat,mensajeTareas)
        Log.d("mensaje Tareas","$mensajeTareas")

        //Volver al fragment anterior
        val action = FormularioCrearTareasFragmentDirections.actionFormularioCrearTareasFragmentToNavigationDashboard()
        findNavController().navigate(action)

    }
    fun setUpUiAsignarTareas(){

        // *** SPINER CON OBJETO CONSUMIDO API RETROFIT ***
        asignarTareaViewModel.devuelvePersonasGrupo(idsuperiorInmediato)
        asignarTareaViewModel.personasGrupoLista.observe(viewLifecycleOwner , {

            if(it.isNotEmpty()){
                listaPersonas = it

                //Llenar lista con nombres de subordinados
                listaN = ArrayList<String>()
                listaPersonas.forEach(){
                    listaN.add(it.nombre)
                }

                //SpinnerPersonasAsignadas
                personasAsignadasAdapter = ArrayAdapter((activity as HomeActivity), R.layout.support_simple_spinner_dropdown_item, listaN )
                binding.textSpinPersona.setAdapter(personasAsignadasAdapter)
                binding.textSpinPersona.threshold

            }else{
                Toast.makeText(activity , "No se encontraron personas en el grupo", Toast.LENGTH_LONG).show()
            }
        })

        //Spinner Prioridades
        prioridadAdapter = ArrayAdapter((activity as HomeActivity), R.layout.support_simple_spinner_dropdown_item, listaPrioridades )
        binding.textSpinPrioridad.setAdapter(prioridadAdapter)
        binding.textSpinPrioridad.threshold

    }
    fun abrirDialogoFecha(b:Int) {
        val newFragment = EdtFecha(this, b)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }
    // *** FUNCIONES ***

    // *** INTERFACES ***
    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {
        val diaString : String
        val mesString : String
        anioInicio  = anio
        mesInicio   = mes+1
        diaInicio   = dia

        if(dia<10){
            diaString = "0$dia"
        }else{
            diaString = "$dia"
        }
        if(mes+1<10){
            mesString = "0$mesInicio"
        }else{
            mesString = "${mes+1}"
        }

        val fecha=binding.edtFechaInicio
        val fechaObtenida = "$anio-$mesString-$diaString"
        fecha.setText(fechaObtenida)
        fechaInicio = fecha.text.toString()
        Log.e("Mensaje", "Fecha Inicio $fechaInicio")

    }
    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {
        val diaString : String
        val mesString : String
        anioFin  = anio
        mesFin   = mes+1
        diaFin   = dia

        if(dia<10){
            diaString = "0$dia"
        }else{
            diaString = "$dia"
        }
        if(mes+1<10){
            mesString = "0$mesFin"
        }else{
            mesString = "${mes+1}"
        }

        val fecha=binding.edtFechaFin
        val fechaObtenida = "$anio-$mesString-$diaString"
        fecha.setText(fechaObtenida)
        fechaFin = fecha.text.toString()
        Log.e("Mensaje", "Fecha Fin $fechaFin")

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // *** INTERFACES ***
}
