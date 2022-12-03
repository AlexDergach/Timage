package com.example.calendarevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskListManager {

    Context context;
    private TimageDatabaseHelper DBH;
    private SQLiteDatabase myDatabase;

    public TaskListManager(Context context) {
        this.context = context;
    } // End task list manager

    public TaskListManager open() throws SQLException {
        DBH = new TimageDatabaseHelper(context);
        myDatabase = DBH.getWritableDatabase();
        return this;
    } // End task list manager

    public void close() { DBH.close(); }

    public long insertTask(String name, String status) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TimageDatabaseHelper.TABLE2_KEY_NAME, name);
        initialValues.put(TimageDatabaseHelper.TABLE2_KEY_COMPLETED, status);

        return myDatabase.insert(TimageDatabaseHelper.DATABASE_TABLE2, null, initialValues);
    } //End of insert task

    public boolean deleteTask(String _id) {
        //Delete statement, if any rows deleted (o.e >0), it returns true
        return myDatabase.delete(TimageDatabaseHelper.DATABASE_TABLE2, DBH.TABLE2_KEY_ID + "=" + _id, null) > 0;
    } // End of delete task

    public Cursor getAllTasks() {

        return myDatabase.query(TimageDatabaseHelper.DATABASE_TABLE2, new String[]{

            TimageDatabaseHelper.TABLE2_KEY_ID,
            TimageDatabaseHelper.TABLE2_KEY_NAME,
            TimageDatabaseHelper.TABLE2_KEY_COMPLETED},
            null,
            null,
            null,
            null,
            null);
    } // End of class

    public Cursor getTask(String _id) throws SQLException {

        Cursor mCursor = myDatabase.query(true, TimageDatabaseHelper.DATABASE_TABLE2, new String[] {
                TimageDatabaseHelper.TABLE2_KEY_ID,
                TimageDatabaseHelper.TABLE2_KEY_NAME,
                TimageDatabaseHelper.TABLE2_KEY_COMPLETED},

                TimageDatabaseHelper.TABLE2_KEY_ID + "=" + _id,
                null,
                null,
                null,
                null,
                null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        } // End of is
        return mCursor;
    } // Enf of getTask

    public boolean updateTask(String _id, String name, String status) {
        ContentValues args = new ContentValues();
        args.put(TimageDatabaseHelper.TABLE2_KEY_NAME, name);
        args.put(TimageDatabaseHelper.TABLE2_KEY_COMPLETED, status);
        return myDatabase.update(TimageDatabaseHelper.DATABASE_TABLE2, args, TimageDatabaseHelper.TABLE2_KEY_ID + "=" + _id, null) >0;
    } //End updateTask
} // End manager

