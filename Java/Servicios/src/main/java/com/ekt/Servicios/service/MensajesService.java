package com.ekt.Servicios.service;

import com.ekt.Servicios.entity.Mensajes;

public interface MensajesService {
	Mensajes crearMensaje(Mensajes mensajes);
	Iterable<Mensajes>verConversacion(String idConversacion);
}
