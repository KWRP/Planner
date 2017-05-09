package com.kwrp.planner_gui;

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
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog dialog = createSettingsDialog();
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            AlertDialog dialog = createSyncDialog();
            dialog.show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog dialog = createAboutDialog();
            dialog.show();
            return true;
        }

        if(id== R.id.action_exit){
            AlertDialog dialog = exitButtonAction();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog createAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMonth.this);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("What the fuck did you just fucking type about me, you little bitch? I’ll have you know I graduated top of my class at MIT, and I’ve been involved in numerous secret raids with Anonymous, and I have over 300 confirmed DDoSes. I am trained in online trolling and I’m the top hacker in the entire world. You are nothing to me but just another virus host. I will wipe you the fuck out with precision the likes of which has never been seen before on the Internet, mark my fucking words. You think you can get away with typing that shit to me over the Internet? Think again, fucker. As we chat over IRC I am tracing your IP with my damn bare hands so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your computer. You’re fucking dead, kid. I can be anywhere, anytime, and I can hack into your files in over seven hundred ways, and that’s just with my bare hands. Not only am I extensively trained in hacking, but I have access to the entire arsenal of every piece of malware ever created and I will use it to its full extent to wipe your miserable ass off the face of the world wide web, you little shit. If only you could have known what unholy retribution your little “clever” comment was about to bring down upon you, maybe you would have held your fucking fingers. But you couldn’t, you didn’t, and now you’re paying the price, you goddamn idiot. I will shit code all over you and you will drown in it. You’re fucking dead, kiddo.");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMonth.this);
        builder.setMessage("")
                .setTitle("Settings")
                .setMessage("Change Colour scheme what else?");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createSyncDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMonth.this);
        builder.setMessage("")
                .setTitle("Synchronise")
                .setMessage("Synchronise with:\n-Evision\nGoogle Calendar\n-Hotmail Calendar\n-Facebook Calendar");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog exitButtonAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMonth.this);
        builder.setMessage("")
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String jniGetCurrentDate();
    public native String testJNI();
}
