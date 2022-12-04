/*
    Developed by David Davitashvili - C20406272
 */

package com.example.timage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PageCategories extends AppCompatActivity {

    private Button addCategoryBtn;
    private TextView noList;
    private int cat_columns[] = {R.id.cat_title};
    private String cat_qColumns[] = {"cat_title"};

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
        setContentView(R.layout.activity_category);

        // separate method for the whole code
        run();

    }

    public void run()
    {
        // initialize database and make it writable
        TimageManager db = new TimageManager(this);
        db.open();

        addCategoryBtn = (Button) findViewById(R.id.addCategory);
        noList = (TextView) findViewById(R.id.noList);

        // listens for the click on the add button
        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // new intent to go to the AddCategory page
                Intent intent_addCategory = new Intent(getApplicationContext(), AddCategory.class);
                startActivity(intent_addCategory);
            }
        });


        // stores all categories in a cursor
        Cursor allCategories = db.getAllCategories();

        // if there are rows in the cursor
        if(allCategories.getCount() != 0)
        {
            ListView list = (ListView) findViewById(R.id.list);

            // creates a new adapter and sets it to the list
            SimpleCursorAdapter adapter_cat = new SimpleCursorAdapter(this, R.layout.row_cat, allCategories, cat_qColumns, cat_columns);
            list.setAdapter(adapter_cat);

            // if user clicks on an item
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    // sets each item's position to another cursor
                    Cursor cat_cursor = (Cursor) adapter_cat.getItem(position);

                    // sets whatever the string of the position clicked to cat_title
                    String cat_title = cat_cursor.getString(1);

                    // displays the title of the category entered
                    Toast.makeText(PageCategories.this, cat_title, Toast.LENGTH_LONG).show();

                    // loops through the cursor's rows to match the position of the item click
                    for(int i = 0; i <= cat_cursor.getCount(); i++)
                    {
                        if (position == i)
                        {
                            // new intent to go to the AddCategory page
                            Intent intent_task = new Intent(getApplicationContext(), PageTasks.class);
                            intent_task.putExtra("category_id", id);
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
                    Cursor cat_cursor = (Cursor) adapter_cat.getItem(position);

                    // sets whatever the string of the position clicked to cat_title
                    String cat_title = cat_cursor.getString(1);

                    // displays the title of the category entered
                    Toast.makeText(PageCategories.this, cat_title, Toast.LENGTH_LONG).show();

                    // loops through the cursor's rows to match the position of the long item click
                    for(int i = 0; i <= cat_cursor.getCount(); i++) {
                        if (position == i) {
                            // Reference: https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/

                            // create a new alertDialog.builder object and set the message and title
                            AlertDialog.Builder builder = new AlertDialog.Builder(PageCategories.this);
                            builder.setMessage("Remove '" + cat_title + "' from your lists?");
                            builder.setTitle("Remove List");

                            // if user presses yes
                            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                                //delete the category
                                db.open();
                                db.deleteCategory(id);
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
            noList.setText("No lists created");
        }
    }
}