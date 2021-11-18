package com.ekt.Servicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.repository.MensajesRepository;
import com.google.common.base.Optional;

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
	public Optional<Mensajes> existeConversacion(String idConversacion) {
		// TODO Auto-generated method stub
		return null;
	}


}
