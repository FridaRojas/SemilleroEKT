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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView

        init {
            // Define click listener for the ViewHolder's View.
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

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //  viewHolder.textView.text = dataSet[position]
        viewHolder.enlazarItem(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
