package com.example.agileus.ui.modulotareas.creartareas

import android.annotation.SuppressLint
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.R
import androidx.navigation.fragment.findNavController
import com.example.agileus.config.InitialApplication
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.models.DataPersons
import com.example.agileus.models.DataTask
import com.example.agileus.models.Message
import com.example.agileus.models.Tasks
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoAceptar
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoConfirmOp
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmOpStatusListener
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import com.example.agileus.utils.Constantes
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import kotlin.collections.ArrayList

class FormularioCrearTareasFragment : Fragment(), DialogoFechaListener , DialogoConfirmOpStatusListener{
    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!

    lateinit var conversationviewModel  : ConversationViewModel         // ViewModel
    lateinit var asignarTareaViewModel  : CrearTareasViewModel          // ViewModel
    /*  *** Firebase Storage ***  */
    lateinit var firebaseProvider       : FirebaseProvider
    lateinit var mStorageInstance       : FirebaseStorage
    lateinit var mStorageReference      : StorageReference
    lateinit var resultLauncherArchivo  : ActivityResultLauncher<Intent>
    /*  *** Firebase Storage ***  */

    lateinit var listaPersonas              : ArrayList<DataPersons>
    lateinit var personasAsignadasAdapter   : ArrayAdapter<String>
    lateinit var prioridadAdapter           : ArrayAdapter<String>
    lateinit var listaPrioridades           : Array<String>
    lateinit var nombrePersonaAsignada      : String
    lateinit var prioridadAsignada          : String
    lateinit var idPersonaAsignada          : String

    var listaN = ArrayList<String>()
    var idsuperiorInmediato : String = InitialApplication.preferenciasGlobal.recuperarIdSesion()
    var nombreSesion        : String = InitialApplication.preferenciasGlobal.recuperarNombreSesion()
    var grupoSesion         : String = InitialApplication.preferenciasGlobal.recuperarIdGrupoSesion()
    var fechaInicio         : String = ""
    var fechaFin            : String = ""
    var urlPost             : String = ""
    var tituloTarea         : String = ""
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listaPrioridades = resources.getStringArray(R.array.prioridad_array)          // spiner lista de prioridades archivo strings.xml
        asignarTareaViewModel = ViewModelProvider(this).get()                   // ViewModel
        conversationviewModel = ViewModelProvider(this).get()                   // ViewModel
        firebaseProvider  = FirebaseProvider()
        mStorageInstance = FirebaseStorage.getInstance()                              /*  *** Instancias Firebase Storage ***  */
        mStorageReference = mStorageInstance.getReference("Documentos")        /*  *** Instancias Firebase Storage ***  */

        setUpUiAsignarTareas() /*  *** spiners ***  */

        //toolbar
        (activity as HomeActivity).fragmentSeleccionado = "crearTarea"

        resultLauncherArchivo=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                val data:Intent?=result.data
                if(data!= null){
                    try{
                        var returnUri = data?.data!!
                        //val uriString = data.toString()
                        //val myFile = File(uriString).name
                        binding.btnAdjuntarArchivo.text= "Archivo seleccionado: ${data.data!!.lastPathSegment} "
                        Log.d("mensaje","PDF: ${data.data!!.lastPathSegment}")
                        firebaseProvider.subirPdfFirebase(returnUri, Constantes.referenciaTareas, "tarea$idsuperiorInmediato${(0..999).random()}")
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        Log.e("mensaje", "File not found. ${e.message}");
                    }
                }
            }else{
                val newFragment = DialogoAceptar("No se selecciono archivo")
                newFragment.show(
                    (activity as HomeActivity).supportFragmentManager,
                    "missiles"
                )
            }
        }
        firebaseProvider.obs.observe(viewLifecycleOwner,{
            urlPost = it
        })

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {
            // Guardar datos
            nombrePersonaAsignada = (binding.spinnerPersonaAsignada.getEditText() as AutoCompleteTextView).text.toString()
            prioridadAsignada = (binding.spinnerPrioridad.getEditText() as AutoCompleteTextView).text.toString()

            // Obtiene el id de la persona seleccionada
            listaPersonas.forEach(){
                if(nombrePersonaAsignada == it.nombre){
                    idPersonaAsignada= it.id
                }
            }

            if(fechaInicio=="" || fechaFin=="" ||
                binding.edtAgregaTitulo.text.toString().isNullOrEmpty()||
                binding.edtDescripcion.text.toString().isNullOrEmpty()||
                listaPersonas.isNullOrEmpty()||
                    nombrePersonaAsignada.isNullOrEmpty()){
                val newFragment = DialogoAceptar("Faltan datos por agregar.")
                newFragment.show(
                    (activity as HomeActivity).supportFragmentManager,
                    "missiles"
                )

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
                                val newFragment = DialogoAceptar("Fecha de inicio no puede ser mayor a fecha de vencimiento.")
                                newFragment.show(
                                    (activity as HomeActivity).supportFragmentManager,
                                    "missiles"
                                )
                            }
                        }

                        if(mesInicio!!<mesFin!!){                   // Mes inicio es menor que mes fin. NO IMPORTA EL DIA
                            operacionIsert()
                        }else if (mesInicio!!>mesFin!!){
                            val newFragment = DialogoAceptar("Fecha de inicio no puede ser mayor a fecha de vencimiento.")
                            newFragment.show(
                                (activity as HomeActivity).supportFragmentManager,
                                "missiles"
                            )
                        }
                    }
                }else{
                    val newFragment = DialogoAceptar("Fecha de inicio no puede ser mayor a fecha de vencimiento.")
                    newFragment.show(
                        (activity as HomeActivity).supportFragmentManager,
                        "missiles"
                    )
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

    fun operacionIsert(){

        val tarea: Tasks
        val descripcion = binding.edtDescripcion.text
        tituloTarea = binding.edtAgregaTitulo.text.toString()

        tarea = Tasks(
            // TODO: 08/12/2021  agregar id_grupo y nombreEmisor desde sharedpreferences id_grupo
            grupoSesion,                  // id_grupo
            idsuperiorInmediato,
            nombreSesion,
            idPersonaAsignada,                  // Numero de empleado de la persona seleccionada
            nombrePersonaAsignada,              // Nombre de subordinado seleccionado
            fechaInicio,                        // Fecha Inicio
            fechaFin,                           // Fecha Fin
            tituloTarea,                        // Titulo de la tarea
            descripcion.toString(),             // Descripcion
            prioridadAsignada,                  // Prioridad
            "pendiente",                  // Estatus
            urlPost                             // Url de archivo pdf subido a firebase
        )

        val newFragment = DialogoConfirmOp (tarea, idsuperiorInmediato, idPersonaAsignada, this)
        newFragment.show(parentFragmentManager, "Confirmacion")

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
                val newFragment = DialogoAceptar("No se encontraron personas en el grupo")
                newFragment.show(
                    (activity as HomeActivity).supportFragmentManager,
                    "missiles"
                )
                listaPersonas= emptyList<DataPersons>() as ArrayList<DataPersons>
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
    override fun onDateInicioSelected(anio: Int, mes:String, dia:String) {
        anioInicio  = anio
        mesInicio   = mes.toInt()
        diaInicio   = dia.toInt()

        val fecha=binding.edtFechaInicio
        val fechaObtenida = "$anio-$mes-$dia"
        fecha.setText(fechaObtenida)
        fechaInicio = fecha.text.toString()
        Log.e("Mensaje", "Fecha Inicio $fechaInicio")

    }
    override fun onDateFinSelected(anio: Int, mes:String, dia:String) {
        anioFin  = anio
        mesFin   = mes.toInt()
        diaFin   = dia.toInt()

        val fecha=binding.edtFechaFin
        val fechaObtenida = "$anio-$mes-$dia"
        fecha.setText(fechaObtenida)
        fechaFin = fecha.text.toString()
        Log.e("Mensaje", "Fecha Fin $fechaFin")

    }
    override fun onOpSuccessful() {
        val newFragment = DialogoAceptar("Tarea $tituloTarea creada")
        newFragment.show(
            (activity as HomeActivity).supportFragmentManager,
            "missiles"
        )
        //Volver al fragment anterior
        val action = FormularioCrearTareasFragmentDirections.actionFormularioCrearTareasFragmentToNavigationDashboard()
        findNavController().navigate(action)

    }
    override fun onOpFailure() {
        val newFragment = DialogoAceptar("No se pudo crear la tarea")
        newFragment.show(
            (activity as HomeActivity).supportFragmentManager,
            "missiles"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // *** INTERFACES ***
}
