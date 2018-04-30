package com.example.toor.smashtimer;

public class Task {

    private String taskName;

    private int hour;
    private int minute;

    private int duration;

    public Task(String name)
    {
        taskName = name;
    }

    public String toString()
    {
        return taskName;
    }

}
