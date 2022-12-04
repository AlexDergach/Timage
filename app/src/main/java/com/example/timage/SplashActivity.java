/**
 * Developer: Jaycel Estrellado - C20372876
 */
package com.example.timage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_task);
        getSupportActionBar().hide();

        final Intent i = new Intent(SplashActivity.this, CalendarTask.class);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}
