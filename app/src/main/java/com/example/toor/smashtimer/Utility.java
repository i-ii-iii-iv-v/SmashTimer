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
}
