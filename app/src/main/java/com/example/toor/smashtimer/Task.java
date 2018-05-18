package com.example.toor.smashtimer;

public class Task {

    public static final int M = 0;
    public static final int TU = 1;
    public static final int W = 2;
    public static final int TH = 3;
    public static final int F = 4;
    public static final int SA = 5;
    public static final int SU = 6;

    private String taskName;

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int day;
    private String childid;
    private int alarm;


    public int getStartHour()
    {
        return startHour;
    }
    public int getStartMin()
    {
        return startMinute;
    }
    public int getEndHour()
    {
        return endHour;
    }
    public int getEndMin()
    {
        return endMinute;
    }
    public int getDay()
    {
        return day;
    }
    public int getAlarm(){return alarm;}
    public String getChildid()
    {
        return childid;
    }

    public Task(String name, String childid, int sh, int sm, int eh, int em, int day, int alarm)
    {
        this.childid = childid;
        taskName = name;
        startHour = sh;
        startMinute = sm;
        endHour = eh;
        endMinute = em;
        this.day = day;
        this.alarm = alarm;
    }

    public String getStartTS()
    {
        return Utility.taskTimeToTS(startHour, startMinute);
    }

    public String getEndTS()
    {
        return Utility.taskTimeToTS(endHour, endMinute);
    }

    public String toString()
    {
        return taskName;
    }

}
