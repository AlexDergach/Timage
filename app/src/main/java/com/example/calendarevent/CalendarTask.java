package com.example.calendarevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.calendarevent.Adapter.ToDoAdapter;
import com.example.calendarevent.Model.ToDoModel;

import java.util.List;
import java.util.Objects;

public class CalendarTask extends AppCompatActivity {

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
        setContentView(R.layout.calendar_task);

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

        Objects.requireNonNull(getSupportActionBar()).hide();

        TimageManager db = new TimageManager(this);
        db.open();

        tasksRecyclerView = findViewById(R.id.tasksRecycleView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, CalendarTask.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        // Define helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTask();
        tasksAdapter.setTasks(taskList);

        //###### Task End ######

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