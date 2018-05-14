package com.example.toor.smashtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SmashTimer.db";
    public static final String TABLE_TASKS = "Tasks";

    public static final String TASKS_COL_CHILDID = "childid";
    public static final String TASKS_COL_TASKNAME = "taskName";
    public static final String TASKS_COL_STARTTIME = "startTime";
    public static final String TASKS_COL_ENDTIME = "endTime";
    public static final String TASKS_COL_DAYS = "day";

    public static boolean checkDatabase(Context context)
    {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TASKS +" ( "
                +TASKS_COL_CHILDID + " varchar(50), "
                + TASKS_COL_TASKNAME+ " varchar(255), "
                + TASKS_COL_STARTTIME + " TIME, "
                + TASKS_COL_ENDTIME + " TIME, "
                + TASKS_COL_DAYS + " varchar(5)" +")");
    }

    public static void testQueries(SQLiteDatabase db)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TASKS_COL_CHILDID, "sdfds@ddfs");
        initialValues.put(TASKS_COL_TASKNAME, "do something");
        initialValues.put(TASKS_COL_STARTTIME, "12:12:00");
        initialValues.put(TASKS_COL_ENDTIME, "05:00:00");
        initialValues.put(TASKS_COL_DAYS, "Tu");

        //Log.d("5", Long.toString(db.insert(TABLE_TASKS, null, initialValues)));
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
        if(c.moveToFirst())
        {
            for(int i = 0; i < c.getCount(); i++)
            {
                Log.d(Integer.toString(i), TASKS_COL_CHILDID +": " + c.getString(c.getColumnIndex(TASKS_COL_CHILDID)));
                Log.d(Integer.toString(i), TASKS_COL_TASKNAME+": " + c.getString(c.getColumnIndex(TASKS_COL_TASKNAME)));
                Log.d(Integer.toString(i), TASKS_COL_STARTTIME +": "+ c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)));
                Log.d(Integer.toString(i), TASKS_COL_ENDTIME +": "+ c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)));
                Log.d(Integer.toString(i), TASKS_COL_DAYS +": "+ c.getString(c.getColumnIndex(TASKS_COL_DAYS)));
                c.moveToNext();
            }

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
