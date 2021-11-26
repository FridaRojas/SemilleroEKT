package com.example.agileus.ui.modulomensajeriabuzon.b

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.Models.Buzon
import com.example.agileus.R
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.databinding.BuzonDetallesUserFragmentBinding
import com.example.demoroom.dialogos.DialogoSenderBroadcast
import com.example.demoroom.dialogos.DialogoSenderUser

class BuzonDetallesUserFragment : Fragment() ,UserBuzonListener{


    private lateinit var viewModel: BuzonDetallesUserViewModel
    private var _binding: BuzonDetallesUserFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[BuzonDetallesUserViewModel::class.java]
        _binding = BuzonDetallesUserFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




            binding.vista2.visibility=View.INVISIBLE

        if (BuzonFragment.control == 1) {
            binding.fab.visibility = View.VISIBLE
            binding.fab.setOnClickListener {
                val newFragment =
                    DialogoSenderUser(this) //Se le pasa el dialogolistener con This
                activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
            }
        }

        if (BuzonFragment.control == 2) {
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
                Log.i("response",response.code().toString())
            }}
        )


    Handler().postDelayed({
            binding.vista1.visibility= View.INVISIBLE
            binding.vista2.visibility = View.VISIBLE
            binding.fab.visibility = View.INVISIBLE
        }, 5)
        ////////////////

        startTimeCounter()


        Handler().postDelayed({
            Toast.makeText(context, " Mensaje enviado a Broadcast", Toast.LENGTH_SHORT).show()
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

