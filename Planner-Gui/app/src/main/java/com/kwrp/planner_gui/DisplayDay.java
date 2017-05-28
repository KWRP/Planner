package com.kwrp.planner_gui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class DisplayDay extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("calender");
    }

    private ArrayList<String> eventItems = new ArrayList<>();
    private String newEventTitle = "";
    private String newEventDescription = "";
    private String newEventStart = "";
    private String newEventDuration = "";
    private String filePath;
    private Day selectedDay;
    private ArrayAdapter<String> listAdapter;
    private String currentDate = jniGetCurrentDate();
    private String selectedDate;
    private String selectDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_day);
        toolbar.setSubtitle("Today's Date: " + currentDate);
        setSupportActionBar(toolbar);

        Intent myIntent = getIntent();
        currentDate = modifyDate(myIntent);
        selectedDate = selectDay + currentDate.substring(currentDate.indexOf("/"));


        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventItems);
        ListView events = (ListView) findViewById(R.id.list_view_events);
        events.setAdapter(listAdapter);

        EditText dateField = (EditText) findViewById(R.id.output_selected_date);

        dateField.setText(selectedDate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = createEventDialog();
                dialog.show();
            }
        });
        /*File eventsXml = new File(getFilesDir().getAbsolutePath() +"/events.xml");
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
        Log.d("---JAVA TESTING!!!---", eventsXml.getAbsolutePath());*/

        File dir = getFilesDir();
        File file = new File(dir, "events.xml");
        file.delete();

        if (!file.exists()) {
            filePath = getFilesDir().getAbsolutePath() + "/events.xml";
            String newFile = jniCreateXml(filePath);
            Log.d("JAVA TEST!!!: ", newFile);
        }
        getEvents();
    }

    protected void getEvents() {
        filePath = getFilesDir().getAbsolutePath() + "/events.xml";
        String getDay = jniGetDay(filePath, selectedDate);
        if (getDay == null || getDay.isEmpty()) {
            eventItems.clear();
            eventItems.add("You have no saved events on this day :)");
       } else {
            eventItems.clear();
            Day selectedDay = new Day(getDay);
            for (Event event : selectedDay.getEvents()) {
                eventItems.add(event.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_display_month) {
            Intent myIntent = new Intent(this, DisplayMonth.class); /** Class name here */
            startActivity(myIntent);
            this.finish();
        }
        if (id == R.id.action_display_week) {
            Intent myIntent = new Intent(this, DisplayWeek.class); /** Class name here */
            startActivity(myIntent);
            //startActivityForResult(myIntent, 0);
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

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog createEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDay.this);
        builder.setTitle("New Event");

        final LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        final TextView titleLabel = new TextView(this);
        titleLabel.setText("Event Title:");
        dialogLayout.addView(titleLabel, 0);
        final EditText titleInput = new EditText(this);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogLayout.addView(titleInput, 1);

        final TextView descriptLabel = new TextView(this);
        descriptLabel.setText("Event Description:");
        dialogLayout.addView(descriptLabel, 2);
        final EditText descriptInput = new EditText(this);
        descriptInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogLayout.addView(descriptInput, 3);

        final TextView startLabel = new TextView(this);
        startLabel.setText("Start Time:");
        dialogLayout.addView(startLabel, 4);
        final EditText startInput = new EditText(this);
        startInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        startInput.setHint("e.g. 0800");
        dialogLayout.addView(startInput, 5);

        final TextView durationLabel = new TextView(this);
        durationLabel.setText("Duration");
        dialogLayout.addView(durationLabel, 6);
        final EditText durationInput = new EditText(this);
        durationInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        durationInput.setHint("Number of hours");
        dialogLayout.addView(durationInput, 7);

        builder.setView(dialogLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                newEventTitle = ((EditText) dialogLayout.getChildAt(1)).getText().toString();
                newEventDescription = ((EditText) dialogLayout.getChildAt(3)).getText().toString();
                newEventStart = ((EditText) dialogLayout.getChildAt(5)).getText().toString();
                newEventDuration = ((EditText) dialogLayout.getChildAt(7)).getText().toString();

                if (newEventTitle.equals("") || newEventDescription.equals("") ||
                        newEventStart.equals("") || newEventDuration.equals("")) {
                    Log.d("JAVA create event: ", "failed!!");
                } else {
                    createNewEvent(newEventTitle, newEventDescription, newEventStart, newEventDuration);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void createNewEvent(String title, String description, String start, String duration) {
        Log.d("Java Test createEvent: ", "Before");
        String newEvent = jniCreateEvent(title, description, start, duration, filePath, selectedDate);
        Log.d("Java Test cretEv after:", newEvent);
        getEvents();
        listAdapter.notifyDataSetChanged();
    }

    private String modifyDate(Intent context) {
        selectDay = context.getStringExtra("date");
        if (selectDay.length() == 1) {
            selectDay = "0" + selectDay;
        }

        String month = context.getStringExtra("month");
        int monthValue = Integer.parseInt(month);

        String[] splitDate = currentDate.split("/");
        Integer m = Integer.parseInt(splitDate[1]);
        m += monthValue;

        splitDate[1] = m.toString();
        if (splitDate[1].length() == 1) {
            splitDate[1] = "0" + splitDate[1];
        }

        return (splitDate[0] + "/" + splitDate[1] + "/" + splitDate[2]);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String jniGetEvents();

    public native String jniGetDay(String dir, String currentDate);
    public native String jniGetCurrentDate();
    public native String jniCreateXml(String filePath);
    public native String jniCreateEvent(String title, String description, String start,
                                        String duration, String dir, String selectedDate);
}
