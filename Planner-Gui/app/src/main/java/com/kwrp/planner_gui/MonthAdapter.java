package com.kwrp.planner_gui;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * MonthAdapter handles all of the data surrounding date generation
 * for a particular month, and ensuring it is correctly displayed.
 * As well as ensuring each day is classified into a day of the week.
 *
 * @author https://github.com/jrdnull/Android-Calendar-GridView-Adapter/blob/master/MonthAdapter.java
 */
public class MonthAdapter extends BaseAdapter {
    /*An array mapping indexes (days of the week) to Strings (the day) */
    private final String[] mDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    /*The number of days in each month.*/
    private final int[] mDaysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /*An instance of GregorianCalendar*/
    private GregorianCalendar mCalendar;

    /*Calendar for today*/
    private Calendar mCalendarToday;

    /*The current month context */
    private Context mContext;

    /*this months display metrics*/
    private DisplayMetrics mDisplayMetrics;

    /*A list of days in the month*/
    private List<String> mItems;

    /* month index*/
    private int mMonth;
    /**
     * this months year
     */
    private int mYear;
    /**
     * number of days shown
     */
    private int mDaysShown;
    /**
     * number of days last month
     */
    private int mDaysLastMonth;
    /**
     * number of days next month
     */
    private int mDaysNextMonth;
    /**
     * title and day height for a particular day
     */
    private int mTitleHeight, mDayHeight;

    private ArrayList<Integer> eventDays = new ArrayList<Integer>();

    /**
     * Called on swipe, when the adapter needs to be updated for a new month
     *
     * @param c       the context where the months are shown
     * @param month   the month to generate for
     * @param year    the year to generate for
     * @param metrics the dimensions
     */
    public MonthAdapter(Context c, int month, int year, DisplayMetrics metrics, ArrayList<Integer> days) {
        mContext = c;
        mMonth = month;
        mYear = year;
        mCalendar = new GregorianCalendar(mYear, mMonth, 1);
        mCalendarToday = Calendar.getInstance();
        mDisplayMetrics = metrics;
        eventDays =  days;
        populateMonth();

    }

    /**
     * @param date     - null if day title (0 - dd / 1 - mm / 2 - yy)
     * @param position - position in item list
     * @param item     - view for date
     */
    protected void onDate(int[] date, int position, View item) {
    }

    /**
     * Populates the month with appropriate number of days
     */
    private void populateMonth() {
        mItems = new ArrayList<>();
        for (String day : mDays) {
            mItems.add(day);
            mDaysShown++;
        }

        int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
        int prevDay;
        if (mMonth == 0)
            prevDay = daysInMonth(11) - firstDay + 1;
        else
            prevDay = daysInMonth(mMonth - 1) - firstDay + 1;
        for (int i = 0; i < firstDay; i++) {
            mItems.add(String.valueOf(prevDay + i));
            mDaysLastMonth++;
            mDaysShown++;
        }

        int daysInMonth = daysInMonth(mMonth);
        for (int i = 1; i <= daysInMonth; i++) {
            mItems.add(String.valueOf(i));
            mDaysShown++;
        }

        mDaysNextMonth = 1;
        while (mDaysShown % 7 != 0) {
            mItems.add(String.valueOf(mDaysNextMonth));
            mDaysShown++;
            mDaysNextMonth++;
        }

        // show events
        for (Integer i : eventDays) i += 13;

        //buffer bottom of the screen with another row of next month dates
        if(mDaysNextMonth < 5) {
            for (int i = 1; i < 8; i++) {
                mItems.add(String.valueOf(mDaysNextMonth));
                mDaysShown++;
                mDaysNextMonth++;
            }
        }

        mTitleHeight = 30;
        int rows = (mDaysShown / 7) ;
        mDayHeight = (mDisplayMetrics.heightPixels - mTitleHeight
                - (rows * 8) - getBarHeight()) / (rows - 1);
        mDayHeight *= 0.98f;
//        if(rows <= 6){
//            mDayHeight *= 0.98f;
//        }

    }

    /**
     * Finds the number of days in the month
     *
     * @param month the month to find the number of days in
     * @return the number of days in that month
     */
    private int daysInMonth(int month) {
        int daysInMonth = mDaysInMonth[month];
        if (month == 1 && mCalendar.isLeapYear(mYear))
            daysInMonth++;
        return daysInMonth;
    }

    /**
     * Gets the bar height
     *
     * @return bar height
     */
    private int getBarHeight() {
        switch (mDisplayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                return 48;
            case DisplayMetrics.DENSITY_MEDIUM:
                return 32;
            case DisplayMetrics.DENSITY_LOW:
                return 24;
            default:
                return 48;
        }
    }

    /**
     * Gets a day
     *
     * @param day the day
     * @return int 0-6) where 0 = MONDAY, and 6 = SUNDAY
     */
    private int getDay(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }

    /**
     * Determines if the given date is the current date
     *
     * @param day   given day
     * @param month given month
     * @param year  given year
     * @return true is they are the same
     */
    private boolean isToday(int day, int month, int year) {
        return mCalendarToday.get(Calendar.MONTH) == month
                && mCalendarToday.get(Calendar.YEAR) == year
                && mCalendarToday.get(Calendar.DAY_OF_MONTH) == day;
    }

    /**
     * Get's the date at position n
     *
     * @param position in the grid
     * @return the date
     */
    private int[] getDate(int position) {
        int date[] = new int[3];
        if (position <= 6) {
            return null; // day names
        } else if (position <= mDaysLastMonth + 6) {
            // previous month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 0) {
                date[1] = 11;
                date[2] = mYear - 1;
            } else {
                date[1] = mMonth - 1;
                date[2] = mYear;
            }
        } else if (position <= mDaysShown - mDaysNextMonth) {
            // current month
            date[0] = position - (mDaysLastMonth + 6);
            date[1] = mMonth;
            date[2] = mYear;
        } else {
            // next month
            date[0] = Integer.parseInt(mItems.get(position));
            if (mMonth == 11) {
                date[1] = 0;
                date[2] = mYear + 1;
            } else {
                date[1] = mMonth + 1;
                date[2] = mYear;
            }
        }
        return date;
    }

    /**
     * Retrieves the view
     *
     * @param position    the position
     * @param convertView the view to convert
     * @param parent      where the views are being spawned
     * @return the new view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView view = new TextView(mContext);
        view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        view.setText(mItems.get(position));
        view.setTextColor(Color.BLACK);

        int[] date = getDate(position);
        if (date != null) {
            view.setHeight(mDayHeight - 50);
            if (date[1] != mMonth) {
                // previous or next month
                view.setBackgroundColor(Color.rgb(234, 234, 250));
            } else {
                // current month
                view.setBackgroundColor(Color.rgb(244, 244, 244));
                if (isToday(date[0], date[1], date[2])) {
                    view.setTextColor(Color.BLACK);
                    view.setBackgroundColor(DialogAction.headColor);

                }
            }
        } else {
            view.setBackgroundColor(Color.argb(100, 10, 80, 255));
            view.setHeight(mTitleHeight + 100);
        }
        if (eventDays.contains(position)) view.setBackgroundColor(Color.argb(100, 0, 0, 255));

        onDate(date, position, view);
        return view;
    }

    /**
     * Gets the number of days in the month
     *
     * @return the number of days
     */
    @Override
    public int getCount() {
        return mItems.size();
    }

    /**
     * Gets an item at position
     *
     * @param position the position
     * @return an item
     */
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Gets the id of an item at position
     *
     * @param position the position
     * @return the id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}