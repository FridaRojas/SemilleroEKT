package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.ActivityConversationOneToOneBinding
import com.example.agileus.models.Message
import com.example.agileus.models.StatusRead
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.ui.modulomensajeria.listcontacts.ListContactsViewModel
import com.example.agileus.utils.Constantes
import java.io.FileNotFoundException


class ConversationOneToOneActivity : AppCompatActivity() {

    lateinit var binding:ActivityConversationOneToOneBinding
    lateinit var conversationviewModel:ConversationViewModel
    lateinit var chatsviewmodel:ListConversationViewModel
    lateinit var contactsViewModel:ListContactsViewModel
    lateinit var resultLauncherArchivo: ActivityResultLauncher<Intent>
    lateinit var firebaseProvider : FirebaseProvider
    lateinit var id_receptor :String
    lateinit var id_chat:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationOneToOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        firebaseProvider  = FirebaseProvider()

        conversationviewModel = ViewModelProvider(this).get()
        chatsviewmodel = ViewModelProvider(this).get()
        contactsViewModel = ViewModelProvider(this).get()

        id_chat = intent.getStringExtra(Constantes.ID_CHAT).toString()
        id_receptor = intent.getStringExtra(Constantes.ID_RECEPTOR).toString()
        var name_receptor = intent.getStringExtra(Constantes.NAME_RECEPTOR)

        this.setTitle(name_receptor)

        conversationviewModel.devuelveLista(Constantes.id,id_chat)

        chatsviewmodel.devuelveListaChats(Constantes.id)
        chatsviewmodel.chatsdeUsuario.observe(this,{
            for (user in it){
                if(user.idReceptor.equals(id_receptor)){
                    id_chat = user.idConversacion
                    conversationviewModel.devuelveLista(Constantes.id,id_chat)
                }
            }
        })

        contactsViewModel.devuelveLista(Constantes.id)
        contactsViewModel.contactos.observe(this,{
            for(chats in it){
                if(chats.id.equals(id_receptor)){
                    binding.errordechat.isVisible = true
                    binding.btnArchivoAdjunto.isEnabled = false
                    binding.btnEnviarMensaje.isEnabled = false
                }else{
                    binding.errordechat.isVisible = false
                    binding.btnArchivoAdjunto.isEnabled = true
                    binding.btnEnviarMensaje.isEnabled = true
                }
            }
        })

        conversationviewModel.responseM.observe(this,{
            id_chat = it.data
            conversationviewModel.devuelveLista(Constantes.id, id_chat)
        })


        var background = object : Thread(){
            override fun run() {
                while (true){
                    conversationviewModel.devuelveLista(Constantes.id, id_chat)
                    sleep(5000)
                }
            }
        }.start()

        conversationviewModel.actualizar.observe(this,{
            for( valor in it){
                if(valor.idemisor!=Constantes.id && valor.statusLeido == false){
                    conversationviewModel.statusUpdateMessage(Constantes.id, StatusRead(valor.id,Constantes.finalDate))
                }
            }

        })

        conversationviewModel.adaptador.observe(this,{
                    binding.recyclerConversacion.adapter = it
                    binding.recyclerConversacion.layoutManager = LinearLayoutManager(this)
            binding.recyclerConversacion.getLayoutManager()?.scrollToPosition(conversationviewModel.listaConsumida.size-1)

        })

        resultLauncherArchivo =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                val data: Intent?=result.data
                if(data!= null){
                    try{
                        var returnUri = data?.data!!
                        firebaseProvider.subirPdfFirebase(returnUri,Constantes.referenciaMensajeria, "documento${(0..999).random()}")

                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        Log.e("mensaje", "File not found. ${e.message}");
                    }
                }
            }else{
                Toast.makeText(applicationContext,"No se selecciono un archivo", Toast.LENGTH_LONG).show()
            }
        }

        firebaseProvider.obs.observe(this,{
            conversationviewModel.mandarMensaje(Constantes.id, id_chat,Message(Constantes.id,id_receptor,"$it","Documento",Constantes.finalDate))
        })

        binding.btnArchivoAdjunto.setOnClickListener {
            val intent= Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            resultLauncherArchivo.launch(intent)
        }

        binding.btnEnviarMensaje.setOnClickListener {
            try{
                if(binding.etMensaje.text.isNullOrEmpty()){

                }else{
                    conversationviewModel.mandarMensaje(Constantes.id, id_chat,Message(Constantes.id,id_receptor,"","${binding.etMensaje.text.toString()}",Constantes.finalDate))
                    binding.etMensaje.setText("")
                }
            }catch (ex:Exception){
            }
            }
        }
    }