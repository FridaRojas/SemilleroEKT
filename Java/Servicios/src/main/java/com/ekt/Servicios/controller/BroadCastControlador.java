package com.ekt.Servicios.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ekt.Servicios.entity.BroadCast;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;

@RestController
@RequestMapping("/api/broadCast")
public class BroadCastControlador {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BroadCastRepositorio broadCastRepositorio;
	
	@GetMapping("/listaUsuarios/{miId}")
	public ResponseEntity<?> listaUsuariosGeneral(@PathVariable (value = "miId")String miId){
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(listaUsuarios);
	}
	@GetMapping("/mostarMensajesdelBroadcast")
	public Iterable<BroadCast>listarMensajes(){
		Iterable<BroadCast> brd = broadCastRepositorio.findAll();

		return brd;
	}
	
}
