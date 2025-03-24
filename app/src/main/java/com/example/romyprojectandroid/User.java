package com.example.romyprojectandroid;

import java.io.Serializable;

public class User implements Serializable {
    public String name, lastName, email, dateOfBirth;

    public User() { }

    public User(String name, String lastName, String email, String dateOfBirth) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }
}
