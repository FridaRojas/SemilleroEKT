package com.example.agileus.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.DataBuzon
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.USERTYPE
import com.google.android.material.snackbar.Snackbar

class BuzonAdapter(private var dataSet: ArrayList<DataBuzon>, var tipo: Int) :
    RecyclerView.Adapter<BuzonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val textView1: TextView
        val textView2: TextView
        val textView3: TextView
        val chkbox:CheckBox

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.dueño)
            textView1 = view.findViewById(R.id.msgcontenido)
            textView2 = view.findViewById(R.id.status)
            textView3 = view.findViewById(R.id.DetalleActividad)
            chkbox = view.findViewById(R.id.DetalleActividad1)
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

                viewHolder.textView.text =  "Mensaje enviado por : ${buzon.nombreEmisor} "
                viewHolder.textView1.text = "Contenido:  ${buzon.descripcion}"

                viewHolder.chkbox.setOnClickListener {
                    Log.d("caja", buzon.atendido.toString())
//                viewHolder.textView2.text = "Atendido: ${buzon.atendido} "
                }
        if(tipo==2) {

            if (USERTYPE == "Broadcast") {
                viewHolder.textView.text = "Mensaje enviado a :${buzon.idReceptor} "
            }
            if(buzon.idReceptor =="General"){
                viewHolder.textView.text = "Comunicado:    ${buzon.descripcion}"
            }
            viewHolder.textView1.text = "idRec:    ${buzon.idReceptor}"
            viewHolder.textView2.text = ""
        }
    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}
