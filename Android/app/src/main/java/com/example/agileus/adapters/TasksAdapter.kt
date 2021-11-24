package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listatareas.TaskFragmentDirections
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener


class TasksAdapter(
    private val dataSet: ArrayList<DataTask>,
    private var listener: TaskListListener
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

    class ViewHolder(view: View, private var listener: TaskListListener) :
        RecyclerView.ViewHolder(view) {
        lateinit var action: NavDirections
        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView
        var btnAbrirDetallesTarea: ImageView
        var nivelUsuario: String = "Bajo"

        init {
            nombreTarea = view.findViewById(R.id.txtNombreTarea)
            personaAsignada = view.findViewById(R.id.txtPersonaAsignada)
            fecha = view.findViewById(R.id.txtFecha)
            btnAbrirDetallesTarea = view.findViewById(R.id.iconoAbrirDetallesTarea)
        }

        fun enlazarItem(dataTask: DataTask) {
            nombreTarea.text = dataTask.titulo
            personaAsignada.text = dataTask.nombreReceptor
//            fecha.text = dataTask.fechaIni.toString()


            btnAbrirDetallesTarea.setOnClickListener {
                if (nivelUsuario.equals("Alto") || nivelUsuario.equals("Medio")) {
                    action =
                        TaskFragmentDirections.actionNavigationDashboardToDetalleNivelAltoFragment(
                            dataTask
                        )
                    it.findNavController().navigate(action)
                } else if (nivelUsuario.equals("Bajo")) {
                    listener.abreDialogo(dataTask)
                }
            }
        }


    }
}
