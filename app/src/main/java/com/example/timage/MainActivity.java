package com.example.timage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Locationlistener {

    TextView txtGreeting, txtWeather, txtTemp, txtTotalTasks, txtTasksToday;
    ImageView weatherIcon;
    Button btn1, btn2;

    private final String url = "http://api.openweathermap.org/data/2.5/weather?";
    private final String appid = "";    // Please refer to report
    DecimalFormat df = new DecimalFormat("#.##");
 
    public String icon;
    public String iconName;

    private long minTime = 500;
    private float minDistance = 1;
    private static final int MY_PERMISSION_GPS = 1;
    public LocationManager locationManager;
    public String lat = "";
    public String lon = "";

    public List<Address> addresses;
    public String tempUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGreeting = findViewById(R.id.txtGreeting);
        txtWeather = findViewById(R.id.txtWeather);

        txtTemp = findViewById(R.id.txtTemp);
        weatherIcon = (ImageView) findViewById(R.id.icon);

        txtTotalTasks = findViewById(R.id.txtTotalTasks);
        txtTasksToday = findViewById(R.id.txtTasksToday);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        // Runtime permissions - Get Access Location
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_GPS);
        } else {
            setUpLocationTracking();
        }

        // Switch to calendar page
        btn1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent myIntent = new Intent(MainActivity.this, CalendarActivity.class);

                        // Push into the stack
                        startActivity(myIntent);
                    }
                }
        );

        // Switch to tasks/calendar page
        btn2.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        Intent myIntent = new Intent(MainActivity.this, TasksCategoriesActivity.class);

                        // Push into the stack
                        startActivity(myIntent);
                    }
                }
        );

        // Get time of day
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        // Set greeting text
        if(timeOfDay >= 0 && timeOfDay < 12) {
            txtGreeting.setText("Good Morning, GForceV2");

        } else if(timeOfDay >= 12 && timeOfDay < 16) {
            txtGreeting.setText("Good Afternoon, GForceV2");

        } else if(timeOfDay >= 16 && timeOfDay < 21) {
            txtGreeting.setText("Good Evening, GForceV2");

        } else if(timeOfDay >= 21 && timeOfDay < 24) {
            txtGreeting.setText("Good Night, GForceV2");

        }   // end elseIf()


        // Display tasks
        txtTotalTasks.setText("Total tasks due: x");
        txtTasksToday.setText("Tasks due today: x");

        
    } // end onCreate

    // TODO: Implement it so that button click isn't required anymore
    public void getWeatherDetails(View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            
            @Override
            public void onResponse(String response) {

                Log.d("response", response); // for logcat

                try {
                    JSONObject jsonResponse = new JSONObject(response); // Convert response to a JSON object

                    JSONArray jsonArray = jsonResponse.getJSONArray("weather"); // Get array from JSON object response
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);      
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main"); // pass object name we are looking for

                    // Get key-value pairs
                    String main = jsonObjectWeather.getString("main"); // Main weather description
					String cloudDescription = jsonObjectWeather.getString("description"); // Cloud description
                    String icon = jsonObjectWeather.getString("icon");

                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;

                    txtWeather.setText(main);
                    txtTemp.setText(df.format(temp) + " °C" + "feels like " + df.format(feelsLike) + " °C");

                    // Dynamically get weather Icon
                    iconName = "@drawable/w" + icon;
                    int imageResource = getResources().getIdentifier(iconName, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);

					// Set weather icon
                    weatherIcon.setImageDrawable(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }   // end onResponse

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            } // end onErrorResponse()
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    } // end getWeatherDetails()

    @SuppressLint("MissingPermission")
    //Location Manager - Getting Lat & Long of current location
    private void setUpLocationTracking() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Geocoder class to transform any location/address to long & lat
    public void onLocationChanged(Location location) {

        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

            //Address class for fetching address, country ect
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Log.i("Test", Integer.toString(addresses.size()));
            if(addresses.size() >0) {

                lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());

                tempUrl = url + "lat=" + lat + "&lon=" + lon + "&appid=" + appid;

                Toast.makeText(this, tempUrl, Toast.LENGTH_LONG).show();
            }   // end if

        } catch (Exception e){
            Log.i("Catch", "Here");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.i("Catch", exceptionAsString);
        }
    } // end onLocationChanged()

    // this is a callback method associated with the user having entered in their permission -
    // the compiler will prompt you to add this call back method, when you put in the code for the permission check earlier.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_GPS:
                //If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(this, "All Good!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You need to switch on permissions!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    } // end onRequestPermissionResult

} // end MainActivity()