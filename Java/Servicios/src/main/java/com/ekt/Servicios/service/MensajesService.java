package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Mensajes;
import java.util.Optional;

public interface MensajesService {

	Mensajes crearMensaje(Mensajes mensajes);
	Iterable<Mensajes> verConversacion(String idConversacion);
	public void notificacion2(String title, String asunto, String token);

	Optional<Mensajes> buscarMensaje(String idMensaje);
	Mensajes save(Mensajes mensaje);
}
