package com.ekt.Servicios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.repository.MensajesRepository;

@Service
public class MensajesServiceImpl implements MensajesService{
	@Autowired
	private MensajesRepository mensajesRepository;
	
	@Override
	public Mensajes crearMensaje(Mensajes mensajes) {
		return mensajesRepository.insert(mensajes);
	}
}
