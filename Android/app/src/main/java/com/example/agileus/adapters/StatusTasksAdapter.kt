package com.example.agileus.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel.Companion.statusList
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener

class StatusTasksAdapter(private var dataSet: Array<String>, val listener:TaskDialogListener) :
    RecyclerView.Adapter<StatusTasksAdapter.ViewHolder>() {

    var isSelected:Boolean = false

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.status_task_item, viewGroup, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position], listener)
    }

    override fun getItemCount() = dataSet.size



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


        fun statusSelected() {
            var listaRecyclerStatus = context.resources.getStringArray(R.array.statusRecycler_array)

            when (statusList) {
                listaRecyclerStatus[0] -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.white))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                listaRecyclerStatus[1] -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.white))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                listaRecyclerStatus[2] -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.white))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                listaRecyclerStatus[3] -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.white))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                listaRecyclerStatus[4] -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.white))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
                else -> {
                    statusTextView.setTextColor(context.resources.getColor(R.color.black))
                    statusTextView.setBackgroundColor(context.resources.getColor(R.color.white))
                }
            }

        }
    }

    fun update(filtro: ArrayList<String>) {
        dataSet = filtro as Array<String>
        this.notifyDataSetChanged()
    }

}