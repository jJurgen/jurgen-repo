package com.jurgen.chat.entities;

import java.sql.Time;
import java.util.Date;

public class Message {

    private String message;
    private String author;
    private Time time;

    public Message(String message, String author) {
        this.message = message;
        this.author = author;
        time = new Time(new Date().getTime());
        System.out.println(time.toString());
    }

    public Message(String message, String author, Time time) {
        this.message = message;
        this.author = author;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}
