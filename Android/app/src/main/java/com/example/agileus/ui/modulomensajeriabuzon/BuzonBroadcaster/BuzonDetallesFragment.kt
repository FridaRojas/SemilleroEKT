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

//        var lista=viewModel.getLista()
//        Log.i("lista",lista.size.toString())


        //       val post=Buzon("12","Brody","General","hola mundo feo 1","prueba de post")
   //     viewModel.postMensaje(post)

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


            viewModel.postMensaje(buzon)


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
                viewModel.devuelvebuzon()
            }
        }.start()
    }

}
