package com.example.bj.calendarwork3;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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
        params.y = -100;
        this.getWindow().setAttributes(params);
    }

    public void CancelButtonClicked(View v)
    {
        finish();
    }

    public void AddButtonClicked(View v)
    {
        Log.d("NoteLog" , "AddButton Clicked !");

        String noteContentToSave;
        String noteYearToSave;
        String noteMonthToSave;
        String noteDateToSave;

        EditText noteContent = (EditText) findViewById(R.id.NoteContent);
        noteContentToSave = noteContent.getText().toString();

        EditText noteYear = (EditText) findViewById(R.id.AddNoteYear);
        noteYearToSave = noteYear.getText().toString();

        EditText noteMonth = (EditText) findViewById(R.id.AddNoteMonth);
        noteMonthToSave = noteMonth.getText().toString();

        EditText noteDate = (EditText) findViewById(R.id.AddNoteDate);
        noteDateToSave = noteDate.getText().toString();

        Log.d("NoteLog" , "New note content : "+noteContentToSave);
        Log.d("NoteLog" , "New note year : "+noteYearToSave);
        Log.d("NoteLog" , "New note month : "+noteMonthToSave);
        Log.d("NoteLog" , "New note date : "+noteDateToSave);

        myDb.addNewNote( noteYearToSave , noteMonthToSave , noteDateToSave , noteContentToSave );

        finish();
    }

    private void updateDatabase(String tableName , int dayBlockDay)
    {
        int tableLength = myDb.getTableNoteAmount(tableName);

        if(tableLength>=4)
        {
            tableLength = 4;
        }

        Cursor tableRes = myDb.getRes(tableName);

        for(int i=1; i<=tableLength; ++i)
        {
            String useID = "Day"+dayBlockDay+"_Note"+Integer.toString(i);

            int rID = getResources().getIdentifier(useID , "id" , getPackageName());

            TextView noteText = (TextView) findViewById(rID);

            tableRes.moveToNext();

            String rawString = tableRes.getString(1);

            if(rawString.length()>4)
            {
                rawString = rawString.substring(0, 4);
            }

            noteText.setText(rawString);
        }
    }
}
