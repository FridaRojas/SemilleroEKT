package com.example.agileus.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Chats
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationFragmentDirections


class ChatsAdapter(private var dataSet: ArrayList<Chats>) :
    RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_contacts_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }
    override fun getItemCount() = dataSet.size

    fun update(filtrado: ArrayList<Chats>) {
        var array:ArrayList<Chats> = ArrayList(filtrado)
        dataSet = array
        this.notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNameContact: TextView
        val txtRol: TextView
        val myView: View

        init {
            txtNameContact = view.findViewById(R.id.txtNameContact)
            myView = view.findViewById(R.id.idContact)
            txtRol = view.findViewById(R.id.txtRol)
        }

        fun enlazarItem(chats: Chats){
            txtNameContact.text = chats.nombreConversacionRecepto
            txtRol.text = chats.nombreRol

            myView.setOnClickListener {
                var action: NavDirections
                action = ListConversationFragmentDirections.actionNavigationHomeToUserConversationFragment(chats.idConversacion,chats.idReceptor,chats.nombreConversacionRecepto, R.string.chats_name.toString())
                it.findNavController().navigate(action)
            }
        }
    }

}
