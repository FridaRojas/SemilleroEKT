package com.example.agileus.ui.modulotareas.creartareas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.R
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.models.DataPersons
import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.Tasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosFormularioCrearTareasListener

private const val ARG_PARAM1 = "param1"

class FormularioCrearTareasFragment : Fragment(), DialogosFormularioCrearTareasListener {

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FormularioCrearTareasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    lateinit var listaN: ArrayList<String>
    lateinit var listaObj: ArrayList<DataPersons>
    lateinit var asignarTareaViewModel          : CrearTareasViewModel
    lateinit var listaPersonas                  : ArrayList<DataPersons>

    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!
    private var param1  : String? = null

    lateinit var nombrePersonaAsignada  : String
    lateinit var idPersonaAsignada      : String

    var idsuperiorInmediato        : String = "618e88acc613329636a769ae"
    var fechaInicio     : String = ""
    var fechaFin        : String = ""

    var anioInicio      : Int? = null
    var anioFin         : Int? = null
    var mesInicio       : Int? = null
    var mesFin          : Int? = null
    var diaInicio       : Int? = null
    var diaFin          : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFormularioCrearTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asignarTareaViewModel = ViewModelProvider(this).get()
        setUpUiAsignarTareas()

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

        binding.edtFechaInicio.setOnClickListener {
            abrirDialogoFecha(view,1)
        }
        binding.edtFechaFin.setOnClickListener {
            abrirDialogoFecha(view,2)
        }
    }

    // *** FUNCIONES ***
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
