package com.ekt.AdministradorWeb.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BodyAddUserGroup {
    private String idUsuario;
    private String idGrupo;
    private String idSuperior;
    private String nombreRol;



    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdSuperior() {
        return idSuperior;
    }

    public void setIdSuperior(String idSuperior) {
        this.idSuperior = idSuperior;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Override
    public String toString() {
        return "BodyAddUserGroup{" +
                "idUsuario='" + idUsuario + '\'' +
                ", idGrupo='" + idGrupo + '\'' +
                ", idSuperior='" + idSuperior + '\'' +
                ", nombreRol='" + nombreRol + '\'' +
                '}';
    }
}
