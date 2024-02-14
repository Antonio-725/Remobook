package com.example.onset;

public class ControllerLogin {
    String name,email,password,id;
    int Contact;

    public ControllerLogin(String name, String email,  int contact,String password,String id) {
        this.name = name;
        this.email = email;
        this.password = password;
        Contact = contact;
    }

    public ControllerLogin(String name, String email, String password, String id, int contact) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        Contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getContact() {
        return Contact;
    }

    public void setContact(int contact) {
        Contact = contact;
    }
}
