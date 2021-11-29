package com.example.agileus.adapters

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.agileus.R
import com.example.agileus.models.Conversation
import com.example.agileus.utils.Constantes
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
       if(Constantes.id.equals(usuario.idemisor)){
           return 1
        }else{
            return 2
       }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val msgEmisor: TextView
        lateinit var FechaMsj:TextView
        lateinit var documento: TextView
        lateinit var myView :View
        var context = view.context

        init {
            msgEmisor = view.findViewById(R.id.msgEmisor)
            documento = view.findViewById(R.id.txtArchivoadjunto)
            FechaMsj = view.findViewById(R.id.txtFecha)
            myView = view.findViewById(R.id.idMsj)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun enlazarItem(conversacion:Conversation){
            msgEmisor.text = conversacion.texto

            val formatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
            val text: String = conversacion.fechaCreacion.format(formatter)
            val parsedDate: LocalDate = LocalDate.parse(text, formatter)
            FechaMsj.text = parsedDate.toString()

            if(conversacion.texto.equals("Documento")){
                msgEmisor.isVisible = false
                msgEmisor.isEnabled = false
            }else{
                msgEmisor.isVisible = true
                msgEmisor.isEnabled = true
                documento.isVisible = false
                documento.isEnabled = false
            }

            myView.setOnClickListener {
                if(conversacion.texto.equals("Documento")){
                    val file: File = File(context.getExternalFilesDir(null), "file")
                    val request = DownloadManager.Request(Uri.parse(conversacion.rutaDocumento))
                        .setTitle("${conversacion.texto}${(0..99999).random()}")
                        .setDescription("Download")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationUri(Uri.fromFile(file))
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    downloadManager.enqueue(request)
                }

            }

        }
    }

}
