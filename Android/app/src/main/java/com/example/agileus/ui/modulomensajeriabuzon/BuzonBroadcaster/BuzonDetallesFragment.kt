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
import com.example.agileus.models.Buzon
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.USERTYPE
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.control
import android.os.CountDownTimer
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.Listeners.BroadcasterListener
import com.example.agileus.ui.modulomensajeriabuzon.Dialogos.DialogoSenderBroadcast


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



        USERTYPE="Broadcast"
            binding.vista2.visibility=View.INVISIBLE

        if (control == 1) {

            viewModel.getLista()
            viewModel.devuelvebuzon1()

            binding.fab.visibility = View.VISIBLE
                binding.fab.setOnClickListener {
                val newFragment =
                    DialogoSenderBroadcast(this,listafiltrada) //Se le pasa el dialogolistener con This
                activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
            }
        }

        if (control == 2) {
            viewModel.devuelvebuzon2()
            binding.fab.visibility = View.GONE
            binding.fab.setOnClickListener {
                Toast.makeText(context, "Opción No permitida ", Toast.LENGTH_SHORT).show()
            }
        }


/*
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response->
            if (response.isSuccessful)
            {
                Log.d("Main",response.body().toString())
                Log.d("Main",response.code().toString())
                Log.d("Main",response.message().toString())
            }
            else{
                Log.d("Main",response.code().toString())
            }
        })
*/
        /*
        viewModel.myResponse1.observe(viewLifecycleOwner, Observer { response->
            if (response.isSuccessful)
            {
                Log.d("Main1",response.body().toString())
                Log.d("Main1",response.code().toString())
                Log.d("Main1",response.message().toString())
            }
            else{
                Log.d("Main1",response.code().toString())
            }
        })
*/

//        viewModel.devuelvebuzon()


            viewModel.adaptador.observe(viewLifecycleOwner, {
                binding.recyclerBuzon.adapter = it
                binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)

            })
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        override fun mensajeBroadcasting(buzon: Buzon) {


          //  viewModel.postMensaje(buzon)

/*
            viewModel.myResponse.observe(viewLifecycleOwner, Observer {response->
                if (response.isSuccessful)
                {
                    Log.i("Code ",response.code().toString())
                }
                else{
                    Log.i("Code ",response.code().toString())}
            })

            Handler().postDelayed({
                binding.vista1.visibility= View.INVISIBLE
                binding.vista2.visibility = View.VISIBLE
                binding.fab.visibility = View.INVISIBLE
            }, 5)


            startTimeCounter()
              ////////////////
            Handler().postDelayed({
                Toast.makeText(context, " Mensaje enviado a ${buzon.Receiverid}", Toast.LENGTH_SHORT).show()
                binding.vista2.visibility = View.INVISIBLE
                binding.vista1.visibility= View.VISIBLE
                binding.fab.visibility = View.VISIBLE
  }, 3800)
*/
        }

    override fun mensajeBroadcasting1(buzon: MensajeBodyBroadcaster) {
        //viewModel.postMensaje1(buzon)
       // viewModel.postMensaje(buzon)


  Log.d("body","${buzon.fechaCreacion}")
  Log.d("body","${buzon.idReceptor}")
  Log.d("body","${buzon.idEmisor}")
  Log.d("body","${buzon.texto}")
//        viewModel.postMensaje(buzon)


        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response->
            if (response.isSuccessful)
            {
                Log.d("Mine",response.body().toString())
                Log.d("Mine",response.code().toString())
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
           //     viewModel.devuelvebuzon()
            }
        }.start()
    }

}
