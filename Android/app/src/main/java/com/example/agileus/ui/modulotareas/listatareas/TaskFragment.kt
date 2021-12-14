package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.databinding.FragmentTaskBinding
import com.example.agileus.models.DataTask
import com.example.agileus.models.StatusTasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.iniciosesion.InicioSesionViewModel
import com.example.agileus.ui.login.iniciosesion.InicioSesionViewModel.Companion.userBoss
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoAceptar
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoNivelBajo
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener


class TaskFragment : Fragment(), TaskDialogListener, TaskListListener, dialogoConfirmarListener {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    lateinit var adaptadorStatus : StatusTasksAdapter

    private lateinit var taskViewModel: TaskViewModel
    //var nivelusuario = "medio"
    lateinit var listStatus : Array<String>
    lateinit var list:Array<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        taskViewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recuperarNivelUsuario()
         preferenciasGlobal.recuperarNivelUsuario()
        (activity as HomeActivity?)?.getActionBar()?.setTitle("Hola StackOverflow en Español")

        listStatus = resources.getStringArray(R.array.statusRecycler_array)
        list = resources.getStringArray(R.array.status_array)

        //Toast.makeText(activity, userBoss, Toast.LENGTH_SHORT).show()

        var superior = preferenciasGlobal.recuperarIdSuperiorInmediato()

            if(superior.isNullOrEmpty() || superior == ""){
                preferenciasGlobal.guardarNivelUsuario(getString(R.string.nivelAlto))
            }else{
                if(userBoss.equals(getString(R.string.nivelAlto))){
                    preferenciasGlobal.guardarNivelUsuario(getString(R.string.nivelMedio))
                }else if(userBoss.equals(getString(R.string.nivelBajo))){
                    preferenciasGlobal.guardarNivelUsuario(getString(R.string.nivelBajo))
                }
            }



        Log.d("usuario", "$superior & $userBoss = ${preferenciasGlobal.recuperarNivelUsuario()}")

        var nivelUsuario = preferenciasGlobal.recuperarNivelUsuario()

        //Recycler Status
        if(nivelUsuario == getString(R.string.nivelAlto)){
            adaptadorStatus = StatusTasksAdapter(StatusTasks.obtenerListaNivelAlto(), this)
            taskViewModel.statusRecycler.value = list[4]
            binding.tituloTareas.text = getString(R.string.titleStatus5)
        }else if( nivelUsuario == getString(R.string.nivelMedio) ){
            adaptadorStatus = StatusTasksAdapter(StatusTasks.obtenerListaNivelMedio(), this)
            taskViewModel.statusRecycler.value = list[0]
        }else{
            adaptadorStatus = StatusTasksAdapter(StatusTasks.obtenerListaNivelBajo(), this)
            taskViewModel.statusRecycler.value = list[0]
        }


        // var adaptadorStatus = StatusTasksAdapter(listStatus, this)
        binding.recyclerStatusTareas.adapter = adaptadorStatus
        binding.recyclerStatusTareas.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        //compararNivel(nivel)


        //RecyclerListaTareas
        taskViewModel.devolverListaPorStatus(this)
        taskViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerTareas.adapter = it
            binding.recyclerTareas.layoutManager = LinearLayoutManager(activity)
        })

        binding.btnCrearTarea.setOnClickListener {
            it.findNavController().navigate(R.id.formularioCrearTareasFragment)
        }
        //Btn Crear tareas
        if(nivelUsuario == getString(R.string.nivelAlto) || nivelUsuario == getString(R.string.nivelMedio)){
            binding.btnCrearTarea.isVisible = true
            binding.btnCrearTarea.setOnClickListener {
                it.findNavController().navigate(R.id.formularioCrearTareasFragment)
            }
        }else{
            binding.btnCrearTarea.isVisible = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTaskByStatus(status: String) {
        binding.progressUno.visibility = View.VISIBLE
        var listaStatus = resources.getStringArray(R.array.status_array)
        when (status) {
            listStatus[0] -> {
                taskViewModel.statusRecycler.value = listaStatus[0]
                binding.tituloTareas.text = getString(R.string.titleStatus1)
            }
            listStatus[1] -> {
                taskViewModel.statusRecycler.value = listaStatus[1]
                binding.tituloTareas.text = getString(R.string.titleStatus2)
            }
            listStatus[2] -> {
                taskViewModel.statusRecycler.value = listaStatus[2]
                binding.tituloTareas.text = getString(R.string.titleStatus3)
            }
            listStatus[3] -> {
                taskViewModel.statusRecycler.value = listaStatus[3]
                binding.tituloTareas.text = getString(R.string.titleStatus4)
            }
            listStatus[4] -> {
                taskViewModel.statusRecycler.value = listaStatus[4]
                binding.tituloTareas.text = getString(R.string.titleStatus5)
            }
        }

        taskViewModel.devolverListaPorStatus(this)
        binding.progressUno.visibility = View.GONE
    }



    fun recuperarNivelUsuario() {
        //Todo al iniciar sesion
        if(InicioSesionViewModel.userBoss == getString(R.string.nivelAlto)){
            // nivel alto / medio
            preferenciasGlobal.guardarNivelUsuario(getString(R.string.nivelAlto))
            //todo superior para saber si es nivel alto o medio
        }else{
            preferenciasGlobal.guardarNivelUsuario(getString(R.string.nivelBajo))
            // nivel bajo
        }
    }
    
    override fun abreDialogo(dataTask: DataTask) {
        val newFragment2 =
            DialogoNivelBajo(dataTask,this)
        newFragment2.show((activity as HomeActivity).supportFragmentManager, getString(R.string.logTareas))
    }

    override fun abreDialogoConfirmar(mensaje: String) {
        val dialogoAceptar = DialogoAceptar(mensaje)
        dialogoAceptar.show(
            (activity as HomeActivity).supportFragmentManager,
            getString(R.string.dialogoAceptar)
        )
    }

}