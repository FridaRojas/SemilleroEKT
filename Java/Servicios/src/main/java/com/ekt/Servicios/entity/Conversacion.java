package com.ekt.Servicios.entity;

public class Conversacion {
   private String idConversacion;
   private String idReceptor;
   private String nombreConversacionRecepto;

    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    public String getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(String idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getNombreConversacionRecepto() {
        return nombreConversacionRecepto;
    }

    public void setNombreConversacionRecepto(String nombreConversacionRecepto) {
        this.nombreConversacionRecepto = nombreConversacionRecepto;
    }


}
