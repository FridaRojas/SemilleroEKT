package com.ekt.Servicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.repository.MensajesRepository;

import java.util.Optional;

@Service
public class MensajesServiceImpl implements MensajesService{
	@Autowired
	private MensajesRepository mensajesRepository;
	
	@Override
	public Mensajes crearMensaje(Mensajes mensajes) {
		return mensajesRepository.insert(mensajes);
	}

	@Override 
	public Iterable<Mensajes> verConversacion(String idConversacion) {

		return mensajesRepository.findByIdConversacion(idConversacion);
	}

	@Override
	public Optional<Mensajes> actualizarVisible(String id) {
		return mensajesRepository.findById(id);
	}

	@Override
	public Optional<Mensajes> buscarMensaje(String idMensaje) {
		Optional<Mensajes> buscarMensaje = mensajesRepository.buscarMensaje(idMensaje);
		
		return buscarMensaje;
	}

	@Override
	public Mensajes save(Mensajes mensaje) {
		return mensajesRepository.save(mensaje);
	}

}