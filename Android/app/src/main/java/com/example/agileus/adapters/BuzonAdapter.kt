package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.Models.Buzon
import com.example.agileus.R

class BuzonAdapter(private var dataSet: ArrayList<Buzon>, var tipo: Int) :
    RecyclerView.Adapter<BuzonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val textView1: TextView
        val textView2: TextView
        val textview3: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.dueño)
            textView1 = view.findViewById(R.id.msgcontenido)
            textView2 = view.findViewById(R.id.status)
            textview3 =view.findViewById(R.id.DetalleActividad)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_buzon, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val buzon = dataSet[position]

        if(tipo==1) {
            viewHolder.textView.text =  "Mensaje enviado a : Broadcast"
            viewHolder.textView1.text = "Asunto:  ${buzon.id}"
            viewHolder.textView2.text = "Mensaje: \n ${buzon.message}"
            viewHolder.textview3.text = "Fecha de enviado: ${buzon.fecha}"
        }
        if(tipo==2) {
            viewHolder.textView.text =  "Comunicado dia  : ${buzon.fecha}"
            viewHolder.textView1.text = "Asunto:    ${buzon.asunto}"
            viewHolder.textView2.text = "Mensaje: \n${buzon.message}"
            viewHolder.textview3.text = ""
        }

    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}
