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
    public static int eventColor = Color.argb(100, 10, 80, 255);
    public static int defaultColor = Color.rgb(244, 244, 244);
    public static int outMonthColor = Color.rgb(234, 234, 250);
    public static int selectedColor = Color.LTGRAY;


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
                .setMessage("How to use:"+
                        "\nSwipe side to side to change month."+
                        "\nTap on a day to edit single day. Tap on the pencil icon to make an event"+
                        "\n\nTap the pencil icon to enable multi-day event creation" +
                        "\n\nThe monthly and yearly repeat opinions repeat on that day of that month or " +
                        "year up till the finish date eg a monthly repeat with a start date on 12/02/2017 " +
                        "with an finish date of 11/05/2017 it will repeat on 12/02/2017, 12/03/2017 and 12/04/2017." +
                        "\nWeekly repeat repeats on that day of the week eg friday, up till the finish date."+
                        "\nDaily repeat repeats on ever day up till the finish date."+
                        "\nBy default the end time will be set to an hour after start time."+
                        "\nIf the finish date is before the date of an event that you are trying to add the event will not be added."+
                        "\nAll the input boxes need to be filled in or the event will not be created."+
                        "\n\nTap on an event to update or delete it."+
                        "\nUpdating a repeated event will change that event and the event after it, but keep the events before it the same."+
                        "\nDeleting a repeating event will delete all the repeating events."+
                        "\n");

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
