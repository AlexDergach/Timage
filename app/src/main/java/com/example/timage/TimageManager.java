/*
    Developed by David Davitashvili - C20406272
 */

package com.example.timage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.timage.Model.ToDoModel;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;

public class TimageManager {
    // Initialize variables
    Context context;
    private TimageDatabaseHelper tdh;
    private SQLiteDatabase db;

    // Public constructor
    public TimageManager(Context context) {
        this.context = context;
    }

    // Creates new helper class and makes it writable
    public TimageManager open() throws SQLException {
        tdh = new TimageDatabaseHelper(context);
        db = tdh.getWritableDatabase();

        //db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='task_tbl';");
        return this;
    }

    // Closes the helper
    public void close() {
        tdh.close();
    }

    ///////////////////////////////// CATEGORY TABLE /////////////////////////////////////////

    // Inserts a row into category_tbl
    public long insertCategory(String title) {
        ContentValues initVals = new ContentValues();
        initVals.put(tdh.TABLE1_KEY_TITLE, title);

        return db.insert(tdh.DATABASE_TABLE1, null, initVals);
    }

    // Updates a row in category_tbl
    public boolean updateCategory(long rowID, String title) {
        ContentValues args = new ContentValues();
        args.put(tdh.TABLE1_KEY_TITLE, title);

        return db.update(tdh.DATABASE_TABLE1, args,
                tdh.TABLE1_KEY_ID + "=" + rowID,
                null) > 0;
    }

    // Deletes a row from category_tbl
    public boolean deleteCategory(long rowID) {
        return db.delete(tdh.DATABASE_TABLE1,
                tdh.TABLE1_KEY_ID + "=" + rowID,
                null) > 0;
    }

    // Gets a row based on the rowID in category_tbl
    public Cursor getCategory(long rowID) throws SQLException {
        Cursor cat_cursor = db.query(true, tdh.DATABASE_TABLE1, new String[]{
                        tdh.TABLE1_KEY_ID,
                        tdh.TABLE1_KEY_TITLE},
                tdh.TABLE1_KEY_ID + "=" + rowID,
                null,
                null,
                null,
                null,
                null);

        if (cat_cursor != null) {
            cat_cursor.moveToFirst();
        }

        return cat_cursor;
    }

    // Gets all rows from category_tbl
    public Cursor getAllCategories() {
        return db.query(tdh.DATABASE_TABLE1, new String[]{
                        tdh.TABLE1_KEY_ID,
                        tdh.TABLE1_KEY_TITLE},
                null,
                null,
                null,
                null,
                null);
    }

    public String getTitle(int i) {
        String selectQuery = "select " + tdh.TABLE1_KEY_TITLE + " from " + tdh.DATABASE_TABLE1 + " where " + tdh.TABLE1_KEY_ID + " = " + i;
        String value = "";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            value = cursor.getString(0);
        }

        return value;
    }

    //////////////////////////////////// TASKS TABLE ///////////////////////////////////////

    // Inserts a row into task_tbl
    public long insertTask(String name, String date, String month, String year, String time, String desc, int completed, String sLink, int cat_id) {
        ContentValues initVals = new ContentValues();
        initVals.put(tdh.TABLE2_KEY_NAME, name);
        initVals.put(tdh.TABLE2_KEY_DATE, date);
        initVals.put(tdh.TABLE2_KEY_MONTH, month);
        initVals.put(tdh.TABLE2_KEY_YEAR, year);
        initVals.put(tdh.TABLE2_KEY_TIME, time);
        initVals.put(tdh.TABLE2_KEY_DESC, desc);
        initVals.put(tdh.TABLE2_KEY_COMPLETED, completed);
        initVals.put(tdh.TABLE2_KEY_SOURCELINK, sLink);
        initVals.put(tdh.TABLE2_KEY_CATEGORYID, cat_id);

        return db.insert(tdh.DATABASE_TABLE2, null, initVals);
    }

    // Updates a row in task_tbl
    public boolean updateTask(long rowID, String name, String date, String month, String year, String time, String desc, int completed, String sLink, int cat_id) {
        ContentValues args = new ContentValues();
        args.put(tdh.TABLE2_KEY_NAME, name);
        args.put(tdh.TABLE2_KEY_DATE, date);
        args.put(tdh.TABLE2_KEY_MONTH, month);
        args.put(tdh.TABLE2_KEY_YEAR, year);
        args.put(tdh.TABLE2_KEY_TIME, time);
        args.put(tdh.TABLE2_KEY_DESC, desc);
        args.put(tdh.TABLE2_KEY_COMPLETED, completed);
        args.put(tdh.TABLE2_KEY_SOURCELINK, sLink);
        args.put(tdh.TABLE2_KEY_CATEGORYID, cat_id);

        return db.update(tdh.DATABASE_TABLE2, args,
                tdh.TABLE2_KEY_ID + "=" + rowID,
                null) > 0;
    }

    // Deletes a row from task_tbl
    public boolean deleteTask(long rowID) {
        return db.delete(tdh.DATABASE_TABLE2,
                tdh.TABLE2_KEY_ID + "=" + rowID,
                null) > 0;
    }

    // Gets a row based on the rowID in task_tbl
    public Cursor getTask(long rowID) throws SQLException {
        Cursor task_cursor = db.query(true, tdh.DATABASE_TABLE2, new String[]{
                        tdh.TABLE2_KEY_ID,
                        tdh.TABLE2_KEY_NAME,
                        tdh.TABLE2_KEY_DATE,
                        tdh.TABLE2_KEY_TIME,
                        tdh.TABLE2_KEY_DESC,
                        tdh.TABLE2_KEY_COMPLETED,
                        tdh.TABLE2_KEY_SOURCELINK,
                        tdh.TABLE2_KEY_CATEGORYID},
                tdh.TABLE2_KEY_ID + "=" + rowID,
                null,
                null,
                null,
                null,
                null);

        if (task_cursor != null) {
            task_cursor.moveToFirst();
        }

        return task_cursor;
    }

    // Gets all rows from task_tbl
    public Cursor getAllTasks() {
        return db.query(tdh.DATABASE_TABLE2, new String[]{
                        tdh.TABLE2_KEY_NAME,
                        tdh.TABLE2_KEY_DATE,
                        tdh.TABLE2_KEY_TIME,
                        tdh.TABLE2_KEY_DESC,
                        tdh.TABLE2_KEY_COMPLETED,
                        tdh.TABLE2_KEY_SOURCELINK,
                        tdh.TABLE2_KEY_CATEGORYID},
                null,
                null,
                null,
                null,
                null);
    }

    // ############### Developer: Jaycel Estrellado ############
    // ############### CALENDAR START ###############

    public Cursor getDateTasks(String date, SQLiteDatabase database) {
        String[] Projections = {tdh.DATABASE_TABLE2,
                tdh.TABLE2_KEY_NAME,
                tdh.TABLE2_KEY_DATE,
                tdh.TABLE2_KEY_MONTH,
                tdh.TABLE2_KEY_YEAR,
                tdh.TABLE2_KEY_TIME};

        String Selection = tdh.TABLE2_KEY_DATE + "=?";
        String[] SelectionArgs = {date};

        return db.query(tdh.DATABASE_TABLE2, Projections, Selection, SelectionArgs, null, null, null, null);
    } // End of getDateTasks

    public Cursor getDateTasksPerMonth(String month, String year, SQLiteDatabase database) {
        String[] Projections = {tdh.DATABASE_TABLE2,
                tdh.TABLE2_KEY_NAME,
                tdh.TABLE2_KEY_DATE,
                tdh.TABLE2_KEY_MONTH,
                tdh.TABLE2_KEY_YEAR,
                tdh.TABLE2_KEY_TIME};

        String Selection = tdh.TABLE2_KEY_MONTH + "=? and " + tdh.TABLE2_KEY_YEAR + "=?";
        String[] SelectionArgs = {month,year};

        return db.query(tdh.DATABASE_TABLE2, Projections, Selection, SelectionArgs, null, null, null, null);
    } // End of getDateTasks

    // ############# CALENDAR END ###############

    // ############# TASK START ###################

    public List<ToDoModel> getAllTask() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(tdh.DATABASE_TABLE2, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(tdh.TABLE2_KEY_ID)));
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(tdh.TABLE2_KEY_NAME)));
                        task.setDate(cur.getString(cur.getColumnIndexOrThrow(tdh.TABLE2_KEY_DATE)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(tdh.TABLE2_KEY_COMPLETED)));
                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    } // End of getAllTask

    public void updateStatus(int id, int status) {
        //Log.i("Check This", String.valueOf(id));
        ContentValues cv = new ContentValues();
        cv.put(tdh.TABLE2_KEY_COMPLETED, status);
        db.update(tdh.DATABASE_TABLE2, cv, tdh.TABLE2_KEY_ID + "= ?" , new String[]{String.valueOf(id)});
    } // End update status

//    public void updateTask(int id, String task){
//        ContentValues cv = new ContentValues();
//        cv.put(tdh.TABLE2_KEY_NAME, task);
//        db.update(tdh.DATABASE_TABLE2, cv, tdh.TABLE2_KEY_ID + "= ?", new String[] {String.valueOf(id)});
//    } // End update name

}
