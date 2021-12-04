package com.example.agileus.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listatareas.TaskFragmentDirections
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel.Companion.status
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TasksAdapter(
    private var dataSet: ArrayList<DataTask>,
    private val listener: TaskListListener
) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun update(filtro: ArrayList<DataTask>) {
        dataSet = filtro
        this.notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val listener: TaskListListener) :
        RecyclerView.ViewHolder(view) {

        private lateinit var taskViewModel: TaskViewModel

        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView
        var prioridad: TextView
        var btnAbrirDetallesTarea: ImageView
        var nivelUsuario: String = ""

        init {
            nombreTarea = view.findViewById(R.id.txtNombreTarea)
            personaAsignada = view.findViewById(R.id.txtPersonaAsignada)
            prioridad = view.findViewById(R.id.txtPrioridad)
            fecha = view.findViewById(R.id.txtFecha)
            btnAbrirDetallesTarea = view.findViewById(R.id.iconoAbrirDetallesTarea)
        }


        fun enlazarItem(dataTask: DataTask) {
            nombreTarea.text = dataTask.titulo
            personaAsignada.text = dataTask.nombreReceptor
            prioridad.text = "Prioridad: ${dataTask.prioridad.capitalize()}"

            var fechaInicio = formatoFecha(dataTask.fechaIni)
            fecha.text = fechaInicio


            //Log.d("status", status)
            btnAbrirDetallesTarea.setOnClickListener {
                if (status == "asignada") {
                    var action: NavDirections
                    action =
                        TaskFragmentDirections.actionNavigationDashboardToDetalleNivelAltoFragment(
                            dataTask
                        )
                    it.findNavController().navigate(action)
                } else {
                    listener.abreDialogo(dataTask)
                }
            }
        }

        fun formatoFecha(fecha: Date): String {

            var mesI: String = ""
            var diaI: String = ""
            var fechaIn: String = ""
            val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

            val cal = Calendar.getInstance()
            var fechaI = sdf3.parse(fecha.toString())

            cal.time = fechaI

            cal[Calendar.MONTH] + 1
//        cal[Calendar.DATE] + 1
            if (cal[Calendar.MONTH] < 10) {
                mesI = "0${cal[Calendar.MONTH]}"
            } else {
                mesI = cal[Calendar.MONTH].toString()
            }

            if (cal[Calendar.DATE] < 10) {
                diaI = "0${cal[Calendar.DATE]}"
            } else {
                diaI = cal[Calendar.DATE].toString()
            }

            fechaIn =
                cal[Calendar.YEAR].toString() + "-" + mesI + "-" + diaI

            return fechaIn
        }
    }
}
