package com.ekt.Servicios.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.controller.config.validator.ValidarMensajeImpl;
import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.UserRepository;
import com.ekt.Servicios.service.MensajesService;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;


@RestController
@RequestMapping("/api/mensajes/")
public class MensajesController {
	@Autowired
	private MensajesService mensajesService;
	
	@Autowired
	private ValidarMensajeImpl validarMensajeImpl;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("crearMensaje")
	public ResponseEntity<?> crearMensaje(@RequestBody Mensajes mensajes) throws ApiUnprocessableEntity{
		
		Optional<User> emisor = userRepository.validarUsuario(mensajes.getIDEmisor());
		Optional<User> receptor = userRepository.validarUsuario(mensajes.getIDReceptor());
		
		if(emisor.isPresent()) {
			if(receptor.isPresent()) {
				
				Iterable<Mensajes> opt = mensajesService.verConversacion(mensajes.getIDEmisor()+"_"+mensajes.getIDReceptor());
				Iterable<Mensajes> opt2 = mensajesService.verConversacion(mensajes.getIDReceptor()+"_"+mensajes.getIDEmisor());
				
				if(opt.toString().length() > 3) { 
					mensajes.setIDConversacion(mensajes.getIDEmisor()+"_"+mensajes.getIDReceptor());
				} else if(opt2.toString().length() > 3) {
					mensajes.setIDConversacion(mensajes.getIDReceptor()+"_"+mensajes.getIDEmisor());
				} else {
					mensajes.setIDConversacion(mensajes.getIDEmisor()+"_"+mensajes.getIDReceptor());
				}
				
				this.validarMensajeImpl.validator(mensajes);
				
				//Status-fecha Creado
				mensajes.setStatusCreado(true);
				
				//Status-fecha Enviado
				mensajes.setFechaEnviado(new Date());
				mensajes.setStatusEnviado(true);
				
				//Status-fecha Leido
				mensajes.setStatusLeido(false);
				mensajes.setFechaLeido(new Date(0));
				
				mensajes.setVisible(true);
				
				mensajesService.crearMensaje(mensajes);
				
				return ResponseEntity.status(HttpStatus.CREATED).body(mensajes);
				
			}
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(receptor.get());
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(emisor.get());
		} 
    }
	
	//ver conversacion
	@GetMapping("/verConversacion/{idConversacion}")
	public ResponseEntity<?> verConversacion(@PathVariable (value = "idConversacion") String idConversacion){
		Iterable<Mensajes> iter =mensajesService.verConversacion (idConversacion);

		return ResponseEntity.ok(iter.iterator());
	}


}
