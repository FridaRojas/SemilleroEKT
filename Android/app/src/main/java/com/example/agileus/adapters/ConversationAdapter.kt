package com.example.agileus.adapters
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.Conversation
import com.example.agileus.providers.DownloadProvider
import com.example.agileus.utils.Constantes
import kotlin.collections.ArrayList

class ConversationAdapter(private var dataSet: ArrayList<Conversation>) :
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.enlazarItem(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun update(filtrado: List<Conversation>) {
        var array:ArrayList<Conversation> = ArrayList(filtrado)
        dataSet = array
        this.notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        val usuario=dataSet[position]
       if(preferenciasGlobal.recuperarIdSesion().equals(usuario.idemisor)){
           return 1
        }else{
            return 2
       }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msg: TextView
        lateinit var FechaMsj:TextView
        lateinit var documento: TextView
        lateinit var myView :View
        lateinit var txtStatusLeido:TextView
        lateinit var id_Emisor : TextView
        var context = view.context

        init {
            msg = view.findViewById(R.id.msg)
            documento = view.findViewById(R.id.txtArchivoadjunto)
            FechaMsj = view.findViewById(R.id.txtFecha)
            myView = view.findViewById(R.id.idMsj)
            txtStatusLeido = view.findViewById(R.id.txtStatusLeido)
            id_Emisor = view.findViewById(R.id.id_Emisor)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun enlazarItem(conversacion:Conversation){
            msg.text = conversacion.texto
            id_Emisor.text = conversacion.nombreEmisor
            txtStatusLeido.text = "Leído: "+ Constantes.devuelveFechaHora(conversacion.fechaLeido)
            if(conversacion.idconversacion.length > 50){
                if(!conversacion.idemisor.equals(preferenciasGlobal.recuperarIdSesion())){
                    id_Emisor.isVisible = true
                }else{
                    id_Emisor.isVisible = false
                }

            }else{
                id_Emisor.isVisible = false
            }

            if(conversacion.statusLeido == true && conversacion.idemisor.equals(preferenciasGlobal.recuperarIdSesion())){
                txtStatusLeido.isVisible = true
            }
            else{
                txtStatusLeido.isVisible = false
            }

            FechaMsj.text = "Enviado: "+Constantes.devuelveFechaHora(conversacion.fechaCreacion)

            if(conversacion.texto.equals("Documento")){
                msg.isVisible = false
                msg.isEnabled = false
                documento.isVisible = true
                documento.isEnabled = true
            }else{
                msg.isVisible = true
                msg.isEnabled = true
                documento.isVisible = false
                documento.isEnabled = false
            }

            myView.setOnClickListener {
                if(conversacion.texto.equals("Documento")){
                    var dowloadFile = DownloadProvider()
                    dowloadFile.dowloadFile(context, "${conversacion.rutaDocumento}", "AgilUsDocument ${Constantes.devuelveFechaDocumento()}.")
                }

            }

        }
    }

}
