package com.example.bj.calendarwork3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class AddNoteActivity extends AppCompatActivity
{
    //資料庫的部份
    DatabaseHelper myDb;

    int statusBarHeight;
    int screenWidth , screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getDefaultSizes();
        setLayoutSize();

        myDb = new DatabaseHelper(this);
    }

    private void getDefaultSizes()
    {
        //Get The Screen Width and Height first;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Log.d("NoteLog" , "Screen height : "+height);
        Log.d("NoteLog" , "Screen width : "+width);

        screenHeight = height;
        screenWidth = width;

        //Get the status bar height
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("NoteLog" , "Height of status bar : "+result);
        statusBarHeight = result;
    }

    private void setLayoutSize()
    {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.width = (int) (screenWidth*0.9);
        params.width = (int) (720*0.9);
        params.height = 260;
        this.getWindow().setAttributes(params);
    }

}
