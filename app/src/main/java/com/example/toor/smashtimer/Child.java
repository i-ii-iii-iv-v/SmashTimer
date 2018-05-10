package com.example.toor.smashtimer;

public class Child {

    private String name;
    private String id;
    private String email;

    public Child(String name, String pId, String pEmail)
    {
        this.name = name;
        id = pId;
        email = pEmail;
    }

    public String getEmail()
    {
        return email;
    }

    public String getid()
    {
        return id;
    }


    public String toString()
    {
        return name;
    }
}
