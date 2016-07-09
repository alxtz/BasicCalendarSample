package com.example.bj.calendarwork3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "NoteData.db";

    //TABLE_NAME有個命名規則比較好
    public static final String TABLE_NAME = "TABLE_2016_7_9";

    //每個table需要有的column
    public static final String COL_1 = "NoteID";
    public static final String COL_2 = "NoteContent";

    public DatabaseHelper(Context context)
    {
        //用來產生Database的constructor
        super( context , DATABASE_NAME , null , 1 );

        //Log.d("DatabaseLog" , "Start DatabaseHelper Constructor");
        //Log.d("DatabaseLog" , "The table name is "+TABLE_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        addNewTable(db);

        //Log.d("DatabaseLog" , "End DatabaseHelper Constructor");
    }

    // onCreate()跟onUpgrade()都是預設的重要class
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Log.d("DatabaseLog" , "Start OnCreate");

        //原本是每次onCreate都要創建table
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");

        //Log.d("DatabaseLog" , "End OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Log.d("DatabaseLog" , "Start OnUpgrade");

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

        //Log.d("DatabaseLog" , "End OnUpgrade");
    }

    public void addNewTable(SQLiteDatabase db)
    {
        db.execSQL("create table "+"YouShouldSeeThisDB"+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");
    }
}
