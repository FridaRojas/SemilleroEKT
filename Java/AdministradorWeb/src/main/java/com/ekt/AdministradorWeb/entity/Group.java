package com.ekt.AdministradorWeb.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document ("group")
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

        public String getID() { return id; }
        public void setID(String value) { this.id = value; }

        public String getNombre() { return nombre; }
        public void setNombre(String value) { this.nombre = value; }

        public User[] getUsuarios() { return usuarios; }
        public void setUsuarios(User[] value) { this.usuarios = value; }

}
