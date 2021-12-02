package com.ekt.Servicios.entity;

public class ResponseTask {
    String estatus;
    String mensaje;
    Object data;

    public ResponseTask() {
    }
    public ResponseTask(String estatus, String mensaje) {
        this.estatus = estatus;
        this.mensaje = mensaje;
    }
    public ResponseTask(String estatus, String mensaje, Object data) {
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.data = data;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
