package com.kwrp.planner_gui;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private String newEvTitle = "";

    /**
     * The new event description
     */
    private String newEvDescription = "";

    /**
     * The new event start time
     */
    private String newEvStartTime = "";

    /**
     * The new event duration
     */
    private String newEvFinishTime = "";

    private String newEvEndDate = "";
    private int repeat = 0;

    /**
     * The file path to the file where events are being stored
     */
    private String filepath;

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
    private static Button startTime;
    private static Button endTime;
    private static Button finishDate;
    private static Button eventDays;
    private static final String[] repeats = {"Never", "Daily", "Weekly", "Monthly", "Yearly"};
    private static int selectedRepeat = 0;


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
        toolbar.setBackgroundColor(DialogAction.headColor);

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
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);
            }
        });

        checkDbExists();
        getEvents();
    }

    /**
     * Gets the events from the .xml file and adds them to the
     * eventItems list.
     */
    protected void getEvents() {
        filepath = getFilesDir().getAbsolutePath() + "/events.xml";
        String getDay = jniGetDay(filepath, selectedDate);
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
     * @param finish    duration of the new event
     */
    private void createNewEvent(String title, String description, String start, String finish,
                                String finishDate, String repeat) {

        jniCreateDbEvent(title, description, start, finish, selectedDate, finishDate, repeat, filepath);
        eventId = "";
        newEvTitle = "";
        newEvDescription = "";
        newEvStartTime = "";
        newEvFinishTime = "";
        selectedDay = null;
        newEvEndDate = "";

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
    private void checkDbExists() {
        File dir = getFilesDir();
        File file = new File(dir, "/events.db");
        //file.delete();

        if (!file.exists()) {
            filepath = getFilesDir().getAbsolutePath() + "/events.db";
            String newFile = jniCreateDb(filepath);
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
        titleInput.setText(newEvTitle);
        dialogLayout.addView(titleInput, 1);

        final TextView descriptLabel = new TextView(this);
        descriptLabel.setText("Event Description:");
        dialogLayout.addView(descriptLabel, 2);
        final EditText descriptInput = new EditText(this);
        descriptInput.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptInput.setText(newEvDescription);
        dialogLayout.addView(descriptInput, 3);

        final TextView startLabel = new TextView(this);
        startLabel.setText("Start Time:");
        dialogLayout.addView(startLabel, 4);
        startTime = new Button(this);
        startTime.setHint("Select time");
        startTime.setOnClickListener(startOnClick);
        dialogLayout.addView(startTime, 5);

        final TextView endLabel = new TextView(this);
        endLabel.setText("End Time");
        dialogLayout.addView(endLabel, 6);
        endTime = new Button(this);
        endTime.setHint("Select time");
        endTime.setOnClickListener(endOnClick);
        dialogLayout.addView(endTime, 7);

        final TextView finishLabel = new TextView(this);
        finishLabel.setText("Finish Date");
        dialogLayout.addView(finishLabel, 8);
        finishDate = new Button(this);
        finishDate.setHint("Select date");
        finishDate.setOnClickListener(endDateOnClick);
        dialogLayout.addView(finishDate, 9);

        final TextView eventDaysLabel = new TextView(this);
        eventDaysLabel.setText("Select event to repeat on");
        dialogLayout.addView(eventDaysLabel, 10);
        eventDays = new Button(this);
        eventDays.setHint("Select repeats to repeat");
        eventDays.setOnClickListener(eventDaysOnClick);
        dialogLayout.addView(eventDays, 11);

        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                newEvTitle = ((EditText) dialogLayout.getChildAt(1)).getText().toString();
                newEvDescription = ((EditText) dialogLayout.getChildAt(3)).getText().toString();
                newEvStartTime = startTime.getText().toString();
                newEvFinishTime = endTime.getText().toString();
                newEvEndDate = finishDate.getText().toString();
                String repeat = Integer.toString(selectedRepeat);
                if (newEvTitle.equals("") || newEvDescription.equals("") ||
                        newEvStartTime.equals("") || newEvFinishTime.equals("")) {
                    Log.d("JAVA create event: ", "failed!!");
                } else {
                    createNewEvent(newEvTitle, newEvDescription, newEvStartTime, newEvFinishTime,
                            newEvEndDate, repeat);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventDays.setText("");
                startTime.setText("");
                endTime.setText("");
                finishDate.setText("");
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
                String deleteEvent = jniRemoveEvent(filepath, selectedDate, eventId);
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

    // Code For times and dates, needs refactoring


    private View.OnClickListener eventDaysOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            // add a checkbox list
            DialogFragment newFragment = new SelectDaysFrag();
            newFragment.show(getFragmentManager(), "Select repeat repeats");
        }
    };

    public static class SelectDaysFrag extends DialogFragment {
        private int newSelection = 0;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Repeat event on").setSingleChoiceItems(repeats, selectedRepeat,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eventDays.setText(repeats[which]);
                    newSelection = which;
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedRepeat = newSelection;
                }
            });
            builder.setNegativeButton("Cancel", null);
            return builder.create();
        }
    }

    // Code For times and dates, needs refactoring
    private View.OnClickListener endDateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            DialogFragment newFragment = new DatePickerFrag();
            newFragment.show(getFragmentManager(), "Pick Date");
        }
    };

    public static class DatePickerFrag extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            finishDate.setText(day + "/" + month + "/" + year);
            finishDate.setTextSize(20);
        }
    }

    private View.OnClickListener startOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            DialogFragment newFragment = new StartTimeFrag();
            newFragment.show(getFragmentManager(), "Pick Time");
        }
    };

    public static class StartTimeFrag extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);


            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            int hour = hourOfDay % 12;
            startTime.setText(String.format(Locale.ENGLISH,"%02d:%02d %s", hour == 0 ? 12 : hour,
                    minute, hourOfDay < 12 ? "am" : "pm"));
            startTime.setTextSize(20);
        }
    }

    private View.OnClickListener endOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            DialogFragment newFragment = new EndTimeFrag();
            newFragment.show(getFragmentManager(), "Pick Time");
        }
    };

    public static class EndTimeFrag extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);


            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            int hour = hourOfDay % 12;
            endTime.setText(String.format(Locale.ENGLISH, "%02d:%02d %s", hour == 0 ? 12 : hour,
                    minute, hourOfDay < 12 ? "am" : "pm"));
            endTime.setTextSize(20);
        }
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
     * A JNI function that removes an event from the .xml file rendering it
     * non-existent.
     *
     * @param filePath     path/to/file
     * @param selectedDate the date to remove the event from
     * @param eventId      the event id number
     * @return string confirming action
     */
    public native String jniRemoveEvent(String filePath, String selectedDate, String eventId);

    public native String jniCreateDb(String filePath);

    public native String jniCreateDbEvent(String title, String description, String start,
                                          String finish, String selectedDate,
                                          String finishDate, String repeat, String filepath);

}

