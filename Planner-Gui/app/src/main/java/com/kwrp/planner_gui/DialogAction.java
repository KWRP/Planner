package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

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

    public static AlertDialog createSyncDialog(AppCompatActivity parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setMessage("")
                .setTitle("Synchronise")
                .setMessage("Synchronise with:\n-Evision\n-Google Calendar\n-Hotmail Calendar\n-Facebook Calendar");

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
                .setMessage("This option is to add one, or a series of events." +
                        "\nTo add an event for a single day, simply tap the selected day." +
                        "\nTo add an event for several days, simply hold and drag the selected days." +
                        "\n");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
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
