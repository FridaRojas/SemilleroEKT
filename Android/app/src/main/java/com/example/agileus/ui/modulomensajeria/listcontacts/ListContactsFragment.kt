package com.example.agileus.ui.modulomensajeria.listcontacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.ListContactsFragmentBinding
import com.example.agileus.utils.Constantes
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.agileus.R
import com.example.agileus.ui.login.ui.login.InicioSesionFragment
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser


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

        //    Toast.makeText(activity, "Usuario BRD", Toast.LENGTH_LONG).show()






        contactsviewModel.devuelveLista(Constantes.id)


        contactsviewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerListContacts.adapter = it
            binding.recyclerListContacts.layoutManager = LinearLayoutManager(activity)
        })
        binding.etSearchContact.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                contactsviewModel.devuelveLista(Constantes.id)
                contactsviewModel.contactos.observe(viewLifecycleOwner,{
                    var filtro = it.filter { it.nombre.lowercase().contains(p0.toString().lowercase()) }
                    contactsviewModel.filtrarContactos(Constantes.id,filtro)
                })
            }
            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}