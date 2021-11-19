package com.ekt.Servicios.controller.config.validator;

import org.springframework.stereotype.Component;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;

@Component
public class ValidarMensajeImpl implements ValidarMensaje{

	@Override
	public void validator(Mensajes request) throws ApiUnprocessableEntity {
		if(request.getIDEmisor() == null || request.getIDEmisor().isEmpty()) {
			this.message("El id del emisor es obligatorio");
		}
		
		/*if(request.getIDEmisor().length() < 3) {
			this.message("El id del emisor es muy corto, debe tener almenos 3 caracteres");
		}*/
		
		if(request.getIDReceptor() == null || request.getIDReceptor().isEmpty()) {
			this.message("El id del receptor es obligatorio");
		}
		
		/*if(request.getIDReceptor().length() < 3) {
			this.message("El id del receptor es muy corto, debe tener almenos 3 caracteres");
		}*/
		
		if(request.getTexto() == null || request.getTexto().isEmpty()) {
			this.message("El texto del mensaje es obligatorio");
		}
		
		if(request.getTexto().length() < 1) {
			this.message("El texto es muy corto, debe tener almenos 1 caracter");
		}
		
		if(request.getFechaCreacion() == null) {
			this.message("La fecha de creacion es necesaria");
		}
		
		if(!(request.getFechaCreacion().toString().contains("T"))) {
			this.message("La fecha de creacion no tiene el formato correcto");
		}
	}
	
	private void message(String message) throws ApiUnprocessableEntity {
		throw new ApiUnprocessableEntity(message);
	}
}
