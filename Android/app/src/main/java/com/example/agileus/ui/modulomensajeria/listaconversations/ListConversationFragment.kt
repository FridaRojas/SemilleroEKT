package com.example.agileus.ui.modulomensajeria.listacontactos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.FragmentHomeBinding
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel


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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

  /*      ChatsViewModel.devuelveLista()

        ChatsViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.recyclerListGroups.adapter = it
            binding.recyclerListGroups.layoutManager = LinearLayoutManager(activity)
        })
   */
        binding.btnListContacts.setOnClickListener {
            findNavController().navigate(R.id.listContactsFragment)
        }
        binding.imgBroadcast.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_buzonUserFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}