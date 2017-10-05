package com.kwrp.planner_gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import static com.kwrp.planner_gui.R.id.gridview;


/**
 * Defines the displaymonth activity where the user is shown
 * every day in the month.
 *
 * @author KWRP
 */
public class DisplayMonth extends AppCompatActivity {

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


    /*month offset from current, if sees into the future, this month number will increase*/
    private static int month_offset = 0;

    /*The current shown year */
    private static int viewedYear = 0;

    /*The index of the current month*/
    private static int thisMonthIndex = 0;

    /* Loads the native library "calendar" on start up*/
    static {
        System.loadLibrary("calendar");
    }

    /*An ordered list of months to map month numbers to it's string counterpart*/
    private String[] monthList = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December"};

    /*Defines whether the "edit" pencil has been selected.
     Consider it "edit mode".*/
    private boolean editAvailable = true;

    /* Number of days selected if in edit mode*/
    private int daysSelected = 0;

    /* A list of days selected in edit mode */
    private Collection<String> dayList = new ArrayList<>();

    /* A list of positions selected in edit mode */
    private Collection<Integer> positionList = new ArrayList<>();
    private Collection<Integer> eventPosList = new ArrayList<>();


    /*The filepath where events are stored on the device */
    private String filePath;

    private GridView mGridView;

    private int currentYear;
    private boolean colorThreadRun = false;
    private int currentDayPos;
    private int currentDayColor = DialogAction.headColor;

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

    /**
     * The current date
     */
    private String currentDate = jniGetCurrentDate();
    private static String month;
    private static String year;

    /**
     * selected event Id (for removal)
     */
    private static String eventId = "";
    private static Button startTime;
    private static Button endTime;
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
        setContentView(R.layout.activity_display_month);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
        toolbar.setBackgroundColor(DialogAction.headColor);
        currentDate = jniGetCurrentDate();
        toolbar.setSubtitle("Today's Date: " + currentDate);
        setSupportActionBar(toolbar);
        filePath = getFilesDir().getAbsolutePath() + "/events.db";
        checkDbExists();

        //get current date
        int currentDay = 0;
        int index = 0;
        for (int i = 0; i < currentDate.length(); i++) {
            if (currentDate.charAt(i) == ('/')) {

                if (index == 0) {
                    index = i;
                } else {
                    currentDay = Integer.parseInt(currentDate.substring(0, index));
                    thisMonthIndex = (Integer.parseInt(currentDate.substring(index + 1, i)) - 1);
                    viewedYear = (Integer.parseInt(currentDate.substring(i + 1)));
                    currentYear = viewedYear;
                    break;
                }
            }
        }

        this.setTitle(monthList[thisMonthIndex + month_offset] + " - " + viewedYear);
        toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + viewedYear);
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
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        mGridView = (GridView) findViewById(gridview);

        setGridEventDefault(); //set grid on click listener to default

        String days = jniGetEventsDb(systemDate[1], systemDate[2], filePath);
        Log.e("EVENTS", jniGetEventsDb(systemDate[1], systemDate[2], filePath));
        ArrayList<Integer> eventDays = new ArrayList<Integer>();
        String[] events;
        if (days.length() > 0) {
            events = days.split("__");
            for (String s : events) {
                eventDays.add(Integer.parseInt(s));
            }
        }

        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1,
                Integer.parseInt(systemDate[2]), metrics, eventDays));

        mGridView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (editAvailable) {

                    month_offset -= 1;
                    if ((thisMonthIndex + month_offset) < 0) {
                        month_offset = 11 - thisMonthIndex;
                        viewedYear--;
                    }

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + viewedYear);
                    updateMonthView(month_offset);
                }
            }

            public void onSwipeLeft() {
                if (editAvailable) {

                    month_offset += 1;
                    if ((thisMonthIndex + month_offset) > 11) {
                        month_offset = -thisMonthIndex;
                        viewedYear++;
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
                    toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + viewedYear);
                    updateMonthView(month_offset);
                }
            }
        });


    }

    /**
     * Checks if the XML file where events are stored exists
     * and create a file if not.
     */
    private void checkDbExists() {
        File dir = getFilesDir();
        File file = new File(dir, "/events.db");

        if (!file.exists()) {
            filePath = getFilesDir().getAbsolutePath() + "/events.db";
            String newFile = jniCreateDb(filePath);
            Log.e("CREATE SQL:", newFile);
        }
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
            final AlertDialog dialog = DialogAction.createSettingsDialog(this);
            dialog.show();
            final Intent myIntent = new Intent(this, DisplayMonth.class);

            class ColorSetThread extends Thread {
                @Override
                public void run() {
                    colorThreadRun = true;
                    int c = DialogAction.headColor;
                    while (c == DialogAction.headColor) {
                    }
                    colorThreadRun = false;
                    startActivity(myIntent);
                    finish();


                }
            }

            if (!colorThreadRun) {
                new ColorSetThread().start();
            }
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
            setGridEventEdit();
            fab_close.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle("Add new events");
            toolbar.setSubtitle("");
            setSupportActionBar(toolbar);

            editAvailable = false;
        } else {
            setGridEventDefault();
            fab_close.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_month);
            toolbar.setTitle(monthList[thisMonthIndex + month_offset] + " - " + viewedYear);
            toolbar.setSubtitle("Today's Date: " + jniGetCurrentDate());
            setSupportActionBar(toolbar);

            FloatingActionButton fabconfirm = (FloatingActionButton) findViewById(R.id.fabconfirm);
            fabconfirm.setVisibility(View.GONE);

            for (int pos : positionList) {
                TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(pos);
                if (pos == currentDayPos && month_offset == 0 && viewedYear == currentYear) {
                    view2.setBackgroundColor(currentDayColor);
                } else if (eventPosList.contains(pos)) {
                    view2.setBackgroundColor(DialogAction.eventColor);
                } else {
                    view2.setBackgroundColor(DialogAction.defaultColor);
                }
            }


            positionList = new ArrayList<>();
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

        String[] systemDate = currentDate.split("/");
        int mon = Integer.parseInt(systemDate[1]) + month;
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        String days = jniGetEventsDb(Integer.toString(mon),
                Integer.toString(viewedYear), filePath);
        Log.e("EVENT DAYS", days);
        Log.e("Viewed Year", Integer.toString(viewedYear));
        Log.e("MONTH", Integer.toString(mon));
        String[] events = days.split("__");
        ArrayList<Integer> eventDays = new ArrayList<Integer>();
        if (days.length() > 0) {
            for (String s : events) {
                eventDays.add(Integer.parseInt(s));
            }
        }

        GridView mGridView = (GridView) findViewById(gridview);
        mGridView.invalidateViews();
        mGridView.setAdapter(new MonthAdapter(
                this, Integer.parseInt(systemDate[1]) - 1 + month, viewedYear, metrics, eventDays));
    }

    /**
     * When the user confirms their selection of dates in edit mode
     * it creates a dialog asking for event details.
     */
    public void createEventSetDialog() {

        String eventMonth = "" + (thisMonthIndex + month_offset + 1);
        String eventYear = "" + viewedYear;
        Dialog dialog = createEventDialog(eventMonth, eventYear);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.dialog);
    }


    public void setGridEventEdit() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!editAvailable) {
                    TextView view2 = (TextView) ((GridView) findViewById(gridview)).getChildAt(position);
                    Log.d("POSITION", "\n\n" + position + "\n\n");


                    Drawable background = view2.getBackground();
                    if (background instanceof ColorDrawable) {
                        int color = ((ColorDrawable) background).getColor();

                        if (color == currentDayColor) {
                            currentDayPos = new Integer(position);
                            color = DialogAction.defaultColor;
                        }


                        if (color == DialogAction.selectedColor) {
                            Integer pos = new Integer(position);
                            if (position == currentDayPos) {
                                view2.setBackgroundColor(currentDayColor);
                            } else if (eventPosList.contains(pos)) {
                                view2.setBackgroundColor(DialogAction.eventColor);
                                eventPosList.remove(pos);
                            } else {
                                view2.setBackgroundColor(DialogAction.defaultColor);
                            }

                            positionList.remove(pos);

                            String day = view2.getText().toString();
                            dayList.remove(day);
                            daysSelected -= 1;
                        } else if (color != DialogAction.outMonthColor) {
                            view2.setBackgroundColor(DialogAction.selectedColor);

                            Integer pos = new Integer(position);

                            if (color == DialogAction.eventColor) {
                                eventPosList.add(pos);
                            }

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
    }

    public void setGridEventDefault() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param v     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (editAvailable) {
                    TextView view = (TextView) ((GridView) findViewById(gridview)).getChildAt(position);
                    String dateSelected = view.getText().toString();
                    Intent myIntent = new Intent(view.getContext(), DisplayDay.class);
                    myIntent.putExtra("date", dateSelected);
                    myIntent.putExtra("month", Integer.toString(month_offset));
                    myIntent.putExtra("year", Integer.toString(viewedYear));
                    startActivity(myIntent);
                }
            }
        });
    }

    /**
     * Creates a new event accordingly
     *
     * @param title       title of the new event
     * @param description description of then new event
     * @param start       start time of the new event
     * @param finish      duration of the new event
     */
    private void createNewEvent(String title, String description, String start, String finish,
                                String startDate, int repeat) {

        Log.e("Finish Date: ", startDate);
        String r = jniCreateDbEvent(title, description, start, finish, startDate, startDate, Integer.toString(repeat), filePath);
        Log.e("CREATE Event:", r);
        newEvTitle = "";
        newEvDescription = "";
        newEvStartTime = "";
        newEvFinishTime = "";
    }

    /**
     * Creates the CREATE EVENT dialog box that pops up when the user wants to
     * add an event.
     *
     * @return the dialog.
     */
    private AlertDialog createEventDialog(final String month, final String year) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayMonth.this);

        final ScrollView scrollView = new ScrollView(this);
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

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.e("JAVA DAY", Integer.toString(dayList.size()));
                for (String day : dayList) {
                    newEvTitle = ((EditText) dialogLayout.getChildAt(1)).getText().toString();
                    newEvDescription = ((EditText) dialogLayout.getChildAt(3)).getText().toString();
                    newEvStartTime = startTime.getText().toString();
                    newEvFinishTime = endTime.getText().toString();

                    if (Integer.parseInt(day) < 10) {
                        newEvEndDate = "0" + day + "/" + month + "/" + year;
                    } else {
                        newEvEndDate = day + "/" + month + "/" + year;
                    }

                    if (newEvTitle.equals("") || newEvDescription.equals("") ||
                            newEvStartTime.equals("") || newEvFinishTime.equals("")) {
                        Log.d("JAVA create event: ", "failed!!");
                    } else {
                        createNewEvent(newEvTitle, newEvDescription, newEvStartTime, newEvFinishTime,
                                newEvEndDate, 0);
                    }
                }
                startEditState();
                updateMonthView(month_offset);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTime.setText("");
                endTime.setText("");
                dialog.cancel();
            }
        });
        scrollView.addView(dialogLayout);
        builder.setView(scrollView);

        return builder.create();
    }

    private View.OnClickListener startOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            DialogFragment newFragment = new DisplayMonth.StartTimeFrag();
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

            DialogFragment newFragment = new DisplayMonth.EndTimeFrag();
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
    public native String jniCreateDb(String filePath);

    public native String jniGetCurrentDate();

    public native String jniGetEventsDb(String month, String year, String filePath);

    public native String jniCreateDbEvent(String title, String description, String start,
                                          String finish, String selectedDate,
                                          String finishDate, String repeat, String filepath);

}
