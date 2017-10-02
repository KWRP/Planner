package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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



    /*Loads the native library "calender" on start up */
    static {
        System.loadLibrary("calendar");
    }

    public static int headColor = Color.BLUE;
    public static int textColor = Color.WHITE;


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



}
