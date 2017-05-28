package com.kwrp.planner_gui;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by KurtsPC on 9/05/2017.
 */

public class DialogAction extends AppCompatActivity{

    static {
        System.loadLibrary("calender");
    }
    private String filePath;
    private String selectedDate;

    public static AlertDialog createAboutDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("This application is designed for Otago University students. This" +
                        " application was created by students.");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

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
//                parent.removeView(view);
                switch (position) {
                    case 1:
                        toolbar.setBackgroundColor(Color.GREEN);
                        Log.d("CASE 0 ZIP", "CASE 0 ZIP\n\n\n");
                        break;
                    case 2:
                        toolbar.setBackgroundColor(Color.RED);
                        break;
                    case 3:
                        toolbar.setBackgroundColor(Color.BLUE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        spinner.setPadding(100,20,100,20);

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

    public static AlertDialog createSyncDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Synchronise");

        final LinearLayout dialogLayout = new LinearLayout(parent);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);


        TextView evisionTitle = new TextView(parent);
        evisionTitle.setText("Evision:");
        evisionTitle.setPadding(10, 10, 10, 10);
        evisionTitle.setGravity(Gravity.CENTER);
        evisionTitle.setTextColor(Color.rgb(0, 0, 0));
        evisionTitle.setTextSize(14);

        Button button = new Button(parent);
        button.setText("Otago Timetable");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(evisionTitle, 0);
        dialogLayout.addView(button, 1);

        TextView googleTitle = new TextView(parent);
        googleTitle.setText("Google Calendar:");
        googleTitle.setPadding(10, 10, 10, 10);
        googleTitle.setGravity(Gravity.CENTER);
        googleTitle.setTextColor(Color.rgb(0, 0, 0));
        googleTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Google Calendar");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(googleTitle, 2);
        dialogLayout.addView(button, 3);


        TextView hotmailTitle = new TextView(parent);
        hotmailTitle.setText("Hotmail Calendar:");
        hotmailTitle.setPadding(10, 10, 10, 10);
        hotmailTitle.setGravity(Gravity.CENTER);
        hotmailTitle.setTextColor(Color.rgb(0, 0, 0));
        hotmailTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Hotmail Calendar");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogLayout.addView(hotmailTitle, 4);
        dialogLayout.addView(button, 5);



        TextView facebookTitle = new TextView(parent);
        facebookTitle.setText("Facebook Events:");
        facebookTitle.setPadding(10, 10, 10, 10);
        facebookTitle.setGravity(Gravity.CENTER);
        facebookTitle.setTextColor(Color.rgb(0, 0, 0));
        facebookTitle.setTextSize(14);

        button = new Button(parent);
        button.setText("Facebook Events");
        button.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        dialogLayout.addView(facebookTitle, 6);
        dialogLayout.addView(button, 7);

        dialogLayout.setPadding(70,10,70,10);
        builder.setView(dialogLayout);

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

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
        for(String day : dayList) {

            dateList.add(day + "/"+month+"/"+year);
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


    private void createNewEvent(String title, String description, String start, String duration, ArrayList<String> dates) {
        String s;
        for(String selectedDate : dates) {
            s = modifyDate(selectedDate);
            jniCreateEvent(title, description, start, duration, filePath, s);

        }

    }

    private String modifyDate(String date) {

        String[] splitDate = date.split("/");

        //prefixing zero fix (day)
        if(splitDate[0].length() == 1){
            splitDate[0] = "0" + splitDate[0];
        }

        //prefixing zero fix (month)
        if (splitDate[1].length() == 1) {
            splitDate[1] = "0" + splitDate[1];
        }

        return (splitDate[0] + "/" + splitDate[1] + "/" + splitDate[2]);
    }

    public native String jniCreateEvent(String title, String description, String start,
                                        String duration, String dir, String selectedDate);


}
