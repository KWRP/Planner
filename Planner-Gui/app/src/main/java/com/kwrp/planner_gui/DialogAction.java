package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import static com.kwrp.planner_gui.R.id.gridview;

/**
 * Defines a series of actions for specific actions such as "sync" button select.
 * Returns dialog boxes for specific actions and in some cases sends data to back-end.
 *
 * @author KWRP
 */
public class DialogAction extends AppCompatActivity {

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
    private Event selectedEvent = null;

    /**
     * selected event Id (for removal)
     */
    private static String eventId = "";
    private static Button startTime;
    private static Button endTime;
    private static Button finishDate;
    private static Button eventDays;
    private static final String[] repeats = {"Never", "Daily", "Weekly", "Monthly", "Yearly"};
    private static int selectedRepeat = 0;

    /*Loads the native library "calender" on start up */
    static {
        System.loadLibrary("calendar");
    }

    public static int headColor = Color.BLUE;
    public static int textColor = Color.WHITE;
    public static int eventColor = Color.argb(100, 10, 80, 255);
    public static int defaultColor = Color.rgb(244, 244, 244);
    public static int outMonthColor = Color.rgb(234, 234, 250);
    public static int selectedColor = Color.LTGRAY;


    /*Defines the filepath in the user device where the events.xml is stored.*/
    private String filePath;

    /*Default constructor*/
    public DialogAction() {
    }

    /**
     * Creates the "About" Dialog box which occurs when the user selects "About" in the options menu
     *
     * @param parent The Intent the dialog box is spawned onto
     * @return the dialog box object, to "shown".
     */
    public static AlertDialog createAboutDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("This application is designed for Otago University students. This" +
                        " application was created by students." +
                        "\n\n\t\t\t\t\t How to use:"+
                        "\nSwipe side to side to change month."+
                        "\nPress and hold a day to edit single day."+
                        "\nTap the pencil icon to enable multi-day \n\tevent creation.");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * Creates the "Settings" Dialog which occurs when the user selects "Settings" in the options menu
     *
     * @param parent The Intent the dialog box is spawned onto
     * @return the dialog box object, to "shown".
     */
    public static AlertDialog createSettingsDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        final AppCompatActivity parentf = parent;
        builder.setTitle("Settings");

        final LinearLayout dialogLayout = new LinearLayout(parent);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);


        TextView title = new TextView(parent);
        title.setText("Select a colour scheme:");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(0, 0, 0));
        title.setTextSize(14);
        dialogLayout.addView(title, 0);

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select...");
        spinnerArray.add("Green");
        spinnerArray.add("Red");
        spinnerArray.add("Blue");

        Spinner spinner = new Spinner(parent);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toolbar toolbar = (Toolbar) parentf.findViewById(R.id.toolbar_month);
                if (toolbar == null){
                    toolbar = (Toolbar) parentf.findViewById(R.id.toolbar_day);
                }
                switch (position) {
                    case 1:
                        headColor = Color.GREEN;
                        break;
                    case 2:
                        headColor = Color.RED;
                        break;
                    case 3:
                        headColor = Color.BLUE;
                        break;
                }
                toolbar.setBackgroundColor(headColor);


            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

    });
        spinner.setPadding(100, 20, 100, 20);

        spinner.setDropDownHorizontalOffset(90);
        spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //spinner.setBackgroundColor(parseColor("#FAFAFA"));
        dialogLayout.addView(spinner);
        dialogLayout.setPadding(100, 50, 100, 50);

        builder.setView(dialogLayout);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * Creates the "Sync" Dialog which occurs when the user selects "Sync" in the options menu
     *
     * @param parent The Intent the dialog box is spawned onto
     * @return the dialog box object, to "shown".
     */
    public static AlertDialog createSyncDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Synchronise");

        final LinearLayout dialogLayout = new LinearLayout(parent);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);


        TextView evisionTitle = new TextView(parent);
//        evisionTitle.setText("Evision:");
//        evisionTitle.setPadding(10, 10, 10, 10);
//        evisionTitle.setGravity(Gravity.CENTER);
//        evisionTitle.setTextColor(Color.rgb(0, 0, 0));
//        evisionTitle.setTextSize(14);

        Button button = new Button(parent);
        button.setText("Otago Timetable");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(evisionTitle, 0);
        dialogLayout.addView(button, 1);

        TextView googleTitle = new TextView(parent);
//        googleTitle.setText("Google Calendar:");
//        googleTitle.setPadding(10, 10, 10, 10);
//        googleTitle.setGravity(Gravity.CENTER);
//        googleTitle.setTextColor(Color.rgb(0, 0, 0));
//        googleTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Google Calendar");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(googleTitle, 2);
        dialogLayout.addView(button, 3);

        dialogLayout.setPadding(70, 10, 70, 10);
        builder.setView(dialogLayout);

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * Creates the "Help" Dialog which occurs when the user selects "Help" in the options menu
     *
     * @param parent The Intent the dialog box is spawned onto
     * @return the dialog box object, to "shown".
     */
    public static AlertDialog createHelpDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("Help")
                .setMessage("This option is to add one event to multiple days." +
                        "\nTo add an event for a single day, simply tap the selected day." +
                        " and click the the green tick button");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }




    /**
     * Sends JNI the event details and a date, so that the back-end can create the event and store it
     *
     * @param title       The event title
     * @param description The event description
     * @param start       The time the event starts
     * @param duration    How long (in hours) the event will go for
     * @param dates       A collection containing the dates (DD/MM/YYYY) the event occurs on
     */
    private void createNewEvent(String title, String description, String start, String duration, ArrayList<String> dates) {
        String s;
        for (String selectedDate : dates) {
            s = modifyDate(selectedDate);
            String createEvent = jniCreateEvent(title, description, start, duration, filePath, s);
            Log.d("CreateEvent: %s %s %s", createEvent);
        }
    }

    /**
     * Helper function that makes sure the format of the date strings
     * is valid DD/MM/YYYY format so as to ensure consistency. Will correct errors
     * such as D/M/YYYY or DD/M/YYYY.
     *
     * @param date a string in a given format DD/MM/YYYY OR D/MM/YYYY OR DD/M/YYYY OR D/M/YYYY
     * @return A properly formed date DD/MM/YYYY
     */
    private String modifyDate(String date) {

        String[] splitDate = date.split("/");

        //prefixing zero fix (day)
        if (splitDate[0].length() == 1) {
            splitDate[0] = "0" + splitDate[0];
        }

        //prefixing zero fix (month)
        if (splitDate[1].length() == 1) {
            splitDate[1] = "0" + splitDate[1];
        }

        return (splitDate[0] + "/" + splitDate[1] + "/" + splitDate[2]);
    }

    private void createNewEvent(String title, String description, String start, String finish,
                                String finishDate, String repeat) {

        //if (this.repeat == 3) repeat += repeatDayDate;
        Log.e("Finish Date: ", finishDate);
        Log.e("start Date: ", selectedDate);
        String r = jniCreateDbEvent(title, description, start, finish, selectedDate, finishDate, repeat, filepath);
        Log.e("CREATE Event:", r);
        eventId = "";
        newEvTitle = "";
        newEvDescription = "";
        newEvStartTime = "";
        newEvFinishTime = "";
        selectedDay = null;
        newEvEndDate = "";
        //getEvents();
        listAdapter.notifyDataSetChanged();
    }

    public AlertDialog createEventDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("New Event");

        final LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 50, 50, 50);
        final TextView titleLabel = new TextView(context);
        titleLabel.setText("Event Title:");
        dialogLayout.addView(titleLabel, 0);
        final EditText titleInput = new EditText(context);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setText(newEvTitle);
        dialogLayout.addView(titleInput, 1);

        final TextView descriptLabel = new TextView(context);
        descriptLabel.setText("Event Description:");
        dialogLayout.addView(descriptLabel, 2);
        final EditText descriptInput = new EditText(context);
        descriptInput.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptInput.setText(newEvDescription);
        dialogLayout.addView(descriptInput, 3);

        final TextView startLabel = new TextView(context);
        startLabel.setText("Start Time:");
        dialogLayout.addView(startLabel, 4);
        startTime = new Button(context);
        startTime.setHint("Select time");
        startTime.setOnClickListener(startOnClick);
        dialogLayout.addView(startTime, 5);

        final TextView endLabel = new TextView(context);
        endLabel.setText("End Time");
        dialogLayout.addView(endLabel, 6);
        endTime = new Button(context);
        endTime.setHint("Select time");
        endTime.setOnClickListener(endOnClick);
        dialogLayout.addView(endTime, 7);

        final TextView finishLabel = new TextView(context);
        finishLabel.setText("Finish Date");
        dialogLayout.addView(finishLabel, 8);
        finishDate = new Button(context);
        finishDate.setText(selectedDate);
        finishDate.setOnClickListener(endDateOnClick);
        dialogLayout.addView(finishDate, 9);

        final TextView eventDaysLabel = new TextView(context);
        eventDaysLabel.setText("Select event to repeat on");
        dialogLayout.addView(eventDaysLabel, 10);
        eventDays = new Button(context);
        eventDays.setText(repeats[0]);
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

    private View.OnClickListener eventDaysOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            // add a checkbox list
            DialogFragment newFragment = new SelectRepeatFrag();
            newFragment.show(getFragmentManager(), "Select repeat repeats");
        }
    };

    public static class SelectRepeatFrag extends DialogFragment {
        private int newSelection = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Repeat event on").setSingleChoiceItems(repeats, selectedRepeat,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //repeatDayDate = 0;
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
            String d;
            String m;
            if (day < 10) {
                d = "0" + day;
            } else {
                d = "" + day;
            }
            int mon = month + 1;
            if (mon < 10) {
                m = "0" + mon;
            } else {
                m = "" + mon;
            }
            finishDate.setText(d + "/" + m + "/" + year);
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
            startTime.setText(String.format(Locale.ENGLISH, "%02d:%02d %s", hour == 0 ? 12 : hour,
                    minute, hourOfDay < 12 ? "am" : "pm"));
            startTime.setTextSize(20);
            int h = (hourOfDay + 1) % 12;
            endTime.setText(String.format(Locale.ENGLISH, "%02d:%02d %s", h == 0 ? 12 : h,
                    minute, (hourOfDay + 1) < 12 ? "am" : "pm"));
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

    public native String jniCreateDbEvent(String title, String description, String start,
                                          String finish, String selectedDate,
                                          String finishDate, String repeat, String filepath);

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
     * @return
     */
    public native String jniCreateEvent(String title, String description, String start,
                                        String duration, String dir, String selectedDate);


    public native String jniGetCurrentDate();

}
