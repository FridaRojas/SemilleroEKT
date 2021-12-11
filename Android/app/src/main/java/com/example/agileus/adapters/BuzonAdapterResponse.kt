package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.BuzonComunicados
import com.example.agileus.models.BuzonResp
import com.example.agileus.models.Datas
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.USERTYPE

class BuzonAdapterResponse(private var dataSet: ArrayList<Datas>, var tipo: Int) :
    RecyclerView.Adapter<BuzonAdapterResponse.ViewHolder>() {

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

        viewHolder.textView.text = "Mensaje enviado a a Broadcast"
             viewHolder.textView.text =   "Mensaje enviado por ${buzon.idemisor}"
             viewHolder.textView1.text =  "Contenido: ${buzon.texto}  "
             viewHolder.textView2.text =  "Mensaje enviado a ${buzon.idreceptor} "
        }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
