package com.example.bj.calendarwork3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startCalendarActivity(View v)
    {
        Intent calendarActivityIntent = new Intent(this , CalendarActivity.class);

        String monthGot;
        String yearGot;

        EditText inputMonth = (EditText) findViewById(R.id.InputMonth);
        monthGot = inputMonth.getText().toString();
        Log.d( "MyLog" , "Month is : " + monthGot );

        EditText inputYear = (EditText) findViewById(R.id.InputYear);
        yearGot = inputYear.getText().toString();
        Log.d( "MyLog" , "Year is : " + yearGot );

        calendarActivityIntent.putExtra("INPUT_MONTH" , monthGot);
        calendarActivityIntent.putExtra("INPUT_YEAR" , yearGot);

        startActivity(calendarActivityIntent);

        finish();
    }
}
