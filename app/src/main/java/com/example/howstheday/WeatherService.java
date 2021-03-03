package com.example.howstheday;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherService {

    public static final String HTTPS_WWW_METAWEATHER_COM_API_LOCATION_SEARCH_QUERY = "https://www.metaweather.com/api/location/search/?query=";
    public static final String HTTPS_WWW_METAWEATHER_COM_API_WEATEHR_REPORT_LIST = "https://www.metaweather.com/api/location/";
    public  String id;
    Context context;

    public WeatherService(Context context) {
        this.context = context;
    }

    public interface volleyResponder{
        void onError(String message);
        void onResponse(String id);
    }

    public void getId (String cityName, volleyResponder volleyResponder){
        String url = HTTPS_WWW_METAWEATHER_COM_API_LOCATION_SEARCH_QUERY + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                id = "";
                try {
                    JSONObject info = response.getJSONObject(0);
                    id = info.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, "CityId is " + id, Toast.LENGTH_SHORT).show();
                volleyResponder.onResponse(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                volleyResponder.onError("Error");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface WeatherForecast{
        void onError(String message);
        void onResponse(List<WeatherReport> weatherReports);
    }
    public void getWeatherReportById(String cityId, WeatherForecast weatherForecast) {
        List<WeatherReport> weatherReports = new ArrayList<>();
        String url = HTTPS_WWW_METAWEATHER_COM_API_WEATEHR_REPORT_LIST + cityId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

                    for(int i=0; i<consolidated_weather_list.length(); i++) {

                        WeatherReport oneDay_report = new WeatherReport();
                        JSONObject f_day_list = (JSONObject) consolidated_weather_list.get(i);
                        oneDay_report.setId(f_day_list.getInt("id"));
                        oneDay_report.setWeather_state_name(f_day_list.getString("weather_state_name"));
                        oneDay_report.setWeather_state_abbr(f_day_list.getString("weather_state_abbr"));
                        oneDay_report.setWind_direction_compass(f_day_list.getString("wind_direction_compass"));
                        oneDay_report.setCreated(f_day_list.getString("created"));
                        oneDay_report.setApplicable_date(f_day_list.getString("applicable_date"));
                        oneDay_report.setMin_temp(f_day_list.getLong("min_temp"));
                        oneDay_report.setMax_temp(f_day_list.getLong("max_temp"));
                        oneDay_report.setThe_temp(f_day_list.getLong("the_temp"));
                        oneDay_report.setWind_speed(f_day_list.getLong("wind_speed"));
                        oneDay_report.setWind_direction(f_day_list.getLong("wind_direction"));
                        oneDay_report.setAir_pressure(f_day_list.getInt("air_pressure"));
                        oneDay_report.setHumidity(f_day_list.getInt("humidity"));
                        oneDay_report.setVisibility(f_day_list.getLong("visibility"));
                        oneDay_report.setPredictability(f_day_list.getInt("predictability"));
                        weatherReports.add(oneDay_report);
                    }

                    weatherForecast.onResponse(weatherReports);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherForecast.onError("Error");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
