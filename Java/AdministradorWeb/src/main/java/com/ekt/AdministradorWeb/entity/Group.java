package com.ekt.AdministradorWeb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document("group")
public class Group {

        @Id
        private String id;
        private String nombre;
        private User[] usuarios;

        public Group(String id, String name, User[] usuarios) {
                this.id = id;
                this.nombre = name;
                this.usuarios = usuarios;
        }

        public String getId() {
                return id;
        }
        public void setId(String id) {
                this.id = id;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String value) { this.nombre = value; }


        public User[] getUsuarios() { return usuarios; }
        public void setUsuarios(User[] value) { this.usuarios = value; }

        @Override
        public String toString() {
                return "Grupo{" +
                        "id='" + id + '\'' +
                        ", nombre='" + nombre + '\'' +
                        ", usuarios=" + Arrays.toString(usuarios) +
                        '}';
        }

}
