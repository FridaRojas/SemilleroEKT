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
import com.example.agileus.utils.Constantes

class ListContactsFragment : Fragment() {

    companion object {
        fun newInstance() = ListContactsFragment()
    }

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
        contactsviewModel.devuelveLista(Constantes.id)

        contactsviewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerListContacts.adapter = it
            binding.recyclerListContacts.layoutManager = LinearLayoutManager(activity)
        })

        binding.etSearchContact.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//Se hace el filtro y aqui se pone toda la palabra en minisculas
                contactsviewModel.devuelveLista(Constantes.id)
                contactsviewModel.contactos.observe(viewLifecycleOwner,{
                    var filtro = it.filter { it.nombre.lowercase().contains(p0.toString().lowercase()) }
                    contactsviewModel.filtrarChats(Constantes.id,filtro)
                })

            }

            override fun afterTextChanged(p0: Editable?) {


            }

        })

    }

}