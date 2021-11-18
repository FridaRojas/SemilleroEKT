package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Mensajes;
import com.google.common.base.Optional;

public interface MensajesService {
	Mensajes crearMensaje(Mensajes mensajes);
	Iterable<Mensajes>verConversacion(String idConversacion);
	Optional<Mensajes>existeConversacion(String idConversacion);
}
