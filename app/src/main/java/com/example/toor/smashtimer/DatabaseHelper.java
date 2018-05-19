package com.example.toor.smashtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    SQLiteDatabase db;
    public static final int TBCHILD = 0x100;
    public static final int TBTASKS = 0x1;
    public static final int TBQUERY = 0x10;
    public static final String DATABASE_NAME = "SmashTimer.db";
    public static final String TABLE_TASK = "Task";

    public static final String TABLE_CHILDREN = "Children";
    public static final String CHILDREN_COL_CHILDID = "childid";
    public static final String CHILDREN_COL_CHILDNAME = "childName";

    public static final String TASKS_COL_CHILDID = "childid";
    public static final String TASKS_COL_TASKNAME = "taskName";
    public static final String TASKS_COL_STARTTIME = "startTime";
    public static final String TASKS_COL_ENDTIME = "endTime";
    public static final String TASKS_COL_DAYS = "day";
    public static final String TASKS_COL_ALARM = "alarm";

    public static final String TABLE_QUERY_QUEUE = "QueryQueue";
    public static final String QUERY_QUEUE_COL_ID = "id";
    public static final String QUERY_QUEUE_QUERY = "queryStr";

    public static boolean checkDatabase(Context context)
    {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TASK +" ( "
                +TASKS_COL_CHILDID + " varchar(50), "
                + TASKS_COL_TASKNAME+ " varchar(255), "
                + TASKS_COL_STARTTIME + " TIME, "
                + TASKS_COL_ENDTIME + " TIME, "
                + TASKS_COL_DAYS + " int,"
                + TASKS_COL_ALARM + " int" +");");


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CHILDREN +" ( "
                +CHILDREN_COL_CHILDID + " varchar(50), "
                + CHILDREN_COL_CHILDNAME + " varchar(50) "  + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_QUERY_QUEUE +" ( "
                + QUERY_QUEUE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUERY_QUEUE_QUERY + " varchar(500) "  + ");");
    }

    public ArrayList<Child> initChildList()
    {
        ArrayList<Child> clist = new ArrayList<Child>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CHILDREN, null );

        if(c.moveToFirst())
        {
            for(int i = 0; i < c.getCount(); i++)
            {
                String childName = c.getString(c.getColumnIndex(CHILDREN_COL_CHILDNAME));
                String childId = c.getString(c.getColumnIndex(CHILDREN_COL_CHILDID));
                clist.add(new Child(childName, childId));
                c.moveToNext();
            }
        }

        return clist;
    }
    public ArrayList<Task>[] queryInitTaskList(String childid)
    {
        String[] arg = new String[1];
        arg[0] = childid;

        ArrayList<Task>[] tasklist = new ArrayList[7];
        for(int i = 0; i < tasklist.length; i++)
        {
            tasklist[i] = new ArrayList<Task>();
        }

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASK +" WHERE " + TASKS_COL_CHILDID + " = ?", arg);
        if(c.moveToFirst())
        {
            for(int i = 0; i < c.getCount(); i++)
            {

                String taskName = c.getString(c.getColumnIndex(TASKS_COL_TASKNAME));

                int index = c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)).indexOf(':', 0);
                int startHour = Integer.parseInt(c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)).substring(0,index));
                int index2 = c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)).indexOf(':', index+1);
                int startMinute =  Integer.parseInt(c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)).substring(index+1,index2));

                index = c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)).indexOf(':', 0);
                int endHour = Integer.parseInt(c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)).substring(0,index));
                index2 = c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)).indexOf(':', index+1);
                int endMinute = Integer.parseInt(c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)).substring(index+1,index2));
                int day = c.getInt(c.getColumnIndex(TASKS_COL_DAYS));
                int alarm = c.getInt(c.getColumnIndex(TASKS_COL_ALARM));
                tasklist[day].add(new Task(taskName, childid, startHour, startMinute, endHour, endMinute, day, alarm));
                //Log.d(Integer.toString(i), TASKS_COL_CHILDID +": " + c.getString(c.getColumnIndex(TASKS_COL_CHILDID)));
                //Log.d(Integer.toString(i), TASKS_COL_TASKNAME+": " + c.getString(c.getColumnIndex(TASKS_COL_TASKNAME)));
                //Log.d(Integer.toString(i), TASKS_COL_STARTTIME +": "+ c.getString(c.getColumnIndex(TASKS_COL_STARTTIME)));
                //Log.d(Integer.toString(i), TASKS_COL_ENDTIME +": "+ c.getString(c.getColumnIndex(TASKS_COL_ENDTIME)));
                //Log.d(Integer.toString(i), TASKS_COL_DAYS +": "+ c.getString(c.getColumnIndex(TASKS_COL_DAYS)));
                c.moveToNext();
            }

        }

        return tasklist;
    }

    public void addTask(String childid, Task task)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TASKS_COL_CHILDID, childid);
        initialValues.put(TASKS_COL_TASKNAME, task.toString());
        initialValues.put(TASKS_COL_STARTTIME, Integer.toString(task.getStartHour()) + ":" + task.getStartMin() + ":00");
        initialValues.put(TASKS_COL_ENDTIME, Integer.toString(task.getEndHour()) + ":" + task.getEndMin() + ":00");
        initialValues.put(TASKS_COL_DAYS, task.getDay());
        initialValues.put(TASKS_COL_ALARM, task.getAlarm());
        db.insert(TABLE_TASK, null, initialValues);
    }

    public void addChildTest(String childid, String childname)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CHILDREN_COL_CHILDID, childid);
        initialValues.put(CHILDREN_COL_CHILDNAME, childname);
        db.insert(TABLE_CHILDREN, null, initialValues);
    }

    public void testQueries()
    {
        /*
        ContentValues initialValues = new ContentValues();
        initialValues.put(TASKS_COL_CHILDID, "temp");
        initialValues.put(TASKS_COL_TASKNAME, "different task");
        initialValues.put(TASKS_COL_STARTTIME, "12:12:00");
        initialValues.put(TASKS_COL_ENDTIME, "05:00:00");
        initialValues.put(TASKS_COL_DAYS, "1");
        db.insert(TABLE_TASK, null, initialValues);
        //Log.d("5", Long.toString(db.insert(TABLE_TASK, null, initialValues)));*/

        //resetDatabase(TBTASKS|TBCHILD);
        //addChildTest("ksuk1996@gmail.com", "Alex");
        //addChildTest("ksuk2012@gmail.com", "KSY");

        showTables(TBTASKS|TBCHILD);

    }

    public void showTables(int j)
    {
        Cursor c = null;
        if((j&0x100) == 0x100)
        {
            c = db.rawQuery("SELECT * FROM " + TABLE_CHILDREN, null );
            if(c.moveToFirst())
            {
                for(int i = 0; i < c.getCount(); i++)
                {
                    Log.d(Integer.toString(i)+": ", c.getString(c.getColumnIndex(CHILDREN_COL_CHILDNAME)));
                    Log.d(Integer.toString(i)+": ", c.getString(c.getColumnIndex(CHILDREN_COL_CHILDID)));
                    c.moveToNext();
                }
            }
        }

        if((j&0x001) == 0x1)
        {
            c = db.rawQuery("SELECT * FROM " + TABLE_TASK, null);
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

        if((j&0x010) == 0x010)
        {
            //show saved queries
        }

    }

    public void resetDatabase(int i)
    {
        if((i&0x001) == 0x1)
            db.execSQL("DELETE FROM " + TABLE_TASK);

        if((i&0x010) == 0x010)
            db.execSQL("DELETE FROM " + TABLE_QUERY_QUEUE);

        if((i&0x100) == 0x100)
            db.execSQL("DELETE FROM " + TABLE_CHILDREN);
    }

    public void deleteAllTasksChild(String childid)
    {
        String whereClause = TASKS_COL_CHILDID + " = ?";
        String args[] = new String[]{childid};
        db.delete(TABLE_TASK, whereClause, args);
    }
    public boolean removeTask(Task task)
    {
        String whereClause = TASKS_COL_CHILDID + " = ? AND " +
                TASKS_COL_STARTTIME + " = ? AND " +
                TASKS_COL_ENDTIME + " = ? AND " +
                TASKS_COL_DAYS + " = ? AND " +
                TASKS_COL_TASKNAME + " = ?";
        String args[] = new String[]{
                task.getChildid(),
                Integer.toString(task.getStartHour()) + ":" + task.getStartMin() + ":00",
                Integer.toString(task.getEndHour()) + ":" + task.getEndMin() + ":00",
                Integer.toString(task.getDay()),
                task.toString()
        };
        if (db.delete(TABLE_TASK, whereClause, args) < 1)
        {
            return false;
        }
        return true;
    }

    public void restoreAllTable()
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUERY_QUEUE);
        db.execSQL("CREATE TABLE " + TABLE_TASK +" ( "
                +TASKS_COL_CHILDID + " varchar(50), "
                + TASKS_COL_TASKNAME+ " varchar(255), "
                + TASKS_COL_STARTTIME + " TIME, "
                + TASKS_COL_ENDTIME + " TIME, "
                + TASKS_COL_DAYS + " int,"
                + TASKS_COL_ALARM + " int" +");");


        db.execSQL("CREATE TABLE " + TABLE_CHILDREN +" ( "
                +CHILDREN_COL_CHILDID + " varchar(50), "
                + CHILDREN_COL_CHILDNAME + " varchar(50) "  + ");");

        db.execSQL("CREATE TABLE " + TABLE_QUERY_QUEUE +" ( "
                + QUERY_QUEUE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QUERY_QUEUE_QUERY + " varchar(500) "  + ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}
