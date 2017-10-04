package com.kwrp.planner_gui;

import android.util.Log;


/**
 * A class defining an event object containg all the appropriate
 * details (e.g. title, description)
 *
 * @author KWRP
 */
public class Event {
    /**
     * The event ID
     */
    private String eventId;
    private String eventDate;

    /**
     * The event title
     */
    private String title;

    /**
     * The event description
     */
    private String description;

    /**
     * The event start time
     */
    private String startTime;

    /**
     * The event duration
     */
    private String finishTime;
    private String endDate;
    /**
     * Default constructor
     */
    public Event() {
    }

    /**
     * Constructor given an event formatted in a standard string form
     *
     * @param event the string event
     */
    public Event(String event) {

        Log.e("s", event);
        String[] eventItems = event.split("__");
        this.eventDate = eventItems[0];
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
    public String getEventDate() { return eventDate; }

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
        return "Event " + eventId + " :\n" +
                "\t\tTitle : " + title +
                "\n\t\tDescription : " + description +
                "\n\t\tStart Time : " + startTime +
                "\n\t\tFinish Time: " + finishTime +
                "\n\t\tStart Date: " + eventDate +
                "\n\t\tEnd Date: " + endDate;
    }
}
