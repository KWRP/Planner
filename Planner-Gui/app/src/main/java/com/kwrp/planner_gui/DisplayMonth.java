package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import static com.kwrp.planner_gui.R.id.gridview;


/**
 * Defines the displaymonth activity where the user is shown
 * every day in the month.
 *
 * @author KWRP
 */
public class DisplayMonth extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    /**
     * month offset from current, if sees into the future, this month number will increase
     */
    private static int month_offset = 0;
    /**
     * The current shown year
     */
    private static int thisYear = 0;
    /**
     * The index of the current month
     */
    private static int thisMonthIndex = 0;

    /**
     * Loads the native library "calender" on start up
     */
    static {
        System.loadLibrary("calendar");
    }

    /**
     * An ordered list of months to map month numbers to it's string counterpart
     */
    private String[] monthList = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};
    /**
     * Defines whether the "edit" pencil has been selected.
     * Consider it "edit mode".
     */
    private boolean editAvailable = true;
    /**
     * Number of days selected if in edit mode
     */
    private int daysSelected = 0;
    /**
     * A list of days selected in edit mode
     */
    private Collection<String> dayList = new ArrayList<>();
    /**
     * A list of positions selected in edit mode
     */
    private Collection<Integer> positionList = new ArrayList<>();
    /**
     * The current date (system date)
     */
    private String currentDate;
    /**
     * The filepath where events are stored on the device
     */
    private String filePath;

    /**
     * Called when the activity is first created. Sets up buttons, labels, and initialises variables.
     *
     * @param savedInstanceState defines the action such as screen rotation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);

        currentDate = jniGetCurrentDate();
        toolbar.setSubtitle("Today's Date: " + currentDate);
        setSupportActionBar(toolbar);
        filePath = getFilesDir().getAbsolutePath() + "/events.xml";

        int index = 0;
        for (int i = 0; i < currentDate.length(); i++) {
            if (currentDate.charAt(i) == ('/')) {
                if (index == 0) {
                    index = i;
                } else {
                    thisMonthIndex = (Integer.parseInt(currentDate.substring(index + 1, i)) - 1);
                    thisYear = (Integer.parseInt(currentDate.substring(i + 1)));
                    break;
                }
            }
        }

        this.setTitle(monthList[thisMonthIndex + month_offset] + " - " + thisYear);
        toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + thisYear);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditState();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab_close);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditState();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fabconfirm);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEventSetDialog();
            }
        });


        String[] systemDate = currentDate.split("/");
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1, Integer.parseInt(systemDate[2]), metrics));


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!editAvailable) {
                    TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(position);

                    Drawable background = view2.getBackground();
                    if (background instanceof ColorDrawable) {
                        int color = ((ColorDrawable) background).getColor();
                        if (color == Color.LTGRAY) {

                            Integer pos = new Integer(position);

                            positionList.remove(pos);
                            String day = view2.getText().toString();
                            dayList.remove(day);
                            daysSelected -= 1;
                        } else if (color == Color.rgb(244, 244, 244)) {
                            view2.setBackgroundColor(Color.LTGRAY);

                            Integer pos = new Integer(position);
                            positionList.add(pos);
                            String day = view2.getText().toString();
                            dayList.add(day);
                            daysSelected += 1;
                        }
                    }
                    FloatingActionButton fabconfirm = (FloatingActionButton) findViewById(R.id.fabconfirm);
                    if (daysSelected > 0) {
                        fabconfirm.setVisibility(View.VISIBLE);
                    } else {
                        fabconfirm.setVisibility(View.GONE);
                    }

                }

            }

        });


        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                if (editAvailable) {
                    TextView view = (TextView) ((GridView) findViewById(gridview)).getChildAt(arg2);
                    String dateSelected = view.getText().toString();
                    Intent myIntent = new Intent(arg1.getContext(), DisplayDay.class); /** Class name here */
                    myIntent.putExtra("date", dateSelected);
                    myIntent.putExtra("month", Integer.toString(month_offset));
                    myIntent.putExtra("year", Integer.toString(thisYear));
                    startActivity(myIntent);
                }
                return true;

            }

        });

        mGridView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (editAvailable) {

                    month_offset -= 1;
                    if ((thisMonthIndex + month_offset) < 0) {
                        month_offset = 11 - thisMonthIndex;
                        thisYear--;
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + thisYear);
                    updateMonthView(month_offset);
                }
            }

            public void onSwipeLeft() {
                if (editAvailable) {

                    month_offset += 1;
                    if ((thisMonthIndex + month_offset) > 11) {
                        month_offset = -thisMonthIndex;
                        thisYear++;
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + thisYear);
                    updateMonthView(month_offset);
                }
            }
        });
    }

    /**
     * Called when the options menu on the toolbar is created or updated.
     * Contains visibility toggling for menu items when going in and out
     * of edit mode.
     *
     * @param menu The menu on the toolbar
     * @return true boolean type
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item1 = menu.findItem(R.id.action_settings);
        MenuItem item2 = menu.findItem(R.id.action_sync);
        MenuItem item3 = menu.findItem(R.id.action_about);
        MenuItem item4 = menu.findItem(R.id.action_help);

        if (!editAvailable) {

            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);

            item4.setVisible(true);
        } else {
            item1.setVisible(true);
            item2.setVisible(true);
            item3.setVisible(true);

            item4.setVisible(false);
        }
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

        if (id == R.id.action_help) {
            AlertDialog dialog = DialogAction.createHelpDialog(this);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggles edit mode, each time this method is called, it will toggle between edit mode.
     * It essentially disables some listeners and actions to minimise action clashing and
     * notifies the user that they are able to add an event to a series of days.
     */
    private void startEditState() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab_close = (FloatingActionButton) findViewById(R.id.fab_close);
        if (editAvailable) {

            fab_close.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle("Add new events");
            toolbar.setSubtitle("");
            setSupportActionBar(toolbar);

            editAvailable = false;
        } else {

            fab_close.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + thisYear);
            toolbar.setSubtitle("Today's Date: " + jniGetCurrentDate());
            setSupportActionBar(toolbar);

            FloatingActionButton fabconfirm = (FloatingActionButton) findViewById(R.id.fabconfirm);
            fabconfirm.setVisibility(View.GONE);

            for (int pos : positionList) {
                TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(pos);
                view2.setBackgroundColor(Color.rgb(244, 244, 244));
            }
            dayList = new ArrayList<>();
            daysSelected = 0;
            editAvailable = true;
        }
    }

    /**
     * Updates the month view when the user swipes left or swipes right by
     * updating the gridview adapter.
     *
     * @param month the month offset from the initial month.
     */
    public void updateMonthView(int month) {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
//        toolbar.setSubtitle("Today's Date: " + currentDate);
        String[] systemDate = currentDate.split("/");
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1 + month, thisYear, metrics));
    }

    /**
     * When the user confirms their selection of dates in edit mode
     * it creates a dialog asking for event details.
     */
    public void createEventSetDialog() {
        DialogAction a = new DialogAction();
        String eventMonth = "" + (thisMonthIndex + month_offset + 1);
        String eventYear = "" + thisYear;

        AlertDialog dialog = a.createEventSetDialog(this, dayList, eventMonth, eventYear, filePath);
        dialog.show();
        startEditState();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String jniGetCurrentDate();
}
