package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import static android.graphics.Color.parseColor;
import static com.kwrp.planner_gui.R.id.gridview;

/**
 * Created by KurtsPC on 9/05/2017.
 */

public class DialogAction {

    public static AlertDialog createAboutDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("About us")
                .setMessage("Cyka Blyat Idi Nahoi");

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
//    <color name="colorPrimary">#2a4af8</color>
//    <color name="colorPrimaryDark">#303f9f</color>
//    <color name="colorAccent">#ff4081</color>
//    <color name="background_blue">#eaeafa</color>
//    <color name="days_of_week_blue">#640a50ff</color>
//    <color name="red">#ff0000</color>
//    <color name="blue">#ff33b5e5</color>
//    <color name="green">#ff669900</color>
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

        builder.setNeutralButton("Got it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static AlertDialog createEventSetDialog(AppCompatActivity parent, Collection<Integer> positionList, String date) {
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
        startInput.setInputType(InputType.TYPE_CLASS_TEXT);
        startInput.setHint("e.g. 0800");
        dialogLayout.addView(startInput, 5);

        final TextView durationLabel = new TextView(parent);
        durationLabel.setText("Duration");
        dialogLayout.addView(durationLabel, 6);
        final EditText durationInput = new EditText(parent);
        durationInput.setInputType(InputType.TYPE_CLASS_TEXT);
        durationInput.setHint("Number of hours");
        dialogLayout.addView(durationInput, 7);

        builder.setView(dialogLayout);

        //get month and year
        String dateSuffix = "DialogAction.java ERROR";
        for(int i =0;i<date.length(); i++){
            if(date.charAt(i) == '/'){
                dateSuffix = date.substring(i);
                break;
            }
        }
        ArrayList<String> dateList = new ArrayList<>();
        for(Integer i : positionList) {
            i -=6;
            dateList.add(i.toString() + dateSuffix);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEventTitle = ((EditText) dialogLayout.getChildAt(1)).getText().toString();
                String newEventDescription = ((EditText) dialogLayout.getChildAt(3)).getText().toString();
                String newEventStart = ((EditText) dialogLayout.getChildAt(5)).getText().toString();
                String newEventDuration = ((EditText) dialogLayout.getChildAt(7)).getText().toString();

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





}
