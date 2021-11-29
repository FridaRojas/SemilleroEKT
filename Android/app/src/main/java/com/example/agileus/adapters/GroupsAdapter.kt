package com.example.agileus.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Groups
import com.example.agileus.ui.modulomensajeria.conversationonetoone.ConversationOneToOneActivity


class GroupsAdapter(private var dataSet: ArrayList<Groups>) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_contacts_item, viewGroup, false)

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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNameContact: TextView
        val contexto = view.context

        init {
            // Define click listener for the ViewHolder's View.
            txtNameContact = view.findViewById(R.id.txtNameContact)

        }

        fun enlazarItem(groups: Groups){
            txtNameContact.text = groups.nombreConversacionRecepto

            txtNameContact.setOnClickListener {
                val intent = Intent(contexto, ConversationOneToOneActivity::class.java)
                contexto.startActivity(intent)

            }

        }
    }

}
