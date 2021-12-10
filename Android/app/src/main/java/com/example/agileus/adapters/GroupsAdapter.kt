package com.example.agileus.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Groups
import com.example.agileus.ui.modulomensajeria.conversationonetoone.ConversationOneToOneActivity
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationFragmentDirections
import com.example.agileus.ui.modulotareas.listatareas.TaskFragmentDirections
import com.example.agileus.utils.Constantes
class GroupsAdapter(private var dataSet: ArrayList<Groups>) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_groups_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }
    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNameContact: TextView
        val contexto = view.context
        val myView:View

        init {
            txtNameContact = view.findViewById(R.id.txtNameContact)
            myView = view.findViewById(R.id.idContact)
        }

        fun enlazarItem(groups: Groups){
            txtNameContact.text = groups.nombreConversacionRecepto

            myView.setOnClickListener {
                /*
                val intent = Intent(contexto,ConversationOneToOneActivity::class.java)
                intent.putExtra(Constantes.ID_CHAT, groups.idConversacion)
                intent.putExtra(Constantes.ID_RECEPTOR, groups.idReceptor)
                intent.putExtra(Constantes.NAME_RECEPTOR, groups.nombreConversacionRecepto)
                contexto.startActivity(intent)

                 */

                var action: NavDirections
                action = ListConversationFragmentDirections.actionNavigationHomeToUserConversationFragment(groups.idConversacion,groups.idReceptor,groups.nombreConversacionRecepto, "chatsFragment")
                it.findNavController().navigate(action)
            }

        }
    }

}
