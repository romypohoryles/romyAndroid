package com.example.romyprojectandroid;

public class User {
    public String name;
    public String lastName;
    public String email;
    public String dateOfBirth;

    // בנאי ריק נדרש עבור Firebase
    public User() { }

    public User(String name, String lastName, String email, String dateOfBirth) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }
}
