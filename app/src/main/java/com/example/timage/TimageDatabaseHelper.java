
/*
    Developed by David Davitashvili - C20406272
 */

package com.example.timage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.timage.Model.ToDoModel;

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
    public static final String TABLE2_KEY_MONTH = "task_month";
    public static final String TABLE2_KEY_YEAR = "task_year";
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
                    TABLE2_KEY_MONTH + " text not null, " +
                    TABLE2_KEY_YEAR + " text not null, " +
                    TABLE2_KEY_TIME + " text not null, " +
                    TABLE2_KEY_DESC + " text, " +
                    TABLE2_KEY_COMPLETED + " int not null, " +
                    TABLE2_KEY_SOURCELINK + " text, " +
                    TABLE2_KEY_CATEGORYID + " integer not null, " +
                    "foreign key (" + TABLE2_KEY_CATEGORYID + ") references " + DATABASE_TABLE1 + " (" + TABLE1_KEY_ID + "));";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {

    }

}