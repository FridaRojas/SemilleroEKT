package com.example.agileus.adapters

import android.app.Activity
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
import com.example.agileus.models.Tasks
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.modulotareas.detalletareas.DialogoNivelBajo
import com.example.agileus.ui.modulotareas.listatareas.TaskFragmentDirections
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosFormularioCrearTareasListener


class TasksAdapter(
    private val dataSet: ArrayList<Tasks>,
    val listener: DialogosFormularioCrearTareasListener
) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item, viewGroup, false)

        return ViewHolder(view, listener)
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

    class ViewHolder(view: View, val listener: DialogosFormularioCrearTareasListener) :
        RecyclerView.ViewHolder(view) {
        var nombreTarea: TextView
        var personaAsignada: TextView
        var fecha: TextView
        var btnAbrirDetallesTarea: ImageView
        var nivelUsuario: String = "Alto"

        init {
            // Define click listener for the ViewHolder's View.
            nombreTarea = view.findViewById(R.id.txtNombreTarea)
            personaAsignada = view.findViewById(R.id.txtPersonaAsignada)
            fecha = view.findViewById(R.id.txtFecha)
            btnAbrirDetallesTarea = view.findViewById(R.id.iconoAbrirDetallesTarea)
        }

        fun enlazarItem(task: Tasks) {
            nombreTarea.text = task.titulo
            personaAsignada.text = task.nombreReceptor
            fecha.text = task.fechaFin

            btnAbrirDetallesTarea.setOnClickListener {
                if (nivelUsuario.equals("Alto") || nivelUsuario.equals("Medio")) {
                    Toast.makeText(itemView.context, "Abrimos Fragment", Toast.LENGTH_SHORT).show()
                    var action: NavDirections
                    action =
                        TaskFragmentDirections.actionNavigationDashboardToDetalleNivelAltoFragment(
                            task)
                    it.findNavController().navigate(action)
                } else {
                    Toast.makeText(itemView.context, "Abrimos Dialogo", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}
