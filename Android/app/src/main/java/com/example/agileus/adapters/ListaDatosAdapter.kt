package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Estadisticas
import com.example.agileus.providers.ReportesListener
import com.example.agileus.utils.Constantes


class ListaDatosAdapter(private val dataSet: ArrayList<Estadisticas>, val listener: ReportesListener) :
    RecyclerView.Adapter<ListaDatosAdapter.ViewHolder>(){

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

       var id_vista=0

        if(Constantes.vista==0){
            id_vista = if(viewType == 0)
                R.layout.item_selected
                else
                R.layout.item
        }
        else{
            id_vista = if(viewType == 0)
                R.layout.item
            else
                R.layout.item_selected
        }

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(id_vista, viewGroup, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position],position,listener)
    }

    override fun getItemCount() = dataSet.size


    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

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
            imagen = view.findViewById(R.id.iconolista)

        }


        fun enlazarItem(datos: Estadisticas, position: Int, listener: ReportesListener) {

            dato1.text = datos.dato1
            dato2.text = datos.dato2
            dato3.text = datos.dato3
            dato4.text = datos.dato4
            imagen.setImageResource(datos.imagen)

            view.setOnClickListener {

                listener.cambiarGrafica(position)
                Constantes.vista=position

            }

        }
    }





}