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
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFinFecha
import com.example.agileus.ui.modulotareas.dialogostareas.EdtInicioFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosTareasListener

private const val ARG_PARAM1 = "param1"

class FormularioCrearTareasFragment : Fragment(), DialogosTareasListener {

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

        var tarea: Tasks

        setUpUiAsignarTareas()

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {

            if(fechaInicio=="" || fechaFin=="" ||
                binding.edtAgregaTitulo.text.toString().isNullOrEmpty()||
                binding.edtDescripcion.text.toString().isNullOrEmpty()){

                Toast.makeText(activity as HomeActivity, "Faltan datos por agregar", Toast.LENGTH_SHORT).show()

            }else{

                if(anioInicio<=anioFin && mesInicio<=mesFin && diaInicio<=diaFin){

                    val titulo      = binding.edtAgregaTitulo.text
                    val descripcion = binding.edtDescripcion.text
                    val mPrioridad  = binding.spinPrioridad.selectedItem

                    Toast.makeText(activity as HomeActivity,
                        "Datos to POST = " +
                            "Titulo: $titulo, " +
                            "Prioridad: $mPrioridad, " +
                            "Fecha inicio: $fechaInicio, " +
                            "Fecha fin: $fechaFin, "+
                            "Descripcion: $descripcion ",
                        Toast.LENGTH_LONG).show()

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
                }else{
                    Toast.makeText(FormularioCrearTareasFragment as HomeActivity,
                        "La fecha de Vencimiento no puede ser anterior a la fecha de inicio",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        /* Boton Crear tarea  */

        binding.edtFechaInicio.setOnClickListener {
            abrirDialogoFechaInicio(view,1)
        }
        binding.edtFechaFin.setOnClickListener {
            abrirDialogoFechaInicio(view,2)
            //abrirDialogoFechaFin(view)
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
    fun abrirDialogoFechaInicio(view: View, b:Int) {
        val newFragment = EdtInicioFecha(this, b)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }
    fun abrirDialogoFechaFin(view: View) {
        val newFragment = EdtFinFecha(this)
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
