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
        myT = findViewById(R.id.text);


        imageView = findViewById(R.id.imageone);
        imageView.setAlpha(17);

        imageView2 = findViewById(R.id.imagetwo);


        String url = "https://cdn.discordapp.com/attachments/593568061924179968/1048798479482355782/Outline.png";
        String url2 = "https://cdn.discordapp.com/attachments/593568061924179968/1048796707539914904/Project.png";

        Picasso.with(this).load(url).into(imageView);
        Picasso.with(this).load(url2).into(imageView2);

        calculateProgressFlame(0,15);


        accelerometer.setListener(new Accelerometer.Listener(){
            @Override
            public void onTranslation(float tx, float ty, float tz){
                if(tx > 1.0f){
                    Techniques s = Techniques.Shake;
                    animateImage(s);
                }else if (tx < -1.0f){
                    Techniques s = Techniques.Shake;
                    animateImage(s);

                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
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

    private void animateImage(Techniques s) {
            YoYo.with(s)
                    .duration(700)
                    .repeat(1)
                    .playOn(myT);
    }

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