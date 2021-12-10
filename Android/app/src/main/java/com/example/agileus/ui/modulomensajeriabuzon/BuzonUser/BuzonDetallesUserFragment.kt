package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.models.Buzon
import com.example.agileus.databinding.BuzonDetallesUserFragmentBinding
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.userName
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
import com.example.agileus.ui.modulomensajeriabuzon.Dialogos.DialogoSenderBroadcast
import com.example.agileus.ui.modulomensajeriabuzon.Dialogos.DialogoSenderUser
import com.example.agileus.ui.modulomensajeriabuzon.Listeners.UserBuzonListener
import retrofit2.Response

class BuzonDetallesUserFragment : Fragment() , UserBuzonListener {


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
        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            "Mis Mensajes Recibidos"


        binding.fab.setOnClickListener {
            val newFragment = DialogoSenderUser(this) //Se le pasa el dialogolistener con This
            activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
        }

                  viewModel.devuelvebuzon2()
            viewModel.adaptador.observe(
                viewLifecycleOwner,
                {
                    binding.recyclerBuzon.adapter = it
                    binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)
                })


    }




    override fun mensajeBroadcasting1(buzon: MsgBodyUser) {

           buzon.idEmisor=idUser


        viewModel.postRequest(buzon)

            viewModel.myResponse1.observe(
                viewLifecycleOwner,
                { response ->
                    if (response.isSuccessful) {
                        Log.i("response", response.code().toString())
                    }
                },
            )

            Handler().postDelayed({
                binding.vista1.visibility = View.INVISIBLE
//                binding.vista2.visibility = View.VISIBLE
                binding.fab.visibility = View.INVISIBLE
            }, 5)
            ////////////////

            startTimeCounter()


            Handler().postDelayed({
                Toast.makeText(context, " Mensaje enviado a Broadcast", Toast.LENGTH_SHORT).show()
  //              binding.vista2.visibility = View.INVISIBLE
                binding.vista1.visibility = View.VISIBLE
                binding.fab.visibility = View.VISIBLE
            }, 3800)
        }


        fun startTimeCounter() {
            var counter = 0
            binding.vista1.visibility = View.INVISIBLE
    //        binding.vista2.visibility = View.VISIBLE
            binding.fab.visibility = View.INVISIBLE

      //      val progressBar = binding.progress
        //    progressBar.visibility = View.VISIBLE
//        val countTime: TextView = findViewById(R.id.countTime)
            object : CountDownTimer(3900, 100) {
                override fun onTick(millisUntilFinished: Long) {
//                countTime.text = counter.toString()
//                Log.d("tiempo ", " $counter")
                    counter++
         //           progressBar.progress = counter
                }
                override fun onFinish() {
//                    viewModeldevuelvebuzon()
                }
            }.start()
        }
    }


