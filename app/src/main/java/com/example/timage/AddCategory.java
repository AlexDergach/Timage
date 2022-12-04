/*
    Developed by David Davitashvili - C20406272
 */

package com.example.timage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddCategory extends AppCompatActivity implements View.OnClickListener
{
    private EditText inputCategory;
    private Button addCategoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        inputCategory = (EditText) findViewById(R.id.input);
        addCategoryBtn = (Button) findViewById(R.id.button);

        addCategoryBtn.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        String inputtedCategory = inputCategory.getText().toString();

        // Reference: https://www.geeksforgeeks.org/how-to-create-an-alert-dialog-box-in-android/
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCategory.this);

        builder.setMessage("Add '" + inputtedCategory + "' to your lists?");
        builder.setTitle("Add Category");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

            TimageManager db = new TimageManager(this);

            db.open();
            db.insertCategory(inputtedCategory);
            db.close();

            finish();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Reference completed

    }
}
