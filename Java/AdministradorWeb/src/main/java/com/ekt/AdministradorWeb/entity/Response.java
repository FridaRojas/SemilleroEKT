package com.ekt.AdministradorWeb.entity;

import org.springframework.http.HttpStatus;

public class Response {
    HttpStatus status;
    String msj;
    Object data;

    public Response(HttpStatus status, String msj, Object data) {
        this.status = status;
        this.msj = msj;
        this.data = data;
    }

    public Response(){};

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
