/*
    Developed by David Davitashvili - C20406272
 */

package com.example.timage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PageTasks extends AppCompatActivity
{
    // Initialize arrays for adapter
    private int task_columns[] = {R.id.task_name,R.id.task_date,R.id.task_time};
    private String task_qColumns[] = {"task_name","task_date","task_time"};

    // Initialize button and textview
    private Button addTaskBtn;
    private TextView noTasks;

    private long cat_id_taken;

    // onRestart called when coming from an activity branched from this main activity
    // Reference: https://stackoverflow.com/questions/5545217/back-button-and-refreshing-previous-activity
    @Override
    protected void onRestart() {
        super.onRestart();

        // recreate() called to refresh the page, therefore calling onCreate()
        recreate();
    }
    // Reference complete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // separate method for the whole code
        run();

    }

public void run()
    {
        // initialize database
        TimageManager db = new TimageManager(this);

        Intent category_id_intent = getIntent();
        cat_id_taken = category_id_intent.getLongExtra("category_id", cat_id_taken);

        // open database
        db.open();

        db.insertTask("test",22,12,2025,"13:00","test",0,1);
        db.insertTask("test 2",15,6,2023,"17:30","test 2",1,1);

        db.insertTask("test 3",1,3,2024,"01:40","test 3",1,2);
        db.insertTask("test 4",9,1,2022,"08:21","test 4",0,2);


        addTaskBtn = (Button) findViewById(R.id.addTask);
        noTasks = (TextView) findViewById(R.id.noList);

        // listens for the click on the add button
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // new intent to go to the AddCategory page with the category_id as extra
                Intent intent_addTask = new Intent(getApplicationContext(), AddTask.class);
                intent_addTask.putExtra("id of category", cat_id_taken);
                startActivity(intent_addTask);
            }
        });

        // stores all tasks of the stored category_id in a cursor
        Cursor allTasks = db.getAllTasksInCategory(cat_id_taken);


        // if there are rows in the cursor
        if(allTasks.getCount() != 0)
        {
            ListView list = (ListView) findViewById(R.id.list);

            // creates a new adapter and sets it to the list
            SimpleCursorAdapter adapter_task = new SimpleCursorAdapter(this, R.layout.row_task, allTasks, task_qColumns, task_columns);

            list.setAdapter(adapter_task);


            // if user clicks on an item
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    // sets each item's position to another cursor
                    Cursor task_cursor = (Cursor) adapter_task.getItem(position);

                    // sets whatever the string of the position clicked to cat_title
                    String task_name = task_cursor.getString(1);

                    // displays the title of the category entered
                    Toast.makeText(PageTasks.this, task_name, Toast.LENGTH_LONG).show();

                    // loops through the cursor's rows to match the position of the item click
                    for(int i = 0; i <= task_cursor.getCount(); i++)
                    {
                        if (position == i)
                        {
                            // new intent to go to the AddCategory page
                            Intent intent_task = new Intent(getApplicationContext(), PageTasks.class);
                            intent_task.putExtra("id of category", id);
                            startActivity(intent_task);
                        }
                    }
                }
            });

            // if the user long clicks on an item
            // Reference https://stackoverflow.com/questions/41236032/setonitemlongclicklistener-in-listview-android
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                    // sets each item's position to another cursor
                    Cursor task_cursor = (Cursor) adapter_task.getItem(position);

                    // sets whatever the string of the position clicked to cat_title
                    String task_name = task_cursor.getString(1);

                    // displays the title of the category entered
                    Toast.makeText(PageTasks.this, task_name, Toast.LENGTH_LONG).show();

                    // loops through the cursor's rows to match the position of the long item click
                    for(int i = 0; i <= task_cursor.getCount(); i++) {
                        if (position == i) {
                            // Reference: https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/

                            // create a new alertDialog.builder object and set the message and title
                            AlertDialog.Builder builder = new AlertDialog.Builder(PageTasks.this);
                            builder.setMessage("Remove '" + task_name + "' from this category?");
                            builder.setTitle("Remove Task");

                            // if user presses yes
                            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                                //delete the category
                                db.open();
                                db.deleteTask(id);
                                db.close();

                                // go back to onCreate to refresh database
                                recreate();

                            });

                            // if no then it cancels the deletion
                            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                dialog.cancel();
                            });

                            // shows the alertDialog
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            // Reference completed
                        }
                    }

                    return true;
                }
            });
            // Reference completed

            // close database
            db.close();
        }
        else // else let the user know there are no categories
        {
            noTasks.setText("No tasks created");
        }
    }
}
