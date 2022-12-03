package com.example.calendarevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.calendarevent.Adapter.ToDoAdapter;
import com.example.calendarevent.Model.ToDoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * Developed by Jaycel Estrellado - C20372876
     */

    // Calendar variables
    private SQLiteDBHandler dbHandler;
    private EditText editText;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;

    // Task variables
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;

    private List<ToDoModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //####### Calendar ########
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
        // ####### End Calendar ######

        //####### Task Start #######

        getSupportActionBar().hide();

        taskList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecycleView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("This is a Test Task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        tasksAdapter.setTasks(taskList);

    } // End class

//    public void InsertDB(View view){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("Date", selectedDate);
//        contentValues.put("Event", editText.getText().toString());
//        sqLiteDatabase.insert("EventCal", null, contentValues);
//    } // End class

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