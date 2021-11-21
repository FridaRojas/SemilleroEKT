package com.ekt.Servicios.controller.config.validator;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;
import com.ekt.Servicios.util.exceptions.ResultadoNoEncontrado;


@Service
public interface ValidarMensaje {
	void validator(Mensajes request) throws ApiUnprocessableEntity;
	void validarStatus(Mensajes request)throws ApiUnprocessableEntity;
	void validarOptional(Optional<User> validar, String mensaje) throws ResultadoNoEncontrado;
	void validarIterableUser(Iterable<User> validar, String mensaje) throws ResultadoNoEncontrado;
	void validarOptionalMensajes(Optional<Mensajes> validar, String mensaje) throws ResultadoNoEncontrado;
}
