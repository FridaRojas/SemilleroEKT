package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.BuzonResp
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.USERTYPE

class BuzonAdapter(private var dataSet: ArrayList<BuzonResp>, var tipo: Int) :
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
            if(USERTYPE == "Broadcast" ) {
                viewHolder.textView.text = "Mensaje enviado por :Broadcast "
            }
            else {
                viewHolder.textView.text = "Mensaje enviado a : Broadcast"
            }
            viewHolder.textView1.text = "Asunto:  ${buzon.Asunto}"
            viewHolder.textView2.text = "Mensaje: \n ${buzon.Message}"
        }
        if(tipo==2) {

            if (USERTYPE == "Broadcast") {
                viewHolder.textView.text = "Mensaje enviado a :${buzon.Receiverid} "
            }
            if(buzon.Receiverid =="General"){
                viewHolder.textView.text = "Comunicado:    ${buzon.Receiverid}"
            }
            viewHolder.textView1.text = "Asunto:    ${buzon.Asunto}"
            viewHolder.textView2.text = "Mensaje: \n${buzon.Message}"
        }
    }
    override fun getItemCount(): Int {
        return dataSet.size
    }
}
