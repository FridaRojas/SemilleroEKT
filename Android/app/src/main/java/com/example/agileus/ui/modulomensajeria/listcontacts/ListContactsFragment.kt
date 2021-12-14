package com.example.agileus.ui.modulomensajeria.listcontacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.ListContactsFragmentBinding
import androidx.core.view.isVisible
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.HomeActivity


class ListContactsFragment : Fragment() {

    private lateinit var contactsviewModel: ListContactsViewModel

    private var _binding: ListContactsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactsviewModel = ViewModelProvider(this).get(ListContactsViewModel::class.java)

        _binding = ListContactsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var UserId = InitialApplication.preferenciasGlobal.recuperarIdSesion()
        binding.progressBarContacts.isVisible = true

        (activity as HomeActivity).fragmentSeleccionado = "Contactos"

        contactsviewModel.devuelveLista(UserId)

        contactsviewModel.adaptador.observe(viewLifecycleOwner, {

            binding.progressBarContacts.isVisible = false
            binding.recyclerListContacts.adapter = it
            binding.recyclerListContacts.layoutManager = LinearLayoutManager(activity)
        })

        binding.etSearchContact.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrEmpty()){
                    contactsviewModel.devuelveLista(UserId)
                }else{
                    var fil = contactsviewModel.listaConsumida.filter {  it.nombre.lowercase().contains(p0.toString().lowercase()) }
                    contactsviewModel.filtrarContactos(UserId, fil)

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}