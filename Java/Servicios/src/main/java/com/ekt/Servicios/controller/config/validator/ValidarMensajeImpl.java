package com.ekt.Servicios.controller.config.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.entity.User;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;
import com.ekt.Servicios.util.exceptions.ResultadoNoEncontrado;

@Component
public class ValidarMensajeImpl implements ValidarMensaje {

	@Override
	public void validator(Mensajes request) throws ApiUnprocessableEntity {
		if (request.getIDEmisor() == null || request.getIDEmisor().isEmpty()) {
			this.message("El id del emisor es obligatorio");
		}

		if (request.getIDEmisor().length() < 24) {
			this.message("El id del emisor es muy corto, debe tener almenos 24 caracteres");
		}

		if (request.getIDReceptor() == null || request.getIDReceptor().isEmpty()) {
			this.message("El id del receptor es obligatorio");
		}

		if (request.getIDReceptor().length() < 24) {
			this.message("El id del receptor es muy corto, debe tener almenos 24 caracteres");
		}

		if (request.getTexto() == null || request.getTexto().isEmpty()) {
			this.message("El texto del mensaje es obligatorio");
		}

		/*if (request.getTexto().length() < 1) {
			this.message("El texto es muy corto, debe tener almenos 1 caracter");
		}*/

		if (request.getFechaCreacion() == null) {
			this.message("La fecha de creacion es necesaria");
		}

		if (!(request.getFechaCreacion().toString().contains("T"))) {
			this.message("La fecha de creacion no tiene el formato correcto");
		}
	}

	@Override
	public void validarStatus(Mensajes mensajes) throws ApiUnprocessableEntity {
		if (mensajes.getVisible() == true) {
			this.message("Este campo solo puede ser actualizado a falso");
		}
	}

	

	@Override
	public void validarOptional(Optional<User> validar, String mensaje) throws ResultadoNoEncontrado {
		if(!validar.isPresent()) {
			this.mensajeNoEncontrado("Campo "+mensaje+" no encontrado en la base de datos");
		}
	}
	
	@Override
	public void validarOptionalMensajes(Optional<Mensajes> validar, String mensaje) throws ResultadoNoEncontrado {
		if(!validar.isPresent()) {
			this.mensajeNoEncontrado("El"+mensaje+" no fue encontrado");
		}
	}
	
	@Override
	public void validarIterableUser(Iterable<User> validar, String mensaje) throws ResultadoNoEncontrado {
		if(!validar.iterator().hasNext()) {
			this.mensajeNoEncontrado("La lista de "+mensaje+" esta vacia");
		}
	}
	
	private void message(String message) throws ApiUnprocessableEntity {
		throw new ApiUnprocessableEntity(message);
	}
	
	private void mensajeNoEncontrado(String mensaje) throws ResultadoNoEncontrado{
		throw new ResultadoNoEncontrado(mensaje);
	}
}