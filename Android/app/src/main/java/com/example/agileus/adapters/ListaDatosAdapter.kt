package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Estadisticas

class ListaDatosAdapter(private val dataSet: ArrayList<Estadisticas>) :
    RecyclerView.Adapter<ListaDatosAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dato1: TextView
        val dato2: TextView
        val dato3: TextView
        val dato4: TextView
        val imagen: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            dato1 = view.findViewById(R.id.dato1)
            dato2 = view.findViewById(R.id.dato2)
            dato3 = view.findViewById(R.id.dato3)
            dato4 = view.findViewById(R.id.dato4)
            imagen=view.findViewById(R.id.iconolista)
        }

        fun enlazarItem(datos: Estadisticas){

                dato1.text =  datos.dato1
                dato2.text =  datos.dato2
                dato3.text =  datos.dato3
                dato4.text =  datos.dato4
                imagen.setImageResource(datos.imagen)

        }

    }





}