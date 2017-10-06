package com.kwrp.planner_gui;

import java.util.ArrayList;

/**
 * Defines an object day, with a date and events associated.
 *
 * @author KWRP
 */
public class Day {

    /*
     * The date the day is on.
     */
    private String date = "";

    /*
     * A list of events on that day.
     */
    private ArrayList<Event> events = new ArrayList<>();

    /*
     * Default constructor.
     */
    public Day() {
    }

    /**
     * Constructor passing in a string containing a list
     * of events in String form.
     *
     * @param data the list of events
     */
    public Day(String data) {
        String[] eventsSplit = data.split(";");
        for (String event : eventsSplit) {
            Event newEvent = new Event(event);
            events.add(newEvent);
        }
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Sets the date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the event.
     *
     * @param eventNumber the event number (index in list)
     * @return the event at that index
     */
    public Event getEvent(int eventNumber) {
        return events.get(eventNumber);
    }

    /**
     * Gets the list of events.
     *
     * @return the list of events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Gets the string form of the object.
     *
     * @return the string
     */
    public String toString() {
        return "Day{" +
                "date='" + date + '\'' +
                ", events=" + events +
                '}';
    }
}

