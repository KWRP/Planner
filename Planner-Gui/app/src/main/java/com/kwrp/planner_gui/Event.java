package com.kwrp.planner_gui;

/**
 * Created by wsanson on 5/27/17.
 */

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String startTime;
    private String duration;

    public Event() {
    }

    public Event(String event) {

        String[] eventItems = event.split(";");
        this.eventId = eventItems[0];
        this.title = eventItems[1];
        this.description = eventItems[2];
        this.startTime = eventItems[3];
        this.duration = eventItems[4];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
