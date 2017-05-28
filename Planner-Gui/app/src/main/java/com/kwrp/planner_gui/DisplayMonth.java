package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import static com.kwrp.planner_gui.R.id.gridview;


public class DisplayMonth extends AppCompatActivity {
    //private Calendar currentDate;
    //private ArrayList<Integer> currentDay = new ArrayList<>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("calender");
    }

    private String[] monthList = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};
    private boolean editAvailable = true;
    private int daysSelected = 0;
    private Collection<String> dayList = new ArrayList<>();
    private Collection<Integer> positionList = new ArrayList<>();
    private String currentDate;
    private static int month = 0;
    private static int year = 0;
    private static int thisYear = 0;
    private static int thisMonthIndex = 0;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);

        currentDate = jniGetCurrentDate();
        toolbar.setSubtitle("Today's Date: " + currentDate);
        setSupportActionBar(toolbar);
        filePath = getFilesDir().getAbsolutePath() + "/events.xml";

        int index = 0;
        for(int i =0;i<currentDate.length();i++){
            if(currentDate.charAt(i) ==('/')){
                if(index==0){
                    index=i;
                } else {
                    thisMonthIndex = (Integer.parseInt(currentDate.substring(index+1,i))-1);
                    thisYear = (Integer.parseInt(currentDate.substring(i+1)));
                }
            }
        }
        this.setTitle(monthList[thisMonthIndex + month] + " - " + thisYear);
        toolbar.setTitle(monthList[thisMonthIndex + month] + " - " + thisYear);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditState();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab_close);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditState();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fabconfirm);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEventSetDialog();
            }
        });


        String[] systemDate = currentDate.split("/");
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1, Integer.parseInt(systemDate[2]), metrics));


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!editAvailable) {
                    TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(position);

                    Drawable background = view2.getBackground();
                    if (background instanceof ColorDrawable) {
                        int color = ((ColorDrawable) background).getColor();
                        if (color == Color.LTGRAY) {
                            view2.setBackgroundColor(Color.rgb(244, 244, 244));

                            Integer pos = new Integer(position);

                            positionList.remove(pos);
                            String day = view2.getText().toString();
                            dayList.remove(day);
                            daysSelected -= 1;
                        } else if (color== Color.rgb(244, 244, 244)){
                            view2.setBackgroundColor(Color.LTGRAY);

                            Integer pos = new Integer(position);
                            positionList.add(pos);
                            String day = view2.getText().toString();
                            dayList.add(day);
                            daysSelected += 1;
                        }
                    }
                    FloatingActionButton fabconfirm = (FloatingActionButton) findViewById(R.id.fabconfirm);
                    if(daysSelected > 0 ) {
                        fabconfirm.setVisibility(View.VISIBLE);
                    } else {
                        fabconfirm.setVisibility(View.GONE);
                    }

                }

            }

        });


        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                if(editAvailable) {
                    TextView view = (TextView) ((GridView) findViewById(gridview)).getChildAt(arg2);
                    String dateSelected = view.getText().toString();
                    Intent myIntent = new Intent(arg1.getContext(), DisplayDay.class); /** Class name here */
                    myIntent.putExtra("date", dateSelected);
                    myIntent.putExtra("month", Integer.toString(month));
                    startActivity(myIntent);
                }
                return true;

            }

        });

        mGridView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if(editAvailable) {
                    toastPrint("drag right");
                    month -= 1;
                    if((thisMonthIndex + month) < 0){
                        month = 11 - thisMonthIndex;
                        thisYear --;
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month] + " - " + thisYear);
                    updateMonthView(month);
                }
            }
            public void onSwipeLeft() {
                if(editAvailable) {
                    toastPrint("drag left");
                    month += 1;
                    if((thisMonthIndex + month) > 11){
                        month = -thisMonthIndex;
                        thisYear++;
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month] + " - " + thisYear);
                    updateMonthView(month);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item1 = menu.findItem(R.id.action_display_week);
        MenuItem item2 = menu.findItem(R.id.action_settings);
        MenuItem item3 = menu.findItem(R.id.action_sync);
        MenuItem item4 = menu.findItem(R.id.action_about);
        MenuItem item5 = menu.findItem(R.id.action_help);

        if(!editAvailable){

            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
            item4.setVisible(false);

            item5.setVisible(true);
        } else {
            item1.setVisible(true);
            item2.setVisible(true);
            item3.setVisible(true);
            item4.setVisible(true);

            item5.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_display_week) {
            Intent myIntent = new Intent(this, DisplayWeek.class); /** Class name here */
            startActivity(myIntent);
            this.finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog dialog = DialogAction.createSettingsDialog(this);
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            AlertDialog dialog = DialogAction.createSyncDialog(this);
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog dialog = DialogAction.createAboutDialog(this);
            dialog.show();
            return true;
        }

        if (id == R.id.action_help) {
            AlertDialog dialog = DialogAction.createHelpDialog(this);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startEditState(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab_close = (FloatingActionButton) findViewById(R.id.fab_close);
        if(editAvailable){

            fab_close.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle("Add new events");
            toolbar.setSubtitle("");
            setSupportActionBar(toolbar);

            editAvailable = false;
        } else {

            fab_close.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle(monthList[thisMonthIndex + month]);
            toolbar.setSubtitle("Today's Date: " + jniGetCurrentDate());
            setSupportActionBar(toolbar);

            FloatingActionButton fabconfirm = (FloatingActionButton) findViewById(R.id.fabconfirm);
            fabconfirm.setVisibility(View.GONE);

            for(int pos : positionList){
                TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(pos);
                view2.setBackgroundColor(Color.rgb(244, 244, 244));
            }
            dayList = new ArrayList<>();
            daysSelected = 0;
            editAvailable = true;
        }
    }

    public void updateMonthView(int month) {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
//        toolbar.setSubtitle("Today's Date: " + currentDate);
        String[] systemDate = currentDate.split("/");
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1 + month, thisYear, metrics));
    }

    public void createEventSetDialog(){
        DialogAction a = new DialogAction();
        String eventMonth =  ""+(thisMonthIndex+month+1);
        String eventYear = ""+thisYear;

        AlertDialog dialog = a.createEventSetDialog(this, dayList, eventMonth, eventYear, filePath );
        dialog.show();
        startEditState();
    }

    public void toastPrint(String s){
        Log.d("     SWIPE     :", s);
//        Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String jniGetCurrentDate();

    //public native String jniCreateXml(String filepath);
}
