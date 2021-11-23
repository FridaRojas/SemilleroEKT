package com.example.agileus.ui.modulomensajeria.listcontacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.FragmentHomeBinding
import com.example.agileus.databinding.ListContactsFragmentBinding

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
        contactsviewModel.devuelveLista()

        contactsviewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerListContacts.adapter = it
            binding.recyclerListContacts.layoutManager = LinearLayoutManager(activity)
        })
    }

}