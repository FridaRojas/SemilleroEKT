package com.example.agileus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.StatusTasks
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener

class StatusTasksAdapter(private var dataSet: ArrayList<StatusTasks>, val listener:TaskDialogListener) :
    RecyclerView.Adapter<StatusTasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        lateinit var view:View
        if(viewType == 1){
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.status_task_item, viewGroup, false)
        }else{
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.status_task_item_noselect, viewGroup, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.statusTextView.text = dataSet[position].status
        viewHolder.statusTextView.setOnClickListener {
            for(estatus in dataSet){
                estatus.isSelected = false
            }
            dataSet[position].isSelected = true
            update(dataSet)
            listener.getTaskByStatus(dataSet[position].status)
            //Toast.makeText(viewHolder.context, "${dataSet[position].status}", Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount() = dataSet.size

    fun update(datos:ArrayList<StatusTasks>){
        this.dataSet = datos
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        //el 1 representa seleccionado
        return if(dataSet[position].isSelected){
            1
        } else{
            0
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusTextView: TextView
        val context = view.context

        init {
            statusTextView = view.findViewById(R.id.statusTextView)
        }

        fun enlazarItem(datos:String, listener: TaskDialogListener){

            statusTextView.text = datos

            statusTextView.setOnClickListener {
                listener.getTaskByStatus(datos)


            }

        }

        fun statusSelected(status: String, listener: TaskDialogListener) {
            var listaRecyclerStatus = context.resources.getStringArray(R.array.statusRecycler_array)

            if(status == listaRecyclerStatus[0]){
                statusTextView.setTextColor(context.resources.getColor(R.color.white))
                statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            }

        }
    }

}