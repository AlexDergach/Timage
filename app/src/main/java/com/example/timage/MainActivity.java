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

    //Variables
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    private int taskComplete;
    private int taskTotal;

    ImageView imageView;
    ImageView imageView2;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);

        imageView = findViewById(R.id.imageone);
        //Change opacity
        imageView.setAlpha(17);

        imageView2 = findViewById(R.id.imagetwo);

        //Custom created image links
        String url = "https://cdn.discordapp.com/attachments/593568061924179968/1048798479482355782/Outline.png";
        String url2 = "https://cdn.discordapp.com/attachments/593568061924179968/1049040464285794304/Project.png";

        //Picasso library to load the url's
        Picasso.with(this).load(url).into(imageView);
        Picasso.with(this).load(url2).into(imageView2);

        //Method for calculating the size to the tasks
        calculateProgressFlame(10,15);

        //LISTENERS FOR ACCELEROMETER AND GYROSCOPE

        accelerometer.setListener(new Accelerometer.Listener(){
            @Override
            //On movement on X axis run animations
            public void onTranslation(float tx, float ty, float tz){
                if(tx > 1.0f){
                    Techniques s = Techniques.Shake;
                    animateImage(s);
                }else if (tx < -1.0f){
                    Techniques s = Techniques.Shake;
                    animateImage(s);

                }else if (ty < -1.0f){
                    Techniques s = Techniques.Bounce;
                    animateImage(s);

                }else if (ty > 1.0f){
                    Techniques s = Techniques.Bounce;
                    animateImage(s);

                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            //Change flame colors on rotation
            public void onRotation(float rx, float ry, float rz) {
                //Straight - Left Right Rotation
                if(rz > 1.0f){
                    imageView2.setColorFilter(Color.CYAN);
                }else if (rz < -1.0f){
                    imageView2.setColorFilter(Color.WHITE);

                //Rotation - Left Right Rotation
                }else if(ry > 1.0f){
                    imageView2.setColorFilter(Color.BLACK);
                }else if(ry < -1.0f){
                    imageView2.setColorFilter(Color.MAGENTA);
                }

                //Up Down Rotation
                else if(rx > 1.0f){
                    imageView2.setColorFilter(Color.YELLOW);
                }else if(rx < -1.0f){
                    imageView2.setColorFilter(Color.GREEN);
                }
            }
        });

    }

    //Animating parameters
    private void animateImage(Techniques s) {
            YoYo.with(s)
                    .duration(700)
                    .repeat(1)
                    .playOn(imageView2);
    }

    //Method to grow the flame according to the amount of tasks completed
    private void calculateProgressFlame(int taskComplete, int taskTotal){

        int minH = 275;
        int minW = 50;
        int maxH = 1675;
        int maxW = 1450;
        int sizeincreaseW;
        int sizeincreaseH;

        sizeincreaseW  = maxW / taskTotal;
        sizeincreaseH  = maxH / taskTotal;


        if(taskComplete == 0){
            imageView2.requestLayout();

            imageView2.getLayoutParams().height = minH;
            imageView2.getLayoutParams().width = minW;

        }
        else if(taskComplete <= taskTotal){
            imageView2.requestLayout();

            sizeincreaseW = sizeincreaseW * taskComplete;
            sizeincreaseH = sizeincreaseH * taskComplete;

                imageView2.getLayoutParams().height = sizeincreaseH;
                imageView2.getLayoutParams().width = sizeincreaseW;
        }
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