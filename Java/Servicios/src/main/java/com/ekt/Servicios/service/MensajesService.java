package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Mensajes;
import java.util.Optional;

public interface MensajesService {

	Mensajes crearMensaje(Mensajes mensajes);
	Iterable<Mensajes>verConversacion(String idConversacion);
	Optional<Mensajes> actualizarVisible(String id);

	Optional<Mensajes> buscarMensaje(String idMensaje);
	Mensajes save(Mensajes mensaje);
}
