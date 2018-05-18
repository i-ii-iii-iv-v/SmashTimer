package com.example.toor.smashtimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utility {
    public static final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    public static boolean clientTSbeforeServerTs(String firstTS, String secondTS)
    {
        Date firstDate, secondDate;
        ParsePosition pos = new ParsePosition(0);
        firstDate = dateFmt.parse(firstTS, pos);
        pos = new ParsePosition(0);
        secondDate = dateFmt.parse(secondTS, pos);

        return firstDate.before(secondDate);
    }

    public static int TStoHour(String timestamp)
    {
        int index = timestamp.indexOf(':', 0);
        int hour = Integer.parseInt(timestamp.substring(0,index));

        return hour;
    }

    public static int TStoMin(String timestamp)
    {
        int index = timestamp.indexOf(':', 0);
        int hour = Integer.parseInt(timestamp.substring(0,index));
        int index2 = timestamp.indexOf(':', index+1);
        int minute =  Integer.parseInt(timestamp.substring(index+1,index2));

        return minute;
    }

    public static String minuteFormat(String minute)
    {
        if(minute.length() == 1)
        {
            minute = "0"+ minute;
        }

        return minute;
    }
}
