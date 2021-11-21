package com.example.agileus.ui.modulotareas.creartareas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.databinding.FragmentFormularioCrearTareasBinding
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFinFecha
import com.example.agileus.ui.modulotareas.dialogostareas.EdtInicioFecha
import com.example.agileus.ui.modulotareas.listatareas.DashboardFragmentDirections
import com.example.agileus.ui.modulotareas.listatareas.DashboardViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosTareasListener

private const val ARG_PARAM1 = "param1"



class FormularioCrearTareasFragment : Fragment(), DialogosTareasListener {

    private lateinit var crearTareasViewModel: DashboardViewModel

    private var _binding: FragmentFormularioCrearTareasBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null

    var fechaInicio    : String = ""
    var fechaFin       : String = ""


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

        _binding = FragmentFormularioCrearTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUiTareas()

        /* Boton Crear tarea  */
        binding.btnCrearTarea.setOnClickListener {

            if(fechaInicio=="" || fechaFin=="" ||
                binding.edtAgregaTitulo.text.toString().isNullOrEmpty()||
                binding.edtDescripcion.text.toString().isNullOrEmpty()
                    ){

                Toast.makeText(activity as HomeActivity, "Faltan datos por agregar", Toast.LENGTH_SHORT).show()

            }else{

                val titulo      = binding.edtAgregaTitulo.text
                val descripcion = binding.edtDescripcion.text
                val mPrioridad  = binding.spinPrioridad.selectedItem

                Toast.makeText(activity as HomeActivity,

                    "Datos POST = " +
                        "Titulo: $titulo, " +
                        "Prioridad: $mPrioridad, " +
                        "Fecha inicio: $fechaInicio, " +
                        "Fecha fin: $fechaFin, "+
                        "Descripcion: $descripcion ",

                    Toast.LENGTH_LONG).show()

                val action = FormularioCrearTareasFragmentDirections.actionFormularioCrearTareasFragmentToNavigationDashboard()
                findNavController().navigate(action)

            }



        }
        /* Boton Crear tarea  */

        binding.edtFechaInicio.setOnClickListener {
            abrirDialogoFechaInicio(view)
        }
        binding.edtFechaFin.setOnClickListener {
            abrirDialogoFechaFin(view)
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

    fun crearTarea(){
        // POST
    }
    fun setUpUiTareas(){
        // SPINER CON RECURSO XML
        val spinPrioridadAdapter = ArrayAdapter.createFromResource(activity as HomeActivity, R.array.prioridad_array, android.R.layout.simple_spinner_item)
        spinPrioridadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinPrioridad.adapter=spinPrioridadAdapter
    }
    fun abrirDialogoFechaInicio(view: View) {
        val newFragment = EdtInicioFecha(this)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }
    fun abrirDialogoFechaFin(view: View) {
        val newFragment = EdtFinFecha(this)
        newFragment.show(parentFragmentManager, "Edt fecha")
    }
    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {
        val fecha=binding.edtFechaInicio
        fecha.setText("$anio/${mes+1}/$dia")
        fechaInicio = fecha.text.toString()
        Toast.makeText(activity as HomeActivity, "$fechaInicio", Toast.LENGTH_SHORT).show()
    }
    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {
        val fecha=binding.edtFechaFin
        fecha.setText("$anio/${mes+1}/$dia")
        fechaFin = fecha.text.toString()
        Toast.makeText(activity as HomeActivity, "$fechaFin", Toast.LENGTH_SHORT).show()
    }


}
