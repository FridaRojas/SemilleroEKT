package com.ekt.Servicios.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ekt.Servicios.entity.BroadCast;
import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.Response;
import com.ekt.Servicios.repository.BroadCastRepositorio;
import com.ekt.Servicios.service.BroadCastServicio;
import com.ekt.Servicios.service.BroadCastServicioImpl;
import com.ekt.Servicios.service.MensajesService;

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
	@Autowired(required = false)
	private BroadCastServicio broadCastServicio;
	@Autowired
	private BroadCastRepositorio broadCastRepositorio;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private MensajesService mensajesService;

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

	@PostMapping("/crearMensajeBroadcast")
	public ResponseEntity<BroadCast> crearMensajeBroadCast(@RequestBody BroadCast broadCast){

		return ResponseEntity.status(HttpStatus.CREATED).body(broadCastRepositorio.save(broadCast));
	}
	@GetMapping("/mostrarMensajesporID/{idEmisor}")
	public List<BroadCast> mostrarMensajes(@PathVariable(value = "idEmisor")String idEmisor){
		List<BroadCast> listBrd = new ArrayList<>();
		Iterable<BroadCast> brd = broadCastRepositorio.findAll();
		for (BroadCast brd2 : brd) {
			if(brd2.getIdEmisor().equals(idEmisor)){
				listBrd.add(brd2);
			}

		}
		return listBrd;
	}
	
	@PostMapping("/enviarMensaje")
	public ResponseEntity<?> enviarMensaje(@RequestBody Mensajes mensajeEntrante){
		//Optional<User> existo = userRepository.validarUsuario(mensajeEntrante.getIDEmisor());
		
		Mensajes mensaje = new Mensajes();
		
		mensaje.setIDConversacion(mensajeEntrante.getIDEmisor()+"_"+mensajeEntrante.getIDReceptor());
		mensaje.setConversacionVisible(true);
		mensaje.setIDEmisor(mensajeEntrante.getIDEmisor());
		mensaje.setIDReceptor(mensajeEntrante.getIDReceptor());
		mensaje.setTexto(mensajeEntrante.getTexto());
		mensaje.setVisible(false);
		mensaje.setRutaDocumento(null);
		mensaje.setNombreConversacionReceptor("Conversacion BroadCast");
		
		mensaje.setFechaCreacion(mensajeEntrante.getFechaCreacion());
		mensaje.setStatusCreado(true);
		
		mensaje.setFechaEnviado(new Date());
		mensaje.setStatusEnviado(true);
		
		mensaje.setFechaLeido(new Date(0));
		mensaje.setStatusLeido(false);
		
		mensajesService.crearMensaje(mensaje);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(mensaje);
	}
}
