package com.example.bj.calendarwork3;

import android.content.ContentValues;
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
        super( context , DATABASE_NAME , null , 1 );
    }

    // onCreate()跟onUpgrade()都是預設的重要class
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //這邊可以新增一些預設的table，有需要再加
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //這個部份在做版本更新的時候才會呼叫到，所以現在可以忽視
    }

    public void addNewNote( String year , String month , String date , String noteContent )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //先創建table
        String TABLE_NAME = "TABLE_"+year+"_"+month+"_"+date;
        db.execSQL("create table "+TABLE_NAME+" (NoteID INTEGER PRIMARY KEY AUTOINCREMENT,NoteContent TEXT)");

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2 , noteContent);

        db.insert(TABLE_NAME , null , contentValues);
    }
}
