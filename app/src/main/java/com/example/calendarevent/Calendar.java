package com.example.calendarevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

public class Calendar extends AppCompatActivity {

    /**
     * Developed by Jaycel Estrellado - C20372876
     */
    private SQLiteDBHandler dbHandler;
    private EditText editText;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                ReadDB(view);
            }
        });

        try{
            dbHandler = new SQLiteDBHandler(this, "CalendarEvent", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EventCal (Date TEXT, Event TEXT)");
        } // End try
        catch(Exception e){
            e.printStackTrace();
        } // End catch
    } // End class

    public void InsertDB(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", selectedDate);
        contentValues.put("Event", editText.getText().toString());
        sqLiteDatabase.insert("EventCal", null, contentValues);
    } // End class

    public void ReadDB(View view){

        String qry = "SELECT Event FROM EventCal WHERE Date =" + selectedDate;
        try{
            Cursor cur = sqLiteDatabase.rawQuery(qry, null);
            cur.moveToFirst();
            editText.setText(cur.getString(0));

        }catch(Exception e) {
            e.printStackTrace();
            editText.setText("");
        }// End catch
    }//End class
}