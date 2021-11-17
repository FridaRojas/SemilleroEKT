package com.ekt.Servicios.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Excepcion para status 422 (validar datos o estructura no similar)
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ApiUnprocessableEntity extends Exception{
    public ApiUnprocessableEntity(String menssage){
    super(menssage);
    }
}
