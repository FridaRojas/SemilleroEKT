package com.example.agileus.ui.modulotareas.listatareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.adapters.StatusTasksAdapter
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.config.MySharedPreferences.Companion.NIVEL_USER
import com.example.agileus.databinding.FragmentTaskBinding
import com.example.agileus.models.DataTask
import com.example.agileus.models.StatusTasks
import com.example.agileus.models.StatusTasks.Companion.lista
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.ui.login.InicioSesionViewModel
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoNivelBajo
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel.Companion.status
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener


class TaskFragment : Fragment(), TaskDialogListener, TaskListListener {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    lateinit var adaptador : StatusTasksAdapter

    private lateinit var taskViewModel: TaskViewModel

    lateinit var listStatus : Array<String>

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
        
        (activity as HomeActivity?)?.getActionBar()?.setTitle("Hola StackOverflow en Español")

        listStatus = resources.getStringArray(R.array.statusRecycler_array)
        //Recycler Status
        var adaptadorStatus = StatusTasksAdapter(StatusTasks.obtenerLista(), this)

       // var adaptadorStatus = StatusTasksAdapter(listStatus, this)
        binding.recyclerStatusTareas.adapter = adaptadorStatus
        binding.recyclerStatusTareas.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        var nivel = preferenciasGlobal.recuperarNivelUsuario()
        compararNivel(nivel)


        //RecyclerListaTareas
        taskViewModel.devolverListaPorStatus(this)
        taskViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerTareas.adapter = it
            binding.recyclerTareas.layoutManager = LinearLayoutManager(activity)
        })


        //Btn Crear tareas
        if(nivel == "alto"){
            binding.btnCrearTarea.isVisible = true
        }else{
            binding.btnCrearTarea.isVisible = false

        }

        binding.btnCrearTarea.setOnClickListener {
            it.findNavController().navigate(R.id.formularioCrearTareasFragment)


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

        //taskViewModel.statusRecycler.value = "Iniciada"
        taskViewModel.devolverListaPorStatus(this)
        binding.progressUno.visibility = View.GONE
        //Toast.makeText(activity, "${taskViewModel.statusRecycler.value}", Toast.LENGTH_SHORT).show()
    }

    private fun compararNivel(nivel: String) {
        if(nivel == "alto"){
            status = "asignada"
            lista.add(StatusTasks("Asignadas",false))
        }else if( nivel == "bajo"){
            lista.add(StatusTasks("Pendientes",true))
            lista.add(StatusTasks("Iniciadas",false))
            lista.add(StatusTasks("En Revisión",false))
            lista.add(StatusTasks("Terminadas",false))
        }else if( nivel == "medio"){
            lista.add(StatusTasks("Asignadas",false))
            lista.add(StatusTasks("Pendientes",true))
            lista.add(StatusTasks("Iniciadas",false))
            lista.add(StatusTasks("En Revisión",false))
            lista.add(StatusTasks("Terminadas",false))
        }
    }

    fun recuperarNivelUsuario() {
        //Todo al iniciar sesion
        if(InicioSesionViewModel.usersByBoss == true){
            // nivel alto / medio
            preferenciasGlobal.guardarNivelUsuario("alto")
            //todo superior para saber si es nivel alto o medio
        }else{
            preferenciasGlobal.guardarNivelUsuario("bajo")
            // nivel bajo
        }
    }
    
    override fun abreDialogo(dataTask: DataTask) {
        val newFragment = DialogoNivelBajo(this,dataTask)
        newFragment.show((activity as HomeActivity).supportFragmentManager, "missiles")
    }
    
}