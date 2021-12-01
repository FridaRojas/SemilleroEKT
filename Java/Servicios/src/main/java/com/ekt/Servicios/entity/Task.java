package com.ekt.Servicios.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "Tareas")
public class Task implements Serializable {
    @Id
    private String id_tarea;

    @Field(name = "id_grupo")
    private String id_grupo;

    //Usuario Emisor
    @Field(name = "id_emisor")
    private String id_emisor;
    @Field(name = "nombre_emisor")
    private String nombre_emisor;

    //Usuario Receptor
    @Field(name = "id_receptor")
    private String id_receptor;
    @Field(name = "nombre_receptor")
    private String nombre_receptor;

    //Fechas
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field(name = "fecha_ini")
    private Date fecha_ini;
    //Fecha de inicio real
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field(name = "fecha_iniR")
    private Date fecha_iniR;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field(name = "fecha_fin")
    private Date fecha_fin;
    //Fecha de termino real
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field(name = "fecha_finR")
    private Date fecha_finR;

    //Información de tarea
    @Field(name = "titulo")
    private String titulo;
    //Mensaje
    @Field(name = "descripcion")
    private String descripcion;

    //Status
    @Field(name = "estatus")
    private String estatus;

    //Valor de tarea vista
    @Field(name = "leido")
    private boolean leido;
    //Fecha en la que sea vio la tarea
    @Field(name = "leido_fecha")//Cambiar a Timestamp (hora)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaLeido;
    @Field(name = "prioridad")
    private String prioridad;

    //Fecha de creacion de tarea en BD
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Field(name = "fecha_BD")
    private LocalDateTime fecha_BD;

    //Observaciones de la tarea
    @Field(name = "observaciones")
    private String observaciones;

    //Guarda el uri del archivo
    @Field(name = "archivo")
    private String archivo;

    public Task() {
    }
    public Task(String tareaId){
        this.id_tarea=tareaId;
    }

    public Task(String id_grupo, String id_emisor, String nombre_emisor, String id_receptor, String nombre_receptor, Date fecha_ini, Date fecha_fin, String titulo, String descripcion, String prioridad, String estatus, boolean leido, String observaciones) {
        this.id_grupo = id_grupo;
        this.id_emisor = id_emisor;
        this.nombre_emisor = nombre_emisor;
        this.id_receptor = id_receptor;
        this.nombre_receptor = nombre_receptor;
        this.fecha_ini = fecha_ini;
        this.fecha_fin = fecha_fin;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estatus = estatus;
        this.leido = leido;
        this.observaciones = observaciones;
    }
    public Task(Task tarea) {
    }
    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(String id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getId_emisor() {
        return id_emisor;
    }

    public void setId_emisor(String id_emisor) {
        this.id_emisor = id_emisor;
    }

    public String getNombre_emisor() {
        return nombre_emisor;
    }

    public void setNombre_emisor(String nombre_emisor) {
        this.nombre_emisor = nombre_emisor;
    }

    public String getId_receptor() {
        return id_receptor;
    }

    public void setId_receptor(String id_receptor) {
        this.id_receptor = id_receptor;
    }

    public String getNombre_receptor() {
        return nombre_receptor;
    }

    public void setNombre_receptor(String nombre_receptor) {
        this.nombre_receptor = nombre_receptor;
    }

    public Date getFecha_ini() {
        return fecha_ini;
    }

    public void setFecha_ini(Date fecha_ini) {
        this.fecha_ini = fecha_ini;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }



    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFecha_BD() {
        return fecha_BD;
    }

    public void setFecha_BD(LocalDateTime fecha_BD) {
        this.fecha_BD = fecha_BD;
    }

    public LocalDateTime getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(LocalDateTime fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    public Date getFecha_iniR() {
        return fecha_iniR;
    }

    public void setFecha_iniR(Date fecha_iniR) {
        this.fecha_iniR = fecha_iniR;
    }

    public Date getFecha_finR() {
        return fecha_finR;
    }

    public void setFecha_finR(Date fecha_finR) {
        this.fecha_finR = fecha_finR;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
