package com.ekt.Servicios.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document("Usuarios")
public class User {

    @Id
    private String id;
    private String correo;
    private String fechaInicio;
    private String fechaTermino;
    private String idUsuario;
    private String nombre;
    private String password;
    private Rol[] roles;
    private String telefono;
    private String idSuperiorInmediato;
    private String status;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getCorreo() { return correo; }
    public void setCorreo(String value) { this.correo = value; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String value) { this.fechaInicio = value; }

    public String getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(String value) { this.fechaTermino = value; }

    public String getIDUsuario() { return idUsuario; }
    public void setIDUsuario(String value) { this.idUsuario = value; }

    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public Rol[] getRoles() { return roles; }
    public void setRoles(Rol[] value) { this.roles = value; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }

    public String getIDSuperiorInmediato() { return idSuperiorInmediato; }
    public void setIDSuperiorInmediato(String value) { this.idSuperiorInmediato = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}
