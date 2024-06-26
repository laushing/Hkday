package com.example.hkday;

public class EventData {
    // Add additional properties as needed //
    public int eventId;

    public String eventName;
    public String summary;
    public long eventDate;
    public String eventDes;

    public EventData(int eventId, String eventName,String summary, long eventDate, String eventDes) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.summary = summary;
        this.eventDate = eventDate;
        this.eventDes = eventDes;
    }
}
