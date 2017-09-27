package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

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


        TextView hotmailTitle = new TextView(parent);
//        hotmailTitle.setText("Hotmail Calendar:");
//        hotmailTitle.setPadding(10, 10, 10, 10);
//        hotmailTitle.setGravity(Gravity.CENTER);
//        hotmailTitle.setTextColor(Color.rgb(0, 0, 0));
//        hotmailTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Hotmail Calendar");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(hotmailTitle, 4);
        dialogLayout.addView(button, 5);


        TextView facebookTitle = new TextView(parent);
//        facebookTitle.setText("Facebook Events:");
//        facebookTitle.setPadding(10, 10, 10, 10);
//        facebookTitle.setGravity(Gravity.CENTER);
//        facebookTitle.setTextColor(Color.rgb(0, 0, 0));
//        facebookTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Facebook Events");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        dialogLayout.addView(facebookTitle, 6);
        dialogLayout.addView(button, 7);

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
     * Creates the dialog box for creating an event when there are a series of dates the event will take place on.
     * <p>
     * Creates the "Settings" Dialog which occurs when the user selects "Settings" in the options menu
     *
     * @param parent   The Intent the dialog box is spawned onto
     * @param dayList  A collection containing the days that the event occurs on
     * @param month    The month in which this event takes place
     * @param year     The year in which this event takes place
     * @param filePath The filepath for the XML file storing all the events
     * @return the dialog box object, to "shown".
     */
    public AlertDialog createEventSetDialog(AppCompatActivity parent, Collection<String> dayList, String month, String year, String filePath) {

        this.filePath = filePath;
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Create Event Set");

        final LinearLayout dialogLayout = new LinearLayout(parent);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 50, 50, 50);

        final TextView titleLabel = new TextView(parent);
        titleLabel.setText("Event Title:");
        dialogLayout.addView(titleLabel, 0);
        final EditText titleInput = new EditText(parent);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogLayout.addView(titleInput, 1);

        final TextView descriptLabel = new TextView(parent);
        descriptLabel.setText("Event Description:");
        dialogLayout.addView(descriptLabel, 2);
        final EditText descriptInput = new EditText(parent);
        descriptInput.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogLayout.addView(descriptInput, 3);

        final TextView startLabel = new TextView(parent);
        startLabel.setText("Start Time:");
        dialogLayout.addView(startLabel, 4);
        final EditText startInput = new EditText(parent);
        startInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        startInput.setHint("e.g. 0800");
        dialogLayout.addView(startInput, 5);

        final TextView durationLabel = new TextView(parent);
        durationLabel.setText("Duration");
        dialogLayout.addView(durationLabel, 6);
        final EditText durationInput = new EditText(parent);
        durationInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        durationInput.setHint("Number of hours");
        dialogLayout.addView(durationInput, 7);

        builder.setView(dialogLayout);

        final ArrayList<String> dateList = new ArrayList<>();
        for (String day : dayList) {

            dateList.add(day + "/" + month + "/" + year);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEventTitle = ((EditText) dialogLayout.getChildAt(1)).getText().toString();
                String newEventDescription = ((EditText) dialogLayout.getChildAt(3)).getText().toString();
                String newEventStart = ((EditText) dialogLayout.getChildAt(5)).getText().toString();
                String newEventDuration = ((EditText) dialogLayout.getChildAt(7)).getText().toString();

                if (newEventTitle.equals("") || newEventDescription.equals("") ||
                        newEventStart.equals("") || newEventDuration.equals("")) {
                    Log.d("JAVA create event: ", "failed!!");
                } else {

                    createNewEvent(newEventTitle, newEventDescription, newEventStart, newEventDuration, dateList);
                }
                Log.d("details:", newEventTitle + newEventDescription + newEventStart + newEventDuration);
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
