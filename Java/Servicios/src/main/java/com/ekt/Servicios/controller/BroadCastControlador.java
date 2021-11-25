package com.ekt.Servicios.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;

@RestController
@RequestMapping("/api/broadCast")
public class BroadCastControlador {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/listaUsuarios/{miId}")
	public ResponseEntity<?> listaUsuariosGeneral(@PathVariable (value = "miId")String miId){
		
		Optional<User> existo = userRepository.validarUsuario(miId);
		
		Iterable<User> listaUsuarios =  userRepository.findByGroupID(existo.get().getIDGrupo());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(listaUsuarios);
	}
	
}
