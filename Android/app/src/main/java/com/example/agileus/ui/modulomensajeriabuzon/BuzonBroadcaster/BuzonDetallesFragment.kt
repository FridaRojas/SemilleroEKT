package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.USERTYPE
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.control
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.agileus.R
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.Listeners.BroadcasterListener
import com.example.agileus.ui.modulomensajeriabuzon.Dialogos.DialogoSenderBroadcast
import com.google.android.material.bottomnavigation.BottomNavigationView


class BuzonDetallesFragment: Fragment() , BroadcasterListener {


    private lateinit var viewModel: BuzonDetallesViewModel

    private var _binding: BuzonDetallesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = ViewModelProvider(this)[BuzonDetallesViewModel::class.java]
        _binding = BuzonDetallesFragmentBinding.inflate(inflater, container, false)



        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Buzon Recibidos Broadcast"




        USERTYPE="Broadcast"
            binding.vista2.visibility=View.INVISIBLE

        if (control == 1) {

            val lista=viewModel.getLista()

            viewModel.devuelvebuzon1()

            binding.fab.visibility = View.VISIBLE
                binding.fab.setOnClickListener {
                val newFragment =
                    DialogoSenderBroadcast(this,lista) //Se le pasa el dialogolistener con This
                activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
            }
        }

        viewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerBuzon.adapter = it
            binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)

        })
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    override fun mensajeBroadcasting(buzon: MensajeBodyBroadcaster) {


        val size= listaus.size

        for(i in 0.. size-1)
        {
            if (buzon.idReceptor == listaus[i].nombre )
            {
                Log.d("id database","${listaus[i].id}")
                buzon.idReceptor= listaus[i].id
            }
        }
        viewModel.postMensaje(buzon)

//   var idreceptor=
  Log.d("body","${buzon.fechaCreacion}")
  Log.d("body","${buzon.idReceptor}")
  Log.d("body","${buzon.idEmisor}")
  Log.d("body","${buzon.texto}")



        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response->
            if (response.isSuccessful)
            {
                Log.d("Mine",response.message().toString())
            }
            else{
                Log.d("Main",response.code().toString())
            }
        })

        Handler().postDelayed({
            binding.vista1.visibility= View.INVISIBLE
            binding.vista2.visibility = View.VISIBLE
            binding.fab.visibility = View.INVISIBLE
        }, 5)


        startTimeCounter()
        ////////////////
        Handler().postDelayed({
            Toast.makeText(context, " Mensaje enviado a ${buzon.idReceptor}", Toast.LENGTH_SHORT).show()
            binding.vista2.visibility = View.INVISIBLE
            binding.vista1.visibility= View.VISIBLE
            binding.fab.visibility = View.VISIBLE
        }, 3800)
    }

    fun startTimeCounter() {
        var counter=0
        val progressBar = binding.progress
        progressBar.visibility=View.VISIBLE
        object : CountDownTimer(3900, 100) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
                progressBar.progress = counter
            }
            override fun onFinish() {
//                viewModel.devuelvebuzon1()
            }
        }.start()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = false
        (activity as HomeActivity).ocultarBtnAtras()


    }
}
