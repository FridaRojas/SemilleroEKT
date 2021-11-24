package com.ekt.AdministradorWeb.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BodyAddUserGroup {
    private String idUsuario;
    private String idGrupo;
    private String idSuperior;
    private String nombreRol;

    public String getIDGrupo() { return idGrupo; }
    public void setIDGrupo(String value) { this.idGrupo = value; }

    public String getIDUsuario() { return idUsuario; }
    public void setIDUsuario(String value) { this.idUsuario = value; }

    public String getIDSuperior() { return idSuperior; }
    public void setIDSuperior(String value) { this.idSuperior = value; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String value) { this.nombreRol = value; }
}
