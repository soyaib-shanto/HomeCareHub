package com.example.mechanic_zone.Model.ServiceProviderModel;

public class mechanic_database {
    String Name, Title,Phone, Email,Password;

    public mechanic_database()
    {

    }

    public mechanic_database(String name, String title,String phone, String email, String password) {
        Name = name;
        Title = title;
        Phone = phone;
        Email = email;
        Password = password;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {Phone = phone;}

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
