package com.example.agileus.ui.modulotareas.creartareas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.R
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.models.DataPersons
import com.example.agileus.models.Tasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileNotFoundException

class FormularioCrearTareasFragment : Fragment(), DialogoFechaListener {

    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!

    lateinit var asignarTareaViewModel  : CrearTareasViewModel
    /*  *** Fb Storage ***  */
    lateinit var mStorageInstance       : FirebaseStorage
    lateinit var mStorageReference      : StorageReference
    lateinit var resultLauncherArchivo  : ActivityResultLauncher<Intent>
    /*  *** Fb Storage ***  */
    lateinit var listaN         : ArrayList<String>
    lateinit var listaObj       : ArrayList<DataPersons>
    lateinit var listaPersonas  : ArrayList<DataPersons>

    lateinit var nombrePersonaAsignada  : String
    lateinit var idPersonaAsignada      : String

    var idsuperiorInmediato        : String = "618e88acc613329636a769ae"
    var fechaInicio     : String = ""
    var fechaFin        : String = ""
    var uriPost         : String = ""
    var anioInicio      : Int? = null
    var anioFin         : Int? = null
    var mesInicio       : Int? = null
    var mesFin          : Int? = null
    var diaInicio       : Int? = null
    var diaFin          : Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFormularioCrearTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asignarTareaViewModel = ViewModelProvider(this).get()
        /*  *** Instancias Fb Storage ***  */
        mStorageInstance = FirebaseStorage.getInstance()
        mStorageReference = mStorageInstance.getReference("Documentos")
        /*  *** Instancias Fb Storage ***  */

        setUpUiAsignarTareas() /*  *** spiners ***  */

        resultLauncherArchivo=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                val data:Intent?=result.data
                if(data!= null){
                    try{
                        var returnUri = data?.data!!
                        val uriString = data.toString()
                        val myFile = File(uriString)
                        Log.d("mensaje","PDF: $uriString")
                        subirPdfFirebase(myFile , returnUri)
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        Log.e("mensaje", "File not found. ${e.message}");

                    }
                }
            }else{
                Toast.makeText(context,"No se Selecciono archivo",Toast.LENGTH_LONG).show()
            }
        }

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {

            // Guarda nombre de persona en variable nombrePersonaAsignada
            nombrePersonaAsignada = binding.spinPersonaAsignada.selectedItem as String
            Log.d("Mensaje", "nombrePersonaAsignada $nombrePersonaAsignada")

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
                if(anioInicio!!<=anioFin!!){                        // AÑO FIN NO PUEDE SER MENOR QUE AÑO INICIO
                if(mesInicio!!+1<=mesFin!!+1){                      // Es un mes menor o igual del mismo año
                        if (mesInicio!!+1==mesFin!!+1){             // Si mes inicio es igual a mes fin del mismo año
                            if (diaInicio!!<=diaFin!!){             // Es un dia menor o igual del mismo mes
                                operacionIsert()
                            }else{
                                    Toast.makeText( context,
                                    "Fecha de inicio no puede ser mayor a fecha fin",
                                    Toast.LENGTH_SHORT).show()
                                }
                        }else if(mesInicio!!<mesFin!!){             // Mes inicio(AGOSTO) es menor que mes fin(DICIEMBRE). NO IMPORTA EL DIA
                            operacionIsert()
                        }
                    }else if(mesInicio!!>mesFin!!){                 // Mes de inicio es superior a mes fin pero de año fin superior. NO IMPORTA EL DIA
                        operacionIsert()
                    }


                }else{
                    Toast.makeText( context,
                        "Fecha de inicio no puede ser mayor a fecha fin",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        /* Boton Crear tarea  */

        binding.btnAdjuntarArchivo.setOnClickListener {
            val intentPdf= Intent()
            intentPdf.setAction(Intent.ACTION_GET_CONTENT)
            intentPdf.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intentPdf.type = "*/*"
            //intentPdf.type = "application/pdf"                     // Filtra para archivos pdf
            resultLauncherArchivo.launch(intentPdf)
        }
        binding.edtFechaInicio.setOnClickListener {
            abrirDialogoFecha(view,1)
        }
        binding.edtFechaFin.setOnClickListener {
            abrirDialogoFecha(view,2)
        }
    }

    // *** FUNCIONES ***
    fun operacionIsert(){
        val tarea: Tasks
        val titulo      = binding.edtAgregaTitulo.text
        val descripcion = binding.edtDescripcion.text
        val mPrioridad  = binding.spinPrioridad.selectedItem

        tarea = Tasks(
            "GRUPOID1",                  // id_grupo
            "EMIS1",
            "Raul",
            idPersonaAsignada,                  // Numero de empleado de la persona seleccionada
            nombrePersonaAsignada,              // Nombre de subordinado seleccionado
            fechaInicio,                        // Fecha Inicio
            fechaFin,                           // Fecha Fin
            titulo.toString(),                  // Titulo de la tarea
            descripcion.toString(),             // Descripcion
            mPrioridad.toString().lowercase(),  // Prioridad
            "pendiente",
            false,                         // Leido
            "2014-01-01"

        )
        asignarTareaViewModel.postTarea(tarea)
    }
    fun subirPdfFirebase(pdf: File, uri: Uri){
        try{
            var refenciaPdf = mStorageReference
                .child("Archivos ${(0..999).random()}")
            var uploadTask = refenciaPdf.putFile(uri)
            //.putStream(stream)

            uploadTask.addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        uriPost=task.result.toString()
                        Toast.makeText(context, "Doc cargada correctamente", Toast.LENGTH_LONG).show()
                        Log.i("Uri", "Archivo uri: ${task.result}")

                    } else {
                        Toast.makeText(context, "Ocurrió un error al cargar archivo", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            uploadTask.addOnFailureListener{
                it.printStackTrace()
                Toast.makeText(context,"Ocurrió un error al cargar archivo",Toast.LENGTH_LONG).show()
            }

        }catch(e:Exception){
            Log.e("mensaje",e.message.toString())
            e.printStackTrace()
            Toast.makeText(context,"Ocurrió un error al cargar archivo",Toast.LENGTH_LONG).show()
        }
        finally {
            uriPost=""
        }
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

                listaObj = ArrayList<DataPersons>()
                listaPersonas.forEach(){
                    listaObj.add(it)
                }

                val spinListaAsignarAdapter = ArrayAdapter((activity as HomeActivity),
                    android.R.layout.simple_spinner_item, listaN)
                spinListaAsignarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinPersonaAsignada.adapter=spinListaAsignarAdapter

            }else{
                Toast.makeText(activity , "No se encontraron personas en el grupo", Toast.LENGTH_LONG).show()
            }
        })
        // *** SPINER CON OBJETO CONSUMIDO API RETROFIT ***

        // SPINER CON RECURSO XML
        val spinPrioridadAdapter = ArrayAdapter.createFromResource(activity as HomeActivity, R.array.prioridad_array, android.R.layout.simple_spinner_item)
        spinPrioridadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinPrioridad.adapter=spinPrioridadAdapter
        // SPINER CON RECURSO XML
    }
    fun abrirDialogoFecha(view: View, b:Int) {
        val newFragment = EdtFecha(this, b)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }
    // *** FUNCIONES ***

    // *** INTERFACES ***
    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {
        anioInicio  = anio
        mesInicio   = mes
        diaInicio   = dia
        val fecha=binding.edtFechaInicio
        val fechaObtenida = "$anio-${mes+1}-$dia"
        fecha.setText(fechaObtenida)
        fechaInicio = fecha.text.toString()
        Log.e("Mensaje", "Fecha Inicio $fechaInicio")

    }
    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {
        anioFin = anio
        mesFin  = mes
        diaFin  = dia
        val fecha=binding.edtFechaFin
        val fechaObtenida = "$anio-${mes+1}-$dia"
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
