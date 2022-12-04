package com.example.timage;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.timage.Adapter.MyGridAdapter;
import com.example.timage.Model.CalendarModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendar extends LinearLayout {

    ImageButton nextBtn, prevBtn;
    TextView curDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat taskDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;

    List<Date> dates = new ArrayList<>();
    List<CalendarModel> dayList = new ArrayList<>();

    public CustomCalendar(Context context) {
        super(context);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        InitializeLayout();

        prevBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_task_layout, null);
                EditText taskName = addView.findViewById(R.id.task_name);
                TextView taskTime = addView.findViewById(R.id.eventTime);
                ImageButton setTime = addView.findViewById(R.id.setTasktime);
                Button addTask = addView.findViewById(R.id.addTask);

                setTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String task_Time = hformat.format(c.getTime());
                                taskTime.setText(task_Time);

                            }
                        }, hours, minute, false);
                        timePickerDialog.show();
                    }
                });
                final String date = taskDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                addTask.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveTask(taskName.getText().toString(), taskTime.getText().toString(), date,month,year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveTask(String task, String time, String date, String month, String year) {

        TimageManager db = new TimageManager(context);
        db.open();

        db.SaveTask(task,time,date,month,year);
        db.close();
        Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show();

    }

    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        nextBtn = view.findViewById(R.id.nextBtn);
        prevBtn = view.findViewById(R.id.previousBtn);
        curDate = view.findViewById(R.id.curDate);
        gridView = view.findViewById(R.id.gridView);
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        curDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) -1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, - firstDayOfMonth);

        TimageManager db = new TimageManager(context);
        db.open();

        db.getDateTask(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));


        while(dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        myGridAdapter = new MyGridAdapter(context, dates, calendar, dayList);
        gridView.setAdapter(myGridAdapter);

    } // End of SetupCalendar


}
