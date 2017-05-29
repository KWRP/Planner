package com.kwrp.planner_gui;

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
    private String duration;

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

        String[] eventItems = event.split(";");
        this.eventId = eventItems[0];
        this.title = eventItems[1];
        this.description = eventItems[2];
        this.startTime = eventItems[3];
        this.duration = eventItems[4];
    }

    /**
     * Gets the eventId
     *
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets eventId
     *
     * @param eventId the eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Get the event title
     *
     * @return the event title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the event title
     *
     * @return the event title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the event description
     *
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the event description
     *
     * @return the event description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the event start time
     *
     * @return the event start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Set the event start time
     *
     * @return the event start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Get the event duration
     *
     * @return the event duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Set the event duration
     *
     * @return the event duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
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
                ", \n\t\tDescription : " + description +
                ", \n\t\tStartTime : " + startTime + " oclock" +
                ", \n\t\tDuration : " + duration + " hour(s)";
    }
}
