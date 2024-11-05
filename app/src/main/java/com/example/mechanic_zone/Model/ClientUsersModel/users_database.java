package com.example.mechanic_zone.Model.ClientUsersModel;

public class users_database {

    String Name,Email,Password;
    Integer Latt,Long;


    public users_database() {

    }

    public users_database(String name, String email, String password) {
        Name = name;
        Email = email;
        Password = password;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
