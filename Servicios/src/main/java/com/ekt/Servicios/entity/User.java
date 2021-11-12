package com.ekt.Servicios.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document
public class User {
    /**
     *
     */

    private static final long serialVersionUID=1L;

    @Id
    private int idUser;
    @Field
    private String mail;
    @Field
    private String name;
    @Field
    private String password;
    @Field
    private String fechaInicio;
    @Field
    private String fechaTermino;
    @Field
    private String phone;
    @Field
    private boolean status;
    @Field
    private String idInmediateSuperior;
    @Field
    private Rol[] rols;


    //constructor sin campos
    public User() {
    }

    //constructor todos los campos
    public User(int idUser, String mail, String name, String password,
                String fechaInicio, String fechaTermino, String phone,
                boolean status, String idInmediateSuperior, Rol[] rols) {
        this.idUser = idUser;
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.phone = phone;
        this.status = status;
        this.idInmediateSuperior = idInmediateSuperior;
        this.rols = rols;
    }

    //constructor todos los campos excepto id


    public User(String mail, String name, String password, String fechaInicio,
                String fechaTermino, String phone, boolean status,
                String idInmediateSuperior, Rol[] rols) {
        this.mail = mail;
        this.name = name;
        this.password = password;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.phone = phone;
        this.status = status;
        this.idInmediateSuperior = idInmediateSuperior;
        this.rols = rols;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(String fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIdInmediateSuperior() {
        return idInmediateSuperior;
    }

    public void setIdInmediateSuperior(String idInmediateSuperior) {
        this.idInmediateSuperior = idInmediateSuperior;
    }

    public Rol[] getRols() {
        return rols;
    }

    public void setRols(Rol[] rols) {
        this.rols = rols;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + idUser + '\'' +
                ", correo='" + mail + '\'' +
                ", nombre='" + name + '\'' +
                ", contraseña='" + password + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaTermino='" + fechaTermino + '\'' +
                ", telefono='" + phone + '\'' +
                ", status=" + status +
                ", id del superior inmediato='" + idInmediateSuperior + '\'' +
                ", roles=" + Arrays.toString(rols) +
                '}';
    }
}
