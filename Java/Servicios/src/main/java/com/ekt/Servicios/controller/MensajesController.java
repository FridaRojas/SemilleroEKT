package com.ekt.Servicios.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.ekt.Servicios.repository.MensajesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.service.MensajesService;

@RestController
@RequestMapping("/api/mensajes/")
public class MensajesController {
	@Autowired
	private MensajesService mensajesService;

	
	@SuppressWarnings("deprecation")
	@PostMapping("crearMensaje")
	public ResponseEntity<?> crearMensaje(@RequestBody Mensajes mensajes){
		
		//Status-fecha Creado
		mensajes.setStatusCreado(true);
		
		//Status-fecha Enviado
		mensajes.setFechaEnviado(new Date());
		mensajes.setStatusEnviado(true);
		
		//Status-fecha Leido
		mensajes.setStatusLeido(false);
		mensajes.setFechaLeido(new Date(0));
		
		mensajes.setVisible(true);
		
		mensajes.setIDConversacion(mensajes.getIDEmisor()+"_"+mensajes.getIDReceptor());
		
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajesService.crearMensaje(mensajes));
    }
	//ver conversacion

	@GetMapping("/verConversacion/{idConversacion}")
	public ResponseEntity<?> verConversacion(@PathVariable (value = "idConversacion") String idConversacion){
		Iterable<Mensajes> iter =mensajesService.verConversacion (idConversacion);

		return ResponseEntity.ok(iter.iterator());
	}


}
