package com.kwrp.planner_gui;

import java.util.ArrayList;

/**
 * Created by wsanson on 5/27/17.
 */

public class Day {

    private String date = "";
    private ArrayList<Event> events = new ArrayList<>();

    public Day() {
    }

    public Day(String data) {
        String[] eventsSplit = data.split(":");
        for (String event : eventsSplit) {
            Event newEvent = new Event(event);
            events.add(newEvent);
        }
    }

    public static String hello() {
        return "JAVA hello";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate(String date) {
        return this.date;
    }


    public Event getEvent(int eventNumber) {
        return events.get(eventNumber);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public int numOfEvents() {
        return events.size();
    }

    public String toString() {
        return "Day{" +
                "date='" + date + '\'' +
                ", events=" + events +
                '}';
    }
}

