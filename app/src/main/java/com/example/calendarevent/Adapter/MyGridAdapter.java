package com.example.calendarevent.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assignment.Model.CalendarModel;
import com.example.assignment.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter {

    List<Date> dates;
    Calendar curDate;
    List<CalendarModel> tasks;
    LayoutInflater inflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar curDate, List<CalendarModel> tasks) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.curDate = curDate;
        this.tasks = tasks;
        inflater = LayoutInflater.from(context);
    } // End of gridAdapter

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int curMonth = curDate.get(Calendar.MONTH) + 1;
        int curYear = curDate.get(Calendar.YEAR);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if(displayMonth == curMonth && displayYear == curYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.turqouis));
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView dayNum = view.findViewById(R.id.day);
        dayNum.setText(String.valueOf(dayNo));

        return view;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
