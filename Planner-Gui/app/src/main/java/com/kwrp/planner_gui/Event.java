package com.kwrp.planner_gui;

import android.util.Log;


/**
 * A class defining an event object containing all the appropriate
 * details (e.g. title, description)
 *
 * @author KWRP
 */
public class Event {
    /*
     * The event ID
     */
    private String eventId;
    private String eventDate;

    /*
     * The event title
     */
    private String title;

    /*
     * The event description
     */
    private String description;

    /*
     * The event start time
     */
    private String startTime;

    /*
     * The event duration
     */
    private String finishTime;
    private String endDate;


    /**
     * Constructor given an event formatted in a standard string form
     *
     * @param event the string event
     */
    public Event(String event) {

        Log.e("s", event);
        String[] eventItems = event.split("__");
        String[] startDay = eventItems[0].split("-");
        this.eventDate = startDay[2] + "/" + startDay[1] + "/" + startDay[0];
        this.title = eventItems[1];
        this.description = eventItems[2];
        this.startTime = eventItems[3];
        this.finishTime = eventItems[4];
        String[] day = eventItems[5].split("-");
        this.endDate = day[2] + "/" + day[1] + "/" + day[0];
        this.eventId = eventItems[6];
    }

    /**
     * Gets the eventId
     *
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /** Returns the end date
     *
     * @return String the end date
     */
    public String getEndDate() { return endDate;}

    /**
     * Get the event title
     *
     * @return the event title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the event description
     *
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /** Gets the finish time
     *
     * @return String finish time
     */
    public String getFinishTime() { return finishTime;}

    /**
     * Get the event start time
     *
     * @return the event start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Converts object to string
     *
     * @return the string version of the object displaying all fields
     */
    @Override
    public String toString() {
        return "\t\t" + title + " starts at " + startTime +
                "\n\t\t"  +description;

    }
}
