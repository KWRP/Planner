package com.kwrp.planner_gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.kwrp.planner_gui.R.id.gridview;


public class DisplayMonth extends AppCompatActivity {
    //private Calendar currentDate;
    //private ArrayList<Integer> currentDay = new ArrayList<>();


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("calender");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
        toolbar.setSubtitle("Today's Date: " + jniGetCurrentDate());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String[] systemDate = jniGetCurrentDate().split("/");
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1])-1, Integer.parseInt(systemDate[2]), metrics));

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                TextView view = (TextView) ((GridView) findViewById(gridview)).getChildAt(arg2);
                String dateSelected = view.getText().toString();
                Intent myIntent = new Intent(arg1.getContext(), DisplayDay.class); /** Class name here */
                myIntent.putExtra("date", dateSelected);
                startActivity(myIntent);
                Log.d("TESTING!!!", getFilesDir().getAbsolutePath());

/*
                File eventsXml = new File(getFilesDir().getAbsolutePath() +"/events.xml");
                if (!eventsXml.exists()) {
                    try {
                        eventsXml.createNewFile();
                        eventsXml.mkdir();
                    }catch(IOException e) {
                        e.printStackTrace();
                        Log.d("---JAVA TESTING!!!---", "File not created!");
                    }
                    if (eventsXml.exists()) Log.d("---JAVA TESTING!!!---", "File created!");
                } else {
                    Log.d("---JAVA TESTING!!!---", "File already exists!");
                }
                Log.d("---JAVA TESTING!!!---", eventsXml.getAbsolutePath());
*/
                File eventsXml = new File(getFilesDir().getAbsolutePath() +"/events.xml");
                if (!eventsXml.exists()) Log.d("---JAVA TESTING!!!---", eventsXml.getAbsolutePath());
                //Integer v = ((GridView) findViewById(gridview)).getChildCount();
                //TextView view = (TextView)((GridView) findViewById(gridview)).getChildAt(arg2);
                //view.setBackgroundColor(Color.rgb(255, 155, 155));
                finish();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if(id== R.id.action_exit){
            AlertDialog dialog = DialogAction.exitButtonAction(this);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

        /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String jniGetCurrentDate();
    public native String testJNI();
}
