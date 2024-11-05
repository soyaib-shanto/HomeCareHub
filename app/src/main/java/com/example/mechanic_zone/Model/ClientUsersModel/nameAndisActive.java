package com.example.mechanic_zone.Model.ClientUsersModel;

public class nameAndisActive {
     String name;
     int isOnline;
    public nameAndisActive() {

    }


    public nameAndisActive(String name,int isOnline) {
        this.name = name;
        this.isOnline = isOnline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}
