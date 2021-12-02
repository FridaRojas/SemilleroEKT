package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.ActivityConversationOneToOneBinding
import com.example.agileus.models.Message
import com.example.agileus.models.StatusRead
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.utils.Constantes
import java.io.FileNotFoundException


class ConversationOneToOneActivity : AppCompatActivity() {

    lateinit var binding:ActivityConversationOneToOneBinding
    lateinit var conversationviewModel:ConversationViewModel
    lateinit var resultLauncherArchivo: ActivityResultLauncher<Intent>
    lateinit var firebaseProvider : FirebaseProvider
    lateinit var id_receptor :String
    lateinit var id_chat:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationOneToOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseProvider  = FirebaseProvider()

        conversationviewModel = ViewModelProvider(this).get()
        id_chat = Constantes.idChat
        id_chat = intent.getStringExtra(Constantes.ID_CHAT).toString()
        id_receptor = intent.getStringExtra(Constantes.ID_RECEPTOR).toString()

        conversationviewModel.devuelveLista(id_chat)

        conversationviewModel.responseM.observe(this,{
            id_chat = it.data
            conversationviewModel.devuelveLista(id_chat)
        })

        var background = object : Thread(){
            override fun run() {
                while (true){
                    Log.i("chechar","checar")
                    conversationviewModel.devuelveLista(id_chat)
                    sleep(3000)
                }
            }
        }.start()

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
            conversationviewModel.mandarMensaje(id_chat,Message(Constantes.id,id_receptor,"$it","Documento",Constantes.finalDate))
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
                    conversationviewModel.mandarMensaje(id_chat,Message(Constantes.id,id_receptor,"","${binding.etMensaje.text.toString()}",Constantes.finalDate))
                    binding.etMensaje.setText("")
                }
            }catch (ex:Exception){
            }
            }
        }
    }