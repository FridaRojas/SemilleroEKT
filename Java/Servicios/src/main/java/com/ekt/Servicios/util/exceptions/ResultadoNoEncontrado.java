package com.ekt.Servicios.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResultadoNoEncontrado extends Exception{
	public ResultadoNoEncontrado(String mensaje) {
		super(mensaje);
	}
}
