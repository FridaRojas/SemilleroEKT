package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Tasks


class TasksAdapter(private val dataSet: ArrayList<Tasks>) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView

        init {
            nombreTarea = view.findViewById(R.id.txtNombreTarea)
            personaAsignada = view.findViewById(R.id.txtPersonaAsignada)
            fecha = view.findViewById(R.id.txtFecha)
        }

        fun enlazarItem(task: Tasks) {
            nombreTarea.text = task.titulo
            personaAsignada.text = task.nombreReceptor
            fecha.text = task.fechaFin
        }
    }

}
