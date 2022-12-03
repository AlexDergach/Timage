package com.example.weatherplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalendarActivity extends AppCompatActivity {

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn1 = (Button)findViewById(R.id.btn1);

        btn1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
    }
}