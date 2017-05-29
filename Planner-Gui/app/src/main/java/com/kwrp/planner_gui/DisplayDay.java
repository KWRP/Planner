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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Defines the display day activity where the user is shown
 * the events for a single day, and what the ability to add
 * an event for a specific day.
 *
 * @author KWRP
 */
public class DisplayDay extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    /**
     * Loads the native library "calender" on start up
     */
    static {
        System.loadLibrary("calendar");
    }

    /**
     * List of eventItems
     */
    private ArrayList<String> eventItems = new ArrayList<>();

    /**
     * The new event title
     */
    private String newEventTitle = "";

    /**
     * The new event description
     */
    private String newEventDescription = "";

    /**
     * The new event start time
     */
    private String newEventStart = "";

    /**
     * The new event duration
     */
    private String newEventDuration = "";

    /**
     * The file path to the file where events are being stored
     */
    private String filePath;

    /**
     * The listview for events
     */
    private ArrayAdapter<String> listAdapter;

    /**
     * The current date
     */
    private String currentDate = jniGetCurrentDate();

    /**
     * The selected date
     */
    private String selectedDate;

    /**
     * Text from the month box
     */
    private String selectDay;

    /**
     * The day object
     */
    private Day selectedDay = null;

    /**
     * selected event Id (for removal)
     */
    private String eventId = "";


    /**
     * Called when the activity is first created. Sets up buttons, labels, and initialises variables.
     *
     * @param savedInstanceState defines the action such as screen rotation
     */
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
        ListView eventsView = (ListView) findViewById(R.id.list_view_events);
        eventsView.setAdapter(listAdapter);

        TextView dateField = (EditText) findViewById(R.id.output_selected_date);

        dateField.setText(selectedDate);

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                if (selectedDay != null && selectedDay.getEvent(position) != null) {
                    eventId = selectedDay.getEvent(position).getEventId();
                    Dialog dialog = removeEventDialog();
                    dialog.show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = createEventDialog();
                dialog.show();
            }
        });

        checkXmlExists();
        getEvents();
    }

    /**
     * Gets the events from the .xml file and adds them to the
     * eventItems list.
     */
    protected void getEvents() {
        filePath = getFilesDir().getAbsolutePath() + "/events.xml";
        String getDay = jniGetDay(filePath, selectedDate);
        if (getDay == null || getDay.isEmpty()) {
            eventItems.clear();
            eventItems.add("You have no saved events on this day :)");
        } else {
            eventItems.clear();
            selectedDay = new Day(getDay);
            for (Event event : selectedDay.getEvents()) {
                eventItems.add(event.toString());
            }
        }
    }

    /**
     * Creates a new event accordingly
     *
     * @param title       title of the new event
     * @param description description of then new event
     * @param start       start time of the new event
     * @param duration    duration of the new event
     */
    private void createNewEvent(String title, String description, String start, String duration) {

        String newEvent = jniCreateEvent(title, description, start, duration, filePath, selectedDate);
        eventId = "";
        newEventTitle = "";
        newEventDescription = "";
        newEventStart = "";
        newEventDuration = "";
        selectedDay = null;
        getEvents();
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Modifies the date to fit format DD/MM/YYYY
     *
     * @param context the intent
     * @return a string in format DD/MM/YYYY
     */
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

        return (splitDate[0] + "/" + splitDate[1] + "/" +
                Integer.parseInt(context.getStringExtra("year")));
    }

    /**
     * Checks if the XML file where events are stored exists
     * and create a file if not.
     */
    private void checkXmlExists() {
        File dir = getFilesDir();
        File file = new File(dir, "events.xml");
        //file.delete();

        if (!file.exists()) {
            filePath = getFilesDir().getAbsolutePath() + "/events.xml";
            String newFile = jniCreateXml(filePath);
        }
    }

    /**
     * Called when the options menu on the toolbar is created or updated.
     *
     * @param menu The menu on the toolbar
     * @return true boolean type
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
        return true;
    }

    /**
     * Called when an item in the menu has been selected. Will find which action it is
     * and then spawn a dialog box (or change view) accordingly.
     *
     * @param item The menu item that was selected
     * @return boolean type
     */
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

    /**
     * Creates the CREATE EVENT dialog box that pops up when the user wants to
     * add an event.
     *
     * @return the dialog.
     */
    private AlertDialog createEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDay.this);
        builder.setTitle("New Event");

        final LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 50, 50, 50);
        final TextView titleLabel = new TextView(this);
        titleLabel.setText("Event Title:");
        dialogLayout.addView(titleLabel, 0);
        final EditText titleInput = new EditText(this);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setText(newEventTitle);
        dialogLayout.addView(titleInput, 1);

        final TextView descriptLabel = new TextView(this);
        descriptLabel.setText("Event Description:");
        dialogLayout.addView(descriptLabel, 2);
        final EditText descriptInput = new EditText(this);
        descriptInput.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptInput.setText(newEventDescription);
        dialogLayout.addView(descriptInput, 3);

        final TextView startLabel = new TextView(this);
        startLabel.setText("Start Time:");
        dialogLayout.addView(startLabel, 4);
        final EditText startInput = new EditText(this);
        startInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        startInput.setText(newEventStart);
        startInput.setHint("e.g. 0800");
        dialogLayout.addView(startInput, 5);

        final TextView durationLabel = new TextView(this);
        durationLabel.setText("Duration");
        dialogLayout.addView(durationLabel, 6);
        final EditText durationInput = new EditText(this);
        durationInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        durationInput.setText(newEventDuration);
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

        return builder.create();
    }

    /**
     * Creates the DELETE EVENT dialog box that pops up when the user wants to
     * add an event. (It gives you the choice of deletion)
     *
     * @return the dialog.
     */
    private AlertDialog removeEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDay.this);
        builder.setTitle("Remove Event")
                .setMessage("Do you wish to delete this event?")
                .setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert, null));

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String deleteEvent = jniRemoveEvent(filePath, selectedDate, eventId);
                selectedDay = null;
                getEvents();
                listAdapter.notifyDataSetChanged();
                Log.d("Delete Event: %s", deleteEvent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String jniGetCurrentDate();

    /**
     * Gets the events on a given day
     *
     * @param filePath    path/to/file
     * @param currentDate the day to search
     * @return the events
     */
    public native String jniGetDay(String filePath, String currentDate);

    /**
     * Creates an xml file for the device if it doesn't exist
     *
     * @param filePath the path/to/file
     * @return the path/to/file
     */
    public native String jniCreateXml(String filePath);

    /**
     * A JNI function that pushes event details through to the back-end to create
     * and store events. This is defined outside this class.
     *
     * @param title        The event title
     * @param description  The event description
     * @param start        The time the event starts
     * @param duration     How long (in hours) the event will go for
     * @param dir          The file path to a .xml file where the events are stored
     * @param selectedDate A collection containing the dates (DD/MM/YYYY) the event occurs on
     * @return string confirming action
     */
    public native String jniCreateEvent(String title, String description, String start,
                                        String duration, String dir, String selectedDate);

    /**
     * A JNI function that removes an event from the .xml file rendering it
     * non-existent.
     *
     * @param filePath     path/to/file
     * @param selectedDate the date to remove the event from
     * @param eventId      the event id number
     * @return string confirming action
     */
    public native String jniRemoveEvent(String filePath, String selectedDate, String eventId);

}

