package com.example.agileus.ui.modulomensajeriabuzon.b
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
import com.example.agileus.Models.Buzon
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonFragment.Companion.USERTYPE
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonFragment.Companion.control
import com.example.demoroom.dialogos.DialogoSenderBroadcast
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import android.R
import android.os.CountDownTimer

import android.widget.ProgressBar








class BuzonDetallesFragment: Fragment() ,BroadcasterListener{


    private lateinit var viewModel: BuzonDetallesViewModel

    private var _binding: BuzonDetallesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    //    val mypost=Buzon("6","Broadcast","General","hola mundo feo","prueba de post")




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
            binding.fab.visibility = View.VISIBLE
            binding.fab.setOnClickListener {
                val newFragment =
                    DialogoSenderBroadcast(this) //Se le pasa el dialogolistener con This
                activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
            }
        }

        if (control == 2) {
            binding.fab.visibility = View.GONE
            binding.fab.setOnClickListener {
                Toast.makeText(context, "Opción No permitida ", Toast.LENGTH_SHORT).show()
            }
        }



        viewModel.devuelvebuzon()

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

            viewModel.postMensaje(buzon)
            viewModel.myResponse.observe(viewLifecycleOwner, Observer {response->
                if (response.isSuccessful)
                {
                    Log.i("Main",response.code().toString())
                }
            })

 //           val progressBar = binding.progress
//            progressBar.progress = 20

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
  }, 4200)

        }
    fun startTimeCounter() {
        var counter=0
        val progressBar = binding.progress
        progressBar.visibility=View.VISIBLE
//        val countTime: TextView = findViewById(R.id.countTime)
        object : CountDownTimer(4000, 100) {
            override fun onTick(millisUntilFinished: Long) {
//                countTime.text = counter.toString()
//                Log.d("tiempo ", " $counter")
                counter++
                progressBar.progress = counter
            }
            override fun onFinish() {
            }
        }.start()
    }

}
