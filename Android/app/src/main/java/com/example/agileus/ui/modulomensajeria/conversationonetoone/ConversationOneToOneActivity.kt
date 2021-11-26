package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.ActivityConversationOneToOneBinding
import com.example.agileus.models.Message
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.utils.Constantes
import java.io.File
import java.io.FileNotFoundException


class ConversationOneToOneActivity : AppCompatActivity() {

    lateinit var binding:ActivityConversationOneToOneBinding
    lateinit var conversationviewModel:ConversationViewModel
    lateinit var resultLauncherArchivo: ActivityResultLauncher<Intent>
    lateinit var firebaseProvider : FirebaseProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationOneToOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        conversationviewModel = ViewModelProvider(this).get()
        conversationviewModel.devuelveLista(Constantes.idChat)

        conversationviewModel.adaptador.observe(this,{
            binding.recyclerConversacion.adapter = it
            binding.recyclerConversacion.layoutManager = LinearLayoutManager(this)

        })

        firebaseProvider  = FirebaseProvider()

        resultLauncherArchivo =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== Activity.RESULT_OK){
                val data: Intent?=result.data
                if(data!= null){
                    try{
                        var returnUri = data?.data!!
                        val uriString = data.toString()

                        val myFile = File(uriString)
                        Log.d("mensaje","PDF: $uriString")
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
            var mensaje = Message(Constantes.id,"618b05c12d3d1d235de0ade0","","$it",Constantes.finalDate)
            conversationviewModel.mandarMensaje(Constantes.idChat,mensaje)
        })

        binding.btnArchivoAdjunto.setOnClickListener {
            val intent= Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            resultLauncherArchivo.launch(intent)
        }

        binding.btnEnviarMensaje.setOnClickListener {
            var mensaje = Message(Constantes.id,"618b05c12d3d1d235de0ade0","","${binding.etMensaje.text.toString()}",Constantes.finalDate)
            conversationviewModel.mandarMensaje(Constantes.idChat,mensaje)
            binding.etMensaje.setText("")
            }
        }
    }