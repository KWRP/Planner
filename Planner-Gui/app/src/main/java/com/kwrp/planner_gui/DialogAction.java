package com.kwrp.planner_gui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Defines a series of actions for specific actions such as "sync" button select.
 * Returns dialog boxes for specific actions and in some cases sends data to back-end.
 *
 * @author KWRP
 */
public class DialogAction extends AppCompatActivity {

    public static int headColor = Color.BLUE;
    public static int textColor = Color.WHITE;
    public static int eventColor = Color.argb(70, 10, 80, 255);
    public static int defaultColor = Color.rgb(244, 244, 244);
    public static int outMonthColor = Color.rgb(234, 234, 250);
    public static int selectedColor = Color.LTGRAY;
    public static int dialogColor = R.color.dialog_blue;



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

                        "\n");

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return  builder.create();
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
        String text = "Select a colour scheme:";
        title.setText(text);
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(parent,
                android.R.layout.simple_spinner_dropdown_item, spinnerArray);

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
                        dialogColor = R.color.dialog_green;
                        break;
                    case 2:
                        headColor = Color.RED;
                        dialogColor = R.color.dialog_red;
                        break;
                    case 3:
                        headColor = Color.BLUE;
                        dialogColor = R.color.dialog_blue;
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
        dialogLayout.addView(spinner);
        dialogLayout.setPadding(100, 50, 100, 50);

        builder.setView(dialogLayout);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return  builder.create();
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
                .setMessage("How to use:"+
                        "\nSwipe side to side in the month view\n\tto change the viewed month." +
                        "\nTap on TODAY to back to the current \n\tmonth." +
                        "\nTap on the pencil icon to make \n\tindividual events on multiple days." +
                        "\nTap on a day to view and edit events \n\tfor that day." +
                        "\nTap on an event to update or delete \n\tthe chosen event."+
                        "\nUpdating a repeated event will only \n\tupdate occurrences for the chosen\n\tdate onwards."+
                        "\nDeleting a repeating event will delete \n\tall occurrences of that event.\n"+

                        "\nThe monthly and yearly repeat \n\toptions repeat on the given date\n\tby month or " +
                        "year until the finish\n\tdate eg. a monthly repeat with a\n\tstart date 12/02/2017 " +
                        "and a finish\n\tdate of 11/05/2017, will occur on\n\t12/02/2017, 12/03/2017 and\n\t12/04/2017." +
                        "\nWeekly repeat occurs on the day of\n\tthe week eg. every friday, until the\n\tfinish date."+
                        "\nDaily repeat occurs on every day\n\tuntil the finish date."+
                        "\nBy default the end time will be set\n\tto an hour after the chosen start\n\ttime."+
                        "\nAn event with a finish date earlier \n\tthan a start date is invalid and\n\twill not be created."+
                        "\nAll the input boxes need to be filled\n\tin or the event will not be created."+
                        "\n");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return  builder.create();
    }

}
