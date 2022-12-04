/***
 * Developed By: Jaycel Estrellado - C20372876
 */

package com.example.timage.Model;

public class ToDoModel {

    private int id, status;
    private String task, date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() { return date; }

    public void setDate(String date) {this.date = date; }

}
