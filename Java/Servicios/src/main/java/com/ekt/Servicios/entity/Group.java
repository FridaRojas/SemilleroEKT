package com.ekt.Servicios.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document ("group")
public class Group {

        @Id
        private String id;
        private String nombre;
        private User[] usuarios;

        public Group() {
        }

        public Group(String nombre, User[] usuarios) {
                this.nombre = nombre;
                this.usuarios = usuarios;
        }

        public Group(String id, String nombre, User[] usuarios) {
                this.id = id;
                this.nombre = nombre;
                this.usuarios = usuarios;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getNombre() {
                return nombre;
        }

        public void setNombre(String nombre) {
                this.nombre = nombre;
        }

        public User[] getUsuarios() {
                return usuarios;
        }

        public void setUsuarios(User[] usuarios) {
                this.usuarios = usuarios;
        }

        @Override
        public String toString() {
                return "Grupo{" +
                        "id='" + id + '\'' +
                        ", nombre='" + nombre + '\'' +
                        ", usuarios=" + Arrays.toString(usuarios) +
                        '}';
        }
}
