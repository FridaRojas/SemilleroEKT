package com.example.agileus.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulomensajeria.conversationonetoone.ConversationOneToOneActivity
import com.example.agileus.utils.Constantes

class ContactsAdapter(private var dataSet: ArrayList<Contacts>) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_contacts_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.enlazarItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun update(filtrado: List<Contacts>) {
        var array:ArrayList<Contacts> = ArrayList(filtrado)
        dataSet = array
        this.notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNameContact: TextView
        val contexto = view.context
        val myView:View

        init {
            txtNameContact = view.findViewById(R.id.txtNameContact)
            myView = view.findViewById(R.id.idContact)
        }

        fun enlazarItem(contacts: Contacts){
            txtNameContact.text = contacts.nombre

            myView.setOnClickListener {
               val intent = Intent(contexto,ConversationOneToOneActivity::class.java)
                intent.putExtra(Constantes.CHAT_NAME, contacts.id)
                contexto.startActivity(intent)
            }

        }
    }

}
