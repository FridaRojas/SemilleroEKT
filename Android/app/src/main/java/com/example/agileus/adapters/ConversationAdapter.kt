package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Conversation
import com.example.agileus.utils.Constantes


class ConversationAdapter(private val dataSet: ArrayList<Conversation>) :
    RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        var id_vista = if
                (viewType == 1){
            R.layout.conversation_emisor_item
        } else{
            R.layout.conversation_receptor_item
        }

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(id_vista, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.enlazarItem(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int): Int {
        val usuario=dataSet[position]
       if(Constantes.id.equals(usuario.idemisor)){
           return 1
        }else{
            return 2
       }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msgEmisor: TextView

        init {
            // Define click listener for the ViewHolder's View.
            msgEmisor = view.findViewById(R.id.msgEmisor)

        }

        fun enlazarItem(conversacion:Conversation){
            msgEmisor.text = conversacion.texto
        }
    }

}
