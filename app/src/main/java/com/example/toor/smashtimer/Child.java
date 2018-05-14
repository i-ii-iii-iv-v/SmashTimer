package com.example.toor.smashtimer;

public class Child {

    private String name;
    private String id;

    public Child(String name, String pId)
    {
        this.name = name;
        id = pId;
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
