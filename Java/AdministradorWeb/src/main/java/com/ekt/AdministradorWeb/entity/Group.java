package com.ekt.AdministradorWeb.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document ("group")
public class Group {

        @Id
        private String id;
        private String name;
        private User[] users;

        public String getID() { return id; }
        public void setID(String value) { this.id = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

        public User[] getUsers() { return users; }
        public void setUsers(User[] value) { this.users = value; }

}
