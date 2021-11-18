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
    private String numeroEmpleado;
    private String nombre;
    private String password;
    private Rol[] roles;
    private String telefono;
    private String idSuperiorInmediato;
    private String statusActivo;
    private String curp;
    private String rfc;
    private String token;

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getCorreo() { return correo; }
    public void setCorreo(String value) { this.correo = value; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String value) { this.fechaInicio = value; }

    public String getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(String value) { this.fechaTermino = value; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String value) { this.numeroEmpleado = value; }

    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }

    public String getCurp() { return curp; }
    public void setCurp(String value) { this.curp = value; }

    public String getRfc() { return rfc; }
    public void setRfc(String value) { this.rfc = value; }

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public Rol[] getRoles() { return roles; }
    public void setRoles(Rol[] value) { this.roles = value; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }

    public String getIDSuperiorInmediato() { return idSuperiorInmediato; }
    public void setIDSuperiorInmediato(String value) { this.idSuperiorInmediato = value; }

    public String getStatusActivo() { return statusActivo; }
    public void setStatusActivo(String value) { this.statusActivo = value; }
}
