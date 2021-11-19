package com.ekt.Servicios.controller.config.validator;

import org.springframework.stereotype.Service;

import com.ekt.Servicios.entity.Mensajes;
import com.ekt.Servicios.util.exceptions.ApiUnprocessableEntity;


@Service
public interface ValidarMensaje {
	void validator(Mensajes request) throws ApiUnprocessableEntity;
	void validarStatus(Mensajes request)throws ApiUnprocessableEntity;
}
