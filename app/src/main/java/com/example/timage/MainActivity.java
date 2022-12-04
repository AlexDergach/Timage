package com.example.timage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private Accelerometer accelerometer;
    private Gyroscope gyroscope;
    private TextView myT;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);
        myT = findViewById(R.id.text);

        ImageView imageView = findViewById(R.id.imageone);
        ImageView imageView2 = findViewById(R.id.imagetwo);


        String url = "https://cdn.discordapp.com/attachments/593568061924179968/1048798479482355782/Outline.png";
        String url2 = "https://cdn.discordapp.com/attachments/593568061924179968/1048796707539914904/Project.png";

        Picasso.with(this).load(url).into(imageView);
        Picasso.with(this).load(url2).into(imageView2);


        accelerometer.setListener(new Accelerometer.Listener(){
            @Override
            public void onTranslation(float tx, float ty, float tz){
                if(tx > 1.0f){
                    animateImage();
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }else if (tx < -1.0f){
                    animateImage();
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if(rz > 1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }else if (rz < -1.0f){
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
        });

    }

    private void animateImage() {
        YoYo.with(Techniques.Bounce)
                .duration(700)
                .repeat(1)
                .playOn(myT);
    }

    protected void onResume(){
        super.onResume();

        accelerometer.register();
        gyroscope.register();
    }

    protected void onPause(){
        super.onPause();

        accelerometer.unregister();
        gyroscope.unregister();
    }
}