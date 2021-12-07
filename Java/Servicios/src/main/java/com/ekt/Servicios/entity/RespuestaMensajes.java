package com.ekt.Servicios.entity;

public class RespuestaMensajes {
	String status;
    String msj;
    Object data;

    public RespuestaMensajes(String status, String msj, Object data) {
        this.status = status;
        this.msj = msj;
        this.data = data;
    }

    public RespuestaMensajes(){};

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
