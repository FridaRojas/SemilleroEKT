package com.example.agileus.adapters

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
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener


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
        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView
        var prioridad: TextView
        var btnAbrirDetallesTarea: ImageView
        var nivelUsuario: String = "Alto"

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
            prioridad.text = dataTask.prioridad
//            fecha.text = dataTask.fechaIni.toString()


            btnAbrirDetallesTarea.setOnClickListener {
                if (nivelUsuario.equals("Alto") || nivelUsuario.equals("Medio")) {
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
    }
}
