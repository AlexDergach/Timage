package com.example.calendarevent.Model;

public class CalendarModel {
    String task,time,date,month,year;

    public CalendarModel(String task, String time, String date, String month, String year) {
        this.task = task;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
