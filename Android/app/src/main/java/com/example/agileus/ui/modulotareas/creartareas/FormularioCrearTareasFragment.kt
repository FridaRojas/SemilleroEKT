package com.example.agileus.ui.modulotareas.creartareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.models.Tasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosFormularioCrearTareasListener

private const val ARG_PARAM1 = "param1"

class FormularioCrearTareasFragment : Fragment(), DialogosFormularioCrearTareasListener {

    private lateinit var asignarTareaViewModel: CrearTareasViewModel

    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null

    var fechaInicio     : String = ""
    var fechaFin        : String = ""

    var anioInicio      : Int = 0
    var anioFin         : Int = 0
    var mesInicio       : Int = 0
    var mesFin          : Int = 0
    var diaInicio       : Int = 0
    var diaFin          : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        asignarTareaViewModel =
            ViewModelProvider(this).get(CrearTareasViewModel::class.java)

        _binding = FragmentFormularioCrearTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUiAsignarTareas()

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {

            if(fechaInicio=="" || fechaFin=="" ||
                binding.edtAgregaTitulo.text.toString().isNullOrEmpty()||
                binding.edtDescripcion.text.toString().isNullOrEmpty()){

                Toast.makeText(activity as HomeActivity, "Faltan datos por agregar", Toast.LENGTH_SHORT).show()

            }else{
                // VALIDAR INICIO Y FIN FECHAS
                if(anioInicio<=anioFin){                        // AÑO FIN NO PUEDE SER MENOR QUE AÑO INICIO
                if(mesInicio+1<=mesFin+1){                      // es un mes menor o igual del mismo año
                        if (mesInicio+1==mesFin+1){             // si mes inicio es igual a mes fin del mismo año
                            if (diaInicio<=diaFin){             // es un dia menor o igual del mismo mes
                                // OPERACION INSERTAR
                                operacionIsert()
                            }else{
                                    Toast.makeText( context,
                                    "El dia de Vencimiento no puede ser anterior al dia de inicio",
                                    Toast.LENGTH_SHORT).show()
                                }
                        }else if(mesInicio<mesFin){             // mes inicio(AGOSTO) es menor que mes fin(DICIEMBRE)
                            // NO IMPORTA EL DIA
                            // OPERACION INSERTAR
                            operacionIsert()
                        }
                    }else if(mesInicio>mesFin){                 // mesInicio es superior a mes fin pero de año fin superior
                        // NO IMPORTA EL DIA
                        // 28/05/2022  inicio
                        // 05/03/2024  fin
                        // OPERACION INSERTAR
                        operacionIsert()
                    }
                }else{
                    Toast.makeText( context,
                        "El año de Vencimiento no puede ser anterior al año de inicio",
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FormularioCrearTareasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    fun setUpUiAsignarTareas(){
        //Recuperar id grupo
        // SPINER CON OBJETO CONSUMIDO API RETROFIT

        /*val spinListaPersonasAdapter = vista.findViewById<Spinner>(R.id.spinPersonaAsignada)

        val spinListaAsignarAdapter = ArrayAdapter<Int>((activity as HomeActivity), android.R.layout.simple_spinner_item, listaF)
        spinListaAsignarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinListaPersonasAdapter.adapter = spinListaAsignarAdapter*/


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

        /*Toast.makeText(activity as HomeActivity,
            "Datos to POST = " +
                "Titulo: $titulo, " +
                "Prioridad: $mPrioridad, " +
                "Fecha inicio: $fechaInicio, " +
                "Fecha fin: $fechaFin, "+
                "Descripcion: $descripcion ",
            Toast.LENGTH_LONG).show()*/

        tarea = Tasks(
            "GRUPOID1",
            "EMIS1",
            "Raul",
            "RECEPT1",
            "Carlos",
            fechaInicio,            //Fecha Inicio
            fechaFin,               //Fecha Fin
            titulo.toString(),      //Titulo
            "EWREWF2323",
            descripcion.toString(), //Descripcion
            mPrioridad.toString(),  //Prioridad
            false,             //Leido
            "2014-01-01"
        )
        asignarTareaViewModel.postTarea(tarea)

    }


    fun abrirDialogoFecha(view: View, b:Int) {
        val newFragment = EdtFecha(this, b)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }

    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {
        anioInicio  = anio
        mesInicio   = mes
        diaInicio   = dia
        val fecha=binding.edtFechaInicio
        fecha.setText("$anio-${mes+1}-$dia")
        fechaInicio = fecha.text.toString()

    }
    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {
        anioFin = anio
        mesFin  = mes
        diaFin  = dia
        val fecha=binding.edtFechaFin
        fecha.setText("$anio-${mes+1}-$dia")
        fechaFin = fecha.text.toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
