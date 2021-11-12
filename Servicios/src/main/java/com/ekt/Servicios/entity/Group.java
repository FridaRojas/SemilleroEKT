package com.ekt.Servicios.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document
public class Group {
    /**
     *
     */

    private static final long serialVersionUID=1L;

    @Id
    private int idGroup;
    @Field
    private String name;
    @Field
    private User[] users;

    public Group() {
    }

    public Group(int idGroup, String name, User[] users) {
        this.idGroup = idGroup;
        this.name = name;
        this.users = users;
    }

    public Group(String name, User[] users) {
        this.name = name;
        this.users = users;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User[] getUsers() {
        return users;
    }


    public void setUsers(User[] users) {
        this.users = users;
    }


    @Override
    public String toString() {
        return "Gropo{" +
                "id='" + idGroup + '\'' +
                ", nombre='" + name + '\'' +
                ", usuarios=" + Arrays.toString(users) +
                '}';
    }
}
