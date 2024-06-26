package com.example.hkday;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class temperatureip extends AppCompatActivity {

    private List<WeatherData> weatherDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptime);

        String url = "https://rthk9.rthk.hk/weather/index_e.htm";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Document doc = Jsoup.parse(response);
                        Elements weatherItems = doc.select(".item_en");

                        for (Element item : weatherItems) {
                            String districtName = item.select(".dname").text();
                            String temperature = item.select(".dtemp").text();
                            weatherDataList.add(new WeatherData(districtName, temperature));
                        }

                       // ListView listView = findViewById(R.id.WeatherData);
                      //  WeatherDataAdapter adapter = new WeatherDataAdapter(temperatureip.this, weatherDataList);
                       // listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }

    private static class WeatherData {
        String districtName;
        String temperature;

        WeatherData(String districtName, String temperature) {
            this.districtName = districtName;
            this.temperature = temperature;
        }
    }

    private static class WeatherDataAdapter extends ArrayAdapter<WeatherData> {
        private Context context;
        private List<WeatherData> weatherDataList;

        public WeatherDataAdapter(Context context, List<WeatherData> weatherDataList) {
            super(context, 0, weatherDataList);
            this.context = context;
            this.weatherDataList = weatherDataList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
            }

            TextView districtNameTextView = convertView.findViewById(R.id.districtName);
            TextView temperatureTextView = convertView.findViewById(R.id.temperature);

            WeatherData weatherData = weatherDataList.get(position);
            districtNameTextView.setText(weatherData.districtName);
            temperatureTextView.setText(weatherData.temperature);

            return convertView;
        }
    }
}