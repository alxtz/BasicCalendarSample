package com.example.bj.calendarwork3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity
{
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        myDb = new DatabaseHelper(this);

        monthGot = Integer.parseInt(getIntent().getStringExtra("INPUT_MONTH"));
        yearGot = Integer.parseInt(getIntent().getStringExtra("INPUT_YEAR"));

        Log.d( "MyLog" , "Calendar Month is : " + monthGot );
        Log.d( "MyLog" , "Calendar Year is : " + yearGot );

        setYearMonthDay();//設定閏年的2月天數
        setDays();
        setMonthYearDisplay();
        //setDays();

        getStatusBarHeight();
        getScreenWidth();
        getDayBlockSize();
        setTopBar();
        setCalendarRow1();
        setCalendarRow2();
        setCalendarRow3();
        setCalendarRow4();
        setCalendarRow5();
        setCalendarRow6();

        setCalendar();

        if( yearGot==2016 && monthGot==1)
        {
            disableArrowLeft();
        }

        hideAllNotes();

        checkDatabaseNotes();

        changeNoteContent(20 , 3);
    }


    //2015年1月1日 是 禮拜四
    static final int DAY_2016_1_1 = 5;
    static final int DEFAULT_YEAR_2016 = 2016;

    //今年1月1日是禮拜幾
    int THIS_YEAR_1_1;

    //每個月的長度
    int[] MONTH_LENGTH_LIST = {31 , 0 , 31 , 30 , 31 , 30 , 31 , 31 , 30 , 31 , 30 , 31};

    //設定一個int紀錄我們自從2015/1/1後過了幾天，方便換算星期幾
    int daysAfter2016_1_1 = 0;

    int monthGot;
    int yearGot;

    //紀錄是否為閏年
    boolean isLeapYear;

    //我們這邊最小年月，使用2015年1月
    public void setYearMonthDay()
    {
        if( (yearGot%400==0) || ( yearGot%4==0 && yearGot%100!=0 ) )
        {
            Log.d( "MyLog" , "Leap Year" );
            MONTH_LENGTH_LIST[1] = 29;
            isLeapYear = true;
        }
        else
        {
            Log.d( "MyLog" , "Average Year" );
            MONTH_LENGTH_LIST[1] = 28;
            isLeapYear = false;
        }
    }

    private boolean isLeap(int inputYear)
    {
        if( (inputYear%400==0) || ( inputYear%4==0 && inputYear%100!=0 ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setDays()
    {
        Log.d("SetDayLog" , "This year is "+yearGot);
        Log.d("SetDayLog" , "This month is "+monthGot);

        for(int i = 2016; i < yearGot; ++i)
        {
            Log.d("SetDayLog" , "Inside for loop");
            if(isLeap(i)==true)
            {
                daysAfter2016_1_1+=366;
                Log.d("SetDayLog" , "Add a Leap Year");
            }
            else
            {
                daysAfter2016_1_1+=365;
                Log.d("SetDayLog" , "Add an Average Year");
            }
        }

        Log.d("SetDayLog" , "Days after 2016/1/1 : "+daysAfter2016_1_1);

        //一直加月份的天數，直到指定的月份
        for(int i=0; i<monthGot-1; ++i)
        {
            daysAfter2016_1_1 += MONTH_LENGTH_LIST[i];
        }

        if((monthGot-1)==0)
        {
            Log.d( "SetDayLog" , "在"+(yearGot-1)+"年"+(12)+"月底時，過了"+daysAfter2016_1_1+"天");
        }
        else
        {
            Log.d("SetDayLog", "在" + yearGot + "年" + (monthGot - 1) + "月底時，過了" + daysAfter2016_1_1 + "天");
        }
        //int whatDay = (daysAfter2016_1_1 + DAY_2016_1_1)%7;

        //Log.d( "SetDayLog" , +yearGot+"年"+(monthGot)+"月初，是禮拜"+whatDay);
    }


    //狀態欄的寬度
    int statusBarHeight;

    //螢幕的長寬
    int screenHeight;
    int screenWidth;

    //topBar的height
    int topBarHeight = 90;

    //一個dayBlock的長寬
    int dayBlockWidth;
    int dayBlockHeight;

    //defaultRow
    int defaultRowHeight = 60;

    public void getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("MyLog" , "Height of status bar : "+result);
        statusBarHeight = result;
    }

    public void getScreenWidth()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Log.d("MyLog" , "Screen height : "+height);
        Log.d("MyLog" , "Screen width : "+width);

        screenHeight = height;
        screenWidth = width;
    }

    public void getDayBlockSize()
    {
        dayBlockWidth = screenWidth/7;
        dayBlockHeight = (screenHeight - statusBarHeight - topBarHeight - defaultRowHeight)/6;
    }

    public void setTopBar()
    {
        RelativeLayout topBar = (RelativeLayout) findViewById(R.id.TopBar);
        ViewGroup.LayoutParams params = topBar.getLayoutParams();

        params.height = topBarHeight;
        topBar.setLayoutParams(params);
    }

    public void setCalendarRow1()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock1);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock2);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock3);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock4);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock5);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock6);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock7);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendarRow2()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock8);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock9);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock10);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock11);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock12);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock13);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock14);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendarRow3()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock15);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock16);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock17);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock18);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock19);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock20);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock21);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendarRow4()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock22);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock23);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock24);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock25);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock26);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock27);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock28);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendarRow5()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock29);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock30);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock31);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock32);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock33);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock34);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock35);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendarRow6()
    {
        RelativeLayout dayBlock1 = (RelativeLayout) findViewById(R.id.DayBlock36);
        ViewGroup.LayoutParams params1 = dayBlock1.getLayoutParams();
        params1.height = dayBlockHeight;
        params1.width = dayBlockWidth;
        dayBlock1.setLayoutParams(params1);

        RelativeLayout dayBlock2 = (RelativeLayout) findViewById(R.id.DayBlock37);
        ViewGroup.LayoutParams params2 = dayBlock2.getLayoutParams();
        params2.height = dayBlockHeight;
        params2.width = dayBlockWidth;
        dayBlock2.setLayoutParams(params2);

        RelativeLayout dayBlock3 = (RelativeLayout) findViewById(R.id.DayBlock38);
        ViewGroup.LayoutParams params3 = dayBlock3.getLayoutParams();
        params3.height = dayBlockHeight;
        params3.width = dayBlockWidth;
        dayBlock3.setLayoutParams(params3);

        RelativeLayout dayBlock4 = (RelativeLayout) findViewById(R.id.DayBlock39);
        ViewGroup.LayoutParams params4 = dayBlock4.getLayoutParams();
        params4.height = dayBlockHeight;
        params4.width = dayBlockWidth;
        dayBlock4.setLayoutParams(params4);

        RelativeLayout dayBlock5 = (RelativeLayout) findViewById(R.id.DayBlock40);
        ViewGroup.LayoutParams params5 = dayBlock5.getLayoutParams();
        params5.height = dayBlockHeight;
        params5.width = dayBlockWidth;
        dayBlock5.setLayoutParams(params5);

        RelativeLayout dayBlock6 = (RelativeLayout) findViewById(R.id.DayBlock41);
        ViewGroup.LayoutParams params6 = dayBlock6.getLayoutParams();
        params6.height = dayBlockHeight;
        params6.width = dayBlockWidth;
        dayBlock6.setLayoutParams(params6);

        RelativeLayout dayBlock7 = (RelativeLayout) findViewById(R.id.DayBlock42);
        ViewGroup.LayoutParams params7 = dayBlock7.getLayoutParams();
        params7.height = dayBlockHeight;
        params7.width = screenWidth-dayBlockWidth*6;
        dayBlock7.setLayoutParams(params7);
    }

    public void setCalendar()
    {
        Log.d( "MyLog" , "開始setCalendar" );
        Log.d( "MyLog" , "月份為"+monthGot+"，年份為"+yearGot );


        //一直加月份的天數，直到指定的月份
        /*
        for(int i=0; i<monthGot-1; ++i)
        {
            daysAfter2016_1_1 += MONTH_LENGTH_LIST[i];
        }

        Log.d( "MyLog" , "在"+yearGot+"年"+(monthGot-1)+"月底時，過了"+daysAfter2016_1_1+"天");
        */

        int whatDay = (daysAfter2016_1_1 + DAY_2016_1_1)%7;

        Log.d( "SetDayLog" , yearGot+"年"+(monthGot)+"月初，是禮拜"+whatDay);

        int textDateCount = 1 + whatDay;

        //設定好當月的日期
        for(int i=1; i<=MONTH_LENGTH_LIST[monthGot-1]; ++i)
        {
            String foo = "DateText" + textDateCount;

            Log.d( "MyLog" , "使用ID為"+foo);

            int resID = getResources().getIdentifier(foo , "id" , getPackageName());

            TextView someDateText = (TextView) findViewById(resID);
            someDateText.setText(Integer.toString(i));

            textDateCount++;
        }

        int textDateCountLast = whatDay;

        //設定上個月的日期，1月會有問題
        if(monthGot==1)
        {
            for (int i = MONTH_LENGTH_LIST[11]; 1 > 0; i--)
            {
                if (textDateCountLast > 0)
                {
                    String foo = "DateText" + textDateCountLast;
                    Log.d("MyLog", "使用ID為" + foo);
                    int resID = getResources().getIdentifier(foo, "id", getPackageName());
                    TextView someDateText = (TextView) findViewById(resID);
                    someDateText.setText(Integer.toString(i));

                    textDateCountLast--;
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
            for (int i = MONTH_LENGTH_LIST[monthGot - 2]; 1 > 0; i--)
            {
                if (textDateCountLast > 0)
                {
                    String foo = "DateText" + textDateCountLast;
                    Log.d("MyLog", "使用ID為" + foo);
                    int resID = getResources().getIdentifier(foo, "id", getPackageName());
                    TextView someDateText = (TextView) findViewById(resID);
                    someDateText.setText(Integer.toString(i));

                    textDateCountLast--;
                }
                else
                {
                    break;
                }
            }
        }
        //設定下個月的日期，12月會有問題
        //int i = whatDay + MONTH_LENGTH_LIST[monthGot-1]+1;
        //Log.d( "MyLog" , "下個月的開始dateBlock編號為"+i);
        int newMonthDate = 1;

        for(int i = whatDay + MONTH_LENGTH_LIST[monthGot-1]+1; i<=42; ++i)
        {
            String foo = "DateText" + i;
            Log.d( "MyLog" , "使用ID為"+foo);
            int resID = getResources().getIdentifier(foo , "id" , getPackageName());
            TextView someDateText = (TextView) findViewById(resID);
            someDateText.setText(Integer.toString(newMonthDate));
            newMonthDate++;
        }

    }

    public void setMonthYearDisplay()
    {
        TextView monthTextView = (TextView) findViewById(R.id.MonthDisplay);
        monthTextView.setText(Integer.toString(monthGot));

        TextView yearTextView = (TextView) findViewById(R.id.YearDisplay);
        yearTextView.setText(Integer.toString(yearGot));
    }

    //左右按鈕的功能
    public void arrowLeft(View v)
    {
        if((monthGot)==1)
        {
            monthGot=13;
            yearGot-=1;
        }

        Log.d("MyLog" , "Pressed Left");

        Intent calendarActivityIntent = new Intent(this , CalendarActivity.class);

        String newMonthGot = Integer.toString(monthGot - 1);
        String newYearGot = Integer.toString(yearGot);

        calendarActivityIntent.putExtra("INPUT_MONTH" , newMonthGot);
        calendarActivityIntent.putExtra("INPUT_YEAR" , newYearGot);

        Log.d("ArrowLog" , "New MonthYear is "+newYearGot+"/"+newMonthGot);

        startActivity(calendarActivityIntent);

        finish();
    }

    public void arrowRight(View v)
    {
        if((monthGot)==12)
        {
            monthGot=0;
            yearGot+=1;
        }

        Log.d("MyLog" , "Pressed Right");

        Intent calendarActivityIntent = new Intent(this , CalendarActivity.class);

        String newMonthGot = Integer.toString(monthGot+1);
        String newYearGot = Integer.toString(yearGot);

        calendarActivityIntent.putExtra("INPUT_MONTH" , newMonthGot);
        calendarActivityIntent.putExtra("INPUT_YEAR" , newYearGot);

        Log.d("ArrowLog" , "New MonthYear is "+newYearGot+"/"+newMonthGot);

        startActivity(calendarActivityIntent);

        finish();
    }

    private void disableArrowLeft()
    {
        ImageView arrowLeft = (ImageView) findViewById(R.id.LeftArrow);
        arrowLeft.setVisibility(View.INVISIBLE);
    }

    public void startPopoutActivity(View v)
    {
        Log.d("MyLog" , "This should popout !");

        Intent addNoteIntent = new Intent(this , AddNoteActivity.class);
        startActivity(addNoteIntent);
    }


    public void changeNoteContent(int dayBlock , int noteIndex)
    {
        String foo = "Day"+Integer.toString(dayBlock)+"_Note"+Integer.toString(noteIndex);

        Log.d( "CalendarLog" , "使用的ID為"+foo);

        int resID = getResources().getIdentifier(foo , "id" , getPackageName());

        TextView someDateText = (TextView) findViewById(resID);
        someDateText.setVisibility(View.VISIBLE);
        someDateText.setText("妹妹吃飯");
    }

    public void hideAllNotes()
    {
        for(int i = 1; i<=42; ++i)
        {
            for(int j = 1; j<=4; ++j)
            {
                String foo = "Day"+Integer.toString(i)+"_Note"+Integer.toString(j);

                int resID = getResources().getIdentifier(foo , "id" , getPackageName());

                TextView someDateText = (TextView) findViewById(resID);

                someDateText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void checkDatabaseNotes()
    {
        //先run過所有日期
        for(int i=1; i<=42; ++i)
        {
            String dateTextViewID = "DateText"+Integer.toString(i);

            int resID = getResources().getIdentifier(dateTextViewID , "id" , getPackageName());

            TextView someDateText = (TextView) findViewById(resID);

            String dateTextViewText = someDateText.getText().toString();

            String month;

            if(i<=7 && Integer.parseInt(dateTextViewText)>=20 )
            {
                month = Integer.toString(monthGot-1);
            }
            else if(i>=25 && Integer.parseInt(dateTextViewText)<15)
            {
                month = Integer.toString(monthGot+1);
            }
            else
            {
                month = Integer.toString(monthGot);
            }



            Log.d( "CalendarLog" , "得到一個日期"+month+"/"+dateTextViewText);
        }
    }
}
