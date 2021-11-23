package com.example.agileus.ui.modulomensajeriabuzon.b
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.Models.Buzon
import com.example.agileus.databinding.BuzonDetallesFragmentBinding
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonFragment.Companion.control
import com.example.demoroom.dialogos.DialogoSenderBroadcast
import com.lalo.room_reto_training.dialogos.SeleccionSimpleDialogFragment

class BuzonDetallesFragment : Fragment() ,BroadcasterListener{


    private lateinit var viewModel: BuzonDetallesViewModel
    private var _binding: BuzonDetallesFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BuzonDetallesViewModel::class.java)
        _binding = BuzonDetallesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(control == 1 )
        { binding.fab.visibility =View.VISIBLE
            binding.fab.setOnClickListener {
                val newFragment = SeleccionSimpleDialogFragment(this) //Se le pasa el dialogolistener con This
                activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
            }
        }
        if(control == 2 )
        {binding.fab.visibility =View.GONE
            binding.fab.setOnClickListener {
                Toast.makeText(context,"Opción No permitida ",Toast.LENGTH_SHORT).show()
            }
        }
//        var spin=binding.

binding.fab.setOnClickListener {
    val newFragment = SeleccionSimpleDialogFragment(this) //Se le pasa el dialogolistener con This
    activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Destino") }
}

        viewModel.devuelvebuzon()

        viewModel.adaptador.observe(viewLifecycleOwner,{
            binding.recyclerBuzon.adapter = it
            binding.recyclerBuzon.layoutManager = LinearLayoutManager(activity)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun mensajeBroadcasting(buzon: Buzon) {
       Toast.makeText(context," Mensaje enviado a ${buzon.receiverId}",Toast.LENGTH_SHORT).show()
    }

    override fun Tipodeusuario(seleccionado: String) {
//        var texto= findViewbyId<TextView>
        val newFragment = DialogoSenderBroadcast(this,seleccionado) //Se le pasa el dialogolistener con This
        activity?.supportFragmentManager?.let { it1 -> newFragment.show(it1, "Cuerpo") }

    }
}