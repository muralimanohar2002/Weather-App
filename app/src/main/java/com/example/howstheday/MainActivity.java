package com.example.howstheday;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.howstheday.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WeatherService weatherService = new WeatherService(MainActivity.this);
        


        binding.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherService.getId(binding.typeName.getText().toString(), new WeatherService.volleyResponder() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String id) {

                        weatherService.getWeatherReportById(id, new WeatherService.WeatherForecast() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<WeatherReport> weatherReports) {

                                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_color, weatherReports);

                                binding.listView.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });
            }
        });


        binding.getId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Instantiate the RequestQueue.
                //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
               weatherService.getId(binding.typeName.getText().toString(), new WeatherService.volleyResponder() {
                   @Override
                   public void onError(String message) {
                       Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onResponse(String id) {
                       Toast.makeText(MainActivity.this, "Obtained id is " + id, Toast.LENGTH_SHORT).show();
                   }
               });
                //Toast.makeText(MainActivity.this, "It's city ID", Toast.LENGTH_SHORT).show();
            }
        });
        binding.useId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherService.getWeatherReportById(binding.typeName.getText().toString(), new WeatherService.WeatherForecast() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReport> weatherReports) {
                        //Toast.makeText(MainActivity.this, weatherReport.toString(), Toast.LENGTH_SHORT).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_color, weatherReports);

                        binding.listView.setAdapter(arrayAdapter);
                    }
                });
            }
        });

    }
}