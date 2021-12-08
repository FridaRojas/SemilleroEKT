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
    private String nombreRol;
    private String idGrupo;
    private Object[] opcionales;
    private String token;
    private String telefono;
    private String idSuperiorInmediato;
    private String statusActivo;
    private String curp;
    private String rfc;

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

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String value) { this.nombreRol = value; }

    public String getIDGrupo() { return idGrupo; }
    public void setIDGrupo(String value) { this.idGrupo = value; }

    public Object[] getOpcionales() { return opcionales; }
    public void setOpcionales(Object[] value) { this.opcionales = value; }

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String value) { this.telefono = value; }

    public String getIDSuperiorInmediato() { return idSuperiorInmediato; }
    public void setIDSuperiorInmediato(String value) { this.idSuperiorInmediato = value; }

    public String getStatusActivo() { return statusActivo; }
    public void setStatusActivo(String value) { this.statusActivo = value; }

    public String getCurp() { return curp; }
    public void setCurp(String value) { this.curp = value; }

    public String getRFC() { return rfc; }
    public void setRFC(String value) { this.rfc = value; }
}