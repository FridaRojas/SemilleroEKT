package com.example.agileus.ui.modulomensajeria.conversation

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.databinding.UserConversationFragmentBinding
import com.example.agileus.models.Message
import com.example.agileus.models.StatusRead
import com.example.agileus.providers.FirebaseProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.ui.modulomensajeria.listcontacts.ListContactsViewModel
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import com.example.agileus.utils.Constantes
import java.io.FileNotFoundException

class UserConversationFragment : Fragment() {

    private var _binding: UserConversationFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var chatsviewmodel: ListConversationViewModel
    lateinit var contactsViewModel: ListContactsViewModel
    lateinit var resultLauncherArchivo: ActivityResultLauncher<Intent>
    lateinit var firebaseProvider : FirebaseProvider


    private lateinit var viewModel: UserConversationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UserConversationFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserConversationViewModel::class.java)
        chatsviewmodel = ViewModelProvider(this).get(ListConversationViewModel::class.java)
        contactsViewModel = ViewModelProvider(this).get(ListContactsViewModel::class.java)
        val args: UserConversationFragmentArgs by navArgs()

        var idChatUser = args.idConversationUser.toString()
        var idReceptor = args.idreceptor.toString()
        var nameReceptor = args.namereceptor.toString()
        var flagFragment = args.banderaFragment.toString()

        firebaseProvider  = FirebaseProvider()

        var UserId = InitialApplication.preferenciasGlobal.recuperarIdSesion()

        (activity as HomeActivity).supportActionBar!!.title = nameReceptor
        (activity as HomeActivity).fragmentSeleccionado = flagFragment

        contactsViewModel.devuelveLista(UserId)

        contactsViewModel.contactos.observe(viewLifecycleOwner,{
            for(chats in it) {
                if (idReceptor.length < 50) {
                    if (chats.id.equals(idReceptor)) {
                        binding.errorchat.visibility = View.GONE
                        binding.btnSendMessage.isEnabled = true
                        binding.btnArchivoAdj.isEnabled = true
                    }
                }
                else {
                    binding.errorchat.visibility = View.GONE
                    binding.btnSendMessage.isEnabled = true
                    binding.btnArchivoAdj.isEnabled = true
                }
            }
        })
        ////////

        viewModel.devuelveLista(UserId,idChatUser)

        chatsviewmodel.devuelveListaChats(UserId)
        chatsviewmodel.chatsdeUsuario.observe(viewLifecycleOwner,{
            for (user in it){
                if(user.idReceptor.equals(idReceptor)){
                    idChatUser = user.idConversacion
                   viewModel.devuelveLista(UserId,idChatUser)
                }
            }
        })

        viewModel.responseM.observe(viewLifecycleOwner,{
            idChatUser = it.data
            viewModel.devuelveLista(UserId, idChatUser)
        })


        var background = object : Thread(){
            override fun run() {
                while (true){
                    viewModel.devuelveLista(UserId, idChatUser)
                    sleep(5000)
                }
            }
        }.start()

        viewModel.actualizar.observe(viewLifecycleOwner,{
            for( valor in it){
                if(valor.idemisor!=UserId&& valor.statusLeido == false){
                    viewModel.statusUpdateMessage(UserId, StatusRead(valor.id,
                        Constantes.finalDate)
                    )
                }
            }

        })


/////////
        viewModel.devuelveLista(UserId,idChatUser)
        viewModel.adaptador.observe(viewLifecycleOwner,{
            binding.progressConversation.isVisible = false
            binding.recyclerUserConversacion.adapter = it
            binding.recyclerUserConversacion.layoutManager = LinearLayoutManager(activity)
            binding.recyclerUserConversacion.getLayoutManager()?.scrollToPosition(viewModel.listaConsumida.size-1)
        })

        /////////
        viewModel.bandera.observe(viewLifecycleOwner,{
            binding.progressConversation.isVisible = false
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
               Toast.makeText(activity,"No se selecciono un archivo", Toast.LENGTH_LONG).show()
            }
        }

        firebaseProvider.obs.observe(viewLifecycleOwner,{
            viewModel.mandarMensaje(UserId, idChatUser,
                Message(UserId,idReceptor,"$it","Documento",Constantes.finalDate)
            )
        })

        binding.btnArchivoAdj.setOnClickListener {
            val intent= Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            resultLauncherArchivo.launch(intent)
        }

        binding.btnSendMessage.setOnClickListener {
            try {
                if (binding.etMensaje.text.isNullOrEmpty()) {

                } else {
                    viewModel.mandarMensaje(
                        UserId, idChatUser,
                        Message(
                            UserId,
                            idReceptor,
                            "",
                            "${binding.etMensaje.text.toString()}",
                            Constantes.finalDate
                        )
                    )
                    binding.etMensaje.setText("")
                }
            } catch (ex: Exception) {
            }

        }
    }

}