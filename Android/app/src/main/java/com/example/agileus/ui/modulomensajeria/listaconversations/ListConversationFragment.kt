package com.example.agileus.ui.modulomensajeria.listacontactos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.FragmentHomeBinding
import com.example.agileus.models.Chats
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.utils.Constantes
import com.google.android.material.bottomnavigation.BottomNavigationView


class ListConversationFragment : Fragment() {

    private lateinit var ChatsViewModel: ListConversationViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ChatsViewModel =
            ViewModelProvider(this).get(ListConversationViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //AGREGADA
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.isVisible = true

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChatsViewModel.devuelveListaGrupos(Constantes.id)
        ChatsViewModel.devuelveListaChats(Constantes.id)

        ChatsViewModel.adaptadorGrupos.observe(viewLifecycleOwner, {
            binding.recyclerListGroups.adapter = it
            binding.recyclerListGroups.layoutManager = LinearLayoutManager(activity)
        })
        ChatsViewModel.adaptadorChats.observe(viewLifecycleOwner,{
            binding.recyclerListChats.adapter = it
            binding.recyclerListChats.layoutManager = LinearLayoutManager(activity)
        })

        binding.cerrarSesion.setOnClickListener {
            findNavController().navigate(R.id.inicioSesionFragment)
        }

        binding.btnListContacts.setOnClickListener {
            findNavController().navigate(R.id.listContactsFragment)
        }
        binding.imgBroadcast.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_buzonUserFragment)
        }

        binding.etSearchConversation.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ChatsViewModel.devuelveListaChats(Constantes.id)

                ChatsViewModel.chatsdeUsuario.observe(viewLifecycleOwner,{

                if(p0.isNullOrEmpty()){
                    binding.recyclerListGroups.isVisible = true
                    binding.recyclerListGroups.isEnabled = true
                }else{
                        var filtrada = it.filter { it.nombreConversacionRecepto.lowercase().contains(p0.toString().lowercase()) }
                        ChatsViewModel.filtrarChats(Constantes.id, filtrada as ArrayList<Chats>)
                        binding.recyclerListGroups.isVisible = false
                        binding.recyclerListGroups.isEnabled = false
                }
                })
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.recyclerListGroups.isVisible = true
                binding.recyclerListGroups.isEnabled = true


            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}