package com.ekt.Servicios.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "BitacoraTarea")
public class TaskLog {

    @Id
    private String id_bitacora;

    //Usuario Emisor
    @Field(name = "id_emisor")
    private String id_emisor;
    @Field(name = "nombre_emisor")
    private String nombre_emisor;

    //Accion
    @Field(name = "accion")
    private String accion;

    //ID tarea actulizada
    private String id_tarea;

    @Field(name = "fecha_actualizacion")//Cambiar a Timestamp (hora)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fecha_actualizacion;

    //Status
    @Field(name = "estatus")
    private String estatus;

    public TaskLog() {
    }

    public TaskLog(String id_emisor, String id_tarea, String nombre_emisor, String accion, Date fecha_actualizacion, String estatus) {
        this.id_emisor = id_emisor;
        this.id_tarea = id_tarea;
        this.nombre_emisor = nombre_emisor;
        this.accion = accion;
        this.fecha_actualizacion = fecha_actualizacion;
        this.estatus = estatus;
    }

    public String getId_bitacora() {
        return id_bitacora;
    }

    public void setId_bitacora(String id_bitacora) {
        this.id_bitacora = id_bitacora;
    }

    public String getId_emisor(String id_emisor) {
        return this.id_emisor;
    }

    public void setId_emisor(String id_emisor) {
        this.id_emisor = id_emisor;
    }

    public String getNombre_emisor(String nombre_emisor) {
        return this.nombre_emisor;
    }

    public void setNombre_emisor(String nombre_emisor) {
        this.nombre_emisor = nombre_emisor;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Date getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(Date fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public String getEstatus(String estatus) {
        return this.estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }
}
