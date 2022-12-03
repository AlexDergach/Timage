
/*
    Developed by David Davitashvili - C20406272
 */

package com.example.calendarevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calendarevent.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class TimageDatabaseHelper extends SQLiteOpenHelper
{
    // Initialize database variables
    public static final String DATABASE_NAME = "timage_db";
    public static final String DATABASE_TABLE1 = "category_tbl";
    public static final String DATABASE_TABLE2 = "task_tbl";
    public static final String DATABASE_TABLE3 = "event_tbl";
    public static final int DATABASE_VERSION = 1;

    // Initialize category_tbl variables
    public static final String TABLE1_KEY_ID = "_id";
    public static final String TABLE1_KEY_TITLE = "cat_title";

    // Initialize task_tbl variables
    public static final String TABLE2_KEY_ID = "_id";
    public static final String TABLE2_KEY_NAME = "task_name";
    public static final String TABLE2_KEY_DATE = "task_date";
    public static final String TABLE2_KEY_TIME = "task_time";
    public static final String TABLE2_KEY_DESC = "task_desc";
    public static final String TABLE2_KEY_COMPLETED = "task_completed";
    public static final String TABLE2_KEY_SOURCELINK = "task_sourcelink";
    public static final String TABLE2_KEY_CATEGORYID = "cat_id";

    // Initialize event_tbl variables
    public static final String TABLE3_KEY_ID = "_id";
    public static final String TABLE3_KEY_TITLE = "event_title";
    public static final String TABLE3_KEY_DATE = "event_date";
    public static final String TABLE3_KEY_TIME = "event_time";
    public static final String TABLE3_KEY_DESC = "event_desc";


    // Query to create category_tbl
    private static final String DATABASE_CREATETABLE1 =
            "create table " + DATABASE_TABLE1 +
                    " (" + TABLE1_KEY_ID + " integer primary key autoincrement, " +
                    TABLE1_KEY_TITLE + " text not null);";

    // Query to create task_tbl with foreign key to category_tbl
    private static final String DATABASE_CREATETABLE2 =
            "create table " + DATABASE_TABLE2 +
                    " (" + TABLE2_KEY_ID + " integer primary key autoincrement, " +
                    TABLE2_KEY_NAME + " text not null, " +
                    TABLE2_KEY_DATE + " text not null, " +
                    TABLE2_KEY_TIME + " text not null, " +
                    TABLE2_KEY_DESC + " text, " +
                    TABLE2_KEY_COMPLETED + " int not null, " +
                    TABLE2_KEY_SOURCELINK + " text, " +
                    TABLE2_KEY_CATEGORYID + " integer not null, " +
                    "foreign key (" + TABLE2_KEY_CATEGORYID + ") references " + DATABASE_TABLE1 + " (" + TABLE1_KEY_ID + "));";

    // Query to create event_tbl
    private static final String DATABASE_CREATETABLE3 =
            "create table " + DATABASE_TABLE3 +
                    " (" + TABLE3_KEY_ID + " integer primary key autoincrement, " +
                    TABLE3_KEY_TITLE + " text not null, " +
                    TABLE3_KEY_DATE + " text not null, " +
                    TABLE3_KEY_TIME + " text not null, " +
                    TABLE3_KEY_DESC + " text);";

    private SQLiteDatabase db;

    public TimageDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create the three tables
        db.execSQL(DATABASE_CREATETABLE1);
        db.execSQL(DATABASE_CREATETABLE2);
        db.execSQL(DATABASE_CREATETABLE3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {

    }

    // ###### Developer: Jaycel Estrellado, Calendar Page #############

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TABLE2_KEY_NAME, task.getTask());
        cv.put(TABLE2_KEY_COMPLETED, 0);
        db.insert(DATABASE_TABLE2, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
         try{
             cur = db.query(DATABASE_TABLE2, null, null, null, null, null, null, null);
             if(cur != null) {
                 if(cur.moveToFirst()){
                     do{
                         ToDoModel task = new ToDoModel();
                         task.setId(cur.getInt(cur.getColumnIndexOrThrow(TABLE2_KEY_ID)));
                         task.setTask(cur.getString(cur.getColumnIndexOrThrow(TABLE2_KEY_NAME)));
                         task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(TABLE2_KEY_COMPLETED)));
                         taskList.add(task);
                     }while(cur.moveToNext());
                 }//End inner if
             } // End outer if
         } finally {
             db.endTransaction();
             cur.close();
         } //End finally
        return taskList;
    }

    //test(???)
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE2_KEY_COMPLETED, status);
        db.update(DATABASE_TABLE2, cv, TABLE2_KEY_ID + "=?", new String[] {String.valueOf(id)});
    } // End update status

//    public void updateTask(int id, String task){
//        ContentValues cv = new ContentValues();
//        cv.put(TABLE2_KEY_NAME, task);
//        db.update(DATABASE_TABLE2, cv, TABLE2_KEY_ID + "=?", new String[] {String.valueOf(id)});
//    } // End update name

    public void deleteTask(int id) {
        db.delete(DATABASE_TABLE2, TABLE2_KEY_ID + "=?", new String[] {String.valueOf(id)});
    } //End delete
}