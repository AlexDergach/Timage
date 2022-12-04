package com.example.timage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import static java.sql.Types.TIME;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddTask extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;

    private DatePicker datePicker;
    private Button dateButton;
    private Button timeButton;
    private int hour, minute;
    private EditText taskName, desc;
    private TextView Due, time;
    private Button addTask;

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        // initialize database
        TimageManager db = new TimageManager(this);

        Intent category_id_intent = getIntent();
        cat_id_taken = category_id_intent.getLongExtra("category_id", cat_id_taken);

//        initDatePicker();
//        dateButton = findViewById(R.id.datePickerButton);
//        dateButton.setText(getTodaysDate());
//        timeButton = findViewById(R.id.timePickerButton);

        datePicker = (DatePicker) findViewById(R.id.datePickerButton);

        taskName = (EditText) findViewById(R.id.taskName);
        Due = findViewById(R.id.dateDue);
        time = findViewById(R.id.timeDue);
        desc = (EditText) findViewById(R.id.description);

        addTask = (Button) findViewById(R.id.addTaskBtn);

        // open database
        db.open();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below line is to get data from all edit text fields.
                String task_name = taskName.getText().toString();

                int task_date = datePicker.getDayOfMonth();
                int task_month = datePicker.getMonth();
                int task_year = datePicker.getYear();

                String time;

                String task_desc = desc.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(AddTask.this);

                // validating if the text fields are empty or not.
                if (task_name.isEmpty() ||  task_desc.isEmpty()) {
                    Toast.makeText(AddTask.this, "Please enter all the data...", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.


                db.insertTask(task_name, task_date, task_month, task_year, "",task_desc,0, (int) cat_id_taken);

                // after adding the data we are displaying a toast message.
                Toast.makeText(AddTask.this, "Task has been added.", Toast.LENGTH_SHORT).show();

                db.close();

                finish();
            }
        });


    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.BUTTON_NEGATIVE;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void timeClicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        int style = AlertDialog.BUTTON_NEGATIVE;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
        timePickerDialog.closeOptionsMenu();
    }
}