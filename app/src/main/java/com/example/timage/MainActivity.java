package com.example.timage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    TextView tvResult;

    private final String url = "http://api.openweathermap.org/data/3.0/weather";
    private final String url2 = "&exclude=hourly,daily";
    private final String appid = "";    // Please refer to report

    private long minTime = 500;
    private float minDistance = 1;
    private static final int MY_PERMISSION_GPS = 1;
    public LocationManager locationManager;
    public String lat = "";
    public String lon = "";


    public String city;
    public String country = "IE";
    public List<Address> addresses;
    public String tempUrl = "";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        //Runtime permissions - Get Access Location
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_GPS);
        }
        setUpLocationTracking();
    } // end onCreate

    public void getWeatherDetails(View view) {
        Toast.makeText(this, tempUrl, Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response); // for logcat
                String output = "";
                try {
                   JSONObject jsonResponse = new JSONObject(response); // Convert response to a JSON object
                   JSONObject jsonObjectCurrent = jsonResponse.getJSONObject("current");

                   // Get key-value pairs
                   double temp = jsonObjectCurrent.getDouble("temp") - 273.15;
                   double feelsLike = jsonObjectCurrent.getDouble("feels_like") - 273.15;
                   float pressure = jsonObjectCurrent.getInt("pressure");
                   int humidity = jsonObjectCurrent.getInt("humidity");
                   int clouds = jsonObjectCurrent.getInt("clouds");


                   tvResult.setTextColor(Color.rgb(68,134,199));
                   output += "Current weather :"
                           + "\n Temp: " + df.format(temp) + " °C"
                           + "\n Feels Like: " + df.format(feelsLike) + " °C"
                           + "\n Humidity: " + humidity + "%"
                           + "\n Pressure: " + pressure + " hPa"
                           + "\n Clouds: " + clouds + " hPa";

                    
                    tvResult.setText(output);

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
                tempUrl = url + "lat=" + lat + "&lon=" + lon + url2 + "&appid=" + appid;
                
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