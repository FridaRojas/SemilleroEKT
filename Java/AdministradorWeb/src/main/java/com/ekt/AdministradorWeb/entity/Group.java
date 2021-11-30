package com.ekt.AdministradorWeb.entity;



import java.util.Arrays;

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

        public String getId() {
                return id;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String value) { this.nombre = value; }
        public void setId(String id) {
                this.id = id;
        }

        public User[] getUsuarios() { return usuarios; }
        public void setUsuarios(User[] value) { this.usuarios = value; }
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
