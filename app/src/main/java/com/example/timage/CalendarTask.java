/**
 * Developed by: Jaycel Estrellado - C20372876
 * 
 * References: 
 * Youtube Link: https://www.youtube.com/watch?v=ASQIvPwQffg&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136&index=2
 * Youtube Link: https://www.youtube.com/watch?v=L2datMyLGHU&t=2014s
 */

package com.example.timage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.timage.Adapter.ToDoAdapter;
import com.example.timage.Model.ToDoModel;

import java.util.List;
import java.util.Objects;

public class CalendarTask extends AppCompatActivity {

    /**
     * Developed by Jaycel Estrellado - C20372876
     */

    // Calendar variables
    CustomCalendar customCalendar;

    // Task variables
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;

    private List<ToDoModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_task);

        //####### Calendar Start ########
        customCalendar = (CustomCalendar)findViewById(R.id.custom_calendar);
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

}