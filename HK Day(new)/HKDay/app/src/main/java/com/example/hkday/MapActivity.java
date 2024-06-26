package com.example.hkday;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class MapActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private ImageView userlocation, userloctionblue;
    private ImageButton maplocation, maplocation1, maplocation2, maplocation3, maplocation4, maplocation5, maplocation6;
    private ImageButton maplocation7, maplocation8, maplocation9, maplocation10, maplocation11, maplocation12, maplocation13, maplocation14;
    private ImageButton MyMap;
    private ImageButton MapPicture;
    private Button MapPlan;
    private Button buttonInt;
    private ImageView Chat;
    private ImageView temperatureicon;
    private TextView Imagetitle, maptime;
    private TextView MapInt;
    private String maptimeurl, temperatureurl;
    private Map<String, Integer> locationIdMap;
    private final String TAG = "MainActivity";

    private Animation chatboxnoshowAnimation;
    private Animation chatboxshoxAnimation;

    private ZoomControls zoomControls;
    private float currentZoomLevel = 1.0f;
    private static final double HK_LATITUDE = 22.3193;
    private static final double HK_LONGITUDE = 114.1694;

    // GPS coordinates for the marker
    private static final double MARKER_LATITUDE = 22.290001448712506;
    private static final double MARKER_LONGITUDE = 114.17149876953127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.map_activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


        userlocation = findViewById(R.id.userlocation);
        userloctionblue = findViewById(R.id.userlocationblue);


        MyMap = findViewById(R.id.MyMap);
        MapPicture = findViewById(R.id.MapPicture);
        MapPlan = findViewById(R.id.MapPlan);
        buttonInt = findViewById(R.id.buttonInt);
        Chat = findViewById(R.id.Chat);
        Imagetitle = findViewById(R.id.Imagetitle);
        MapInt = findViewById(R.id.MapInt);
        temperatureicon = findViewById(R.id.temperature_icon);
        maptime = findViewById(R.id.maptime);
        maptimeurl = "http://worldtimeapi.org/api/timezone/Asia/Hong_Kong";

        maplocation1 = findViewById(R.id.maplocation1);
        maplocation2 = findViewById(R.id.maplocation2);
        maplocation3 = findViewById(R.id.maplocation3);
        maplocation4 = findViewById(R.id.maplocation4);
        maplocation5 = findViewById(R.id.maplocation5);
        maplocation6 = findViewById(R.id.maplocation6);
        maplocation7 = findViewById(R.id.maplocation7);
        maplocation8 = findViewById(R.id.maplocation8);
        maplocation9 = findViewById(R.id.maplocation9);
        maplocation10 = findViewById(R.id.maplocation10);
        maplocation11 = findViewById(R.id.maplocation11);
        maplocation12 = findViewById(R.id.maplocation12);
        maplocation13 = findViewById(R.id.maplocation13);
        maplocation14 = findViewById(R.id.maplocation14);

        locationIdMap = new HashMap<>();
        locationIdMap.put("KOWLOON CITY", R.id.kowlooncity);
        locationIdMap.put("HONG KONG OBSERVATORY", R.id.happyvalley);
        locationIdMap.put("TSING YI", R.id.ngongping);
        locationIdMap.put("SAI KUNG", R.id.paktamchung);
        locationIdMap.put("SHA TIN", R.id.sheungshui);
        locationIdMap.put("TUEN MUN", R.id.tuen_mun);
        String url = "https://rthk9.rthk.hk/weather/index_e.htm";
        String mapurl = "http://10.0.2.2:8080/osmad/myphp/map_json.php";
        zoomControls = findViewById(R.id.zoomControls);
        setUpClickListener(maplocation1, mapurl);
        setUpClickListener(maplocation2, mapurl);
        setUpClickListener(maplocation3, mapurl);
        setUpClickListener(maplocation4, mapurl);
        setUpClickListener(maplocation5, mapurl);
        setUpClickListener(maplocation6, mapurl);
        setUpClickListener(maplocation7, mapurl);
        setUpClickListener(maplocation8, mapurl);
        setUpClickListener(maplocation9, mapurl);
        setUpClickListener(maplocation10, mapurl);
        setUpClickListener(maplocation11, mapurl);
        setUpClickListener(maplocation12, mapurl);
        setUpClickListener(maplocation13, mapurl);
        setUpClickListener(maplocation14, mapurl);

        RequestQueue queue = Volley.newRequestQueue(this);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentZoomLevel += 0.1f;
                MyMap.setScaleX(currentZoomLevel);
                MyMap.setScaleY(currentZoomLevel);
            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentZoomLevel > 1.0f) {
                    currentZoomLevel -= 0.1f;
                    MyMap.setScaleX(currentZoomLevel);
                    MyMap.setScaleY(currentZoomLevel);
                }
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response received");
                        Document doc = Jsoup.parse(response);
                        Elements weatherItems = doc.select(".item_en");

                        for (Element item : weatherItems) {
                            String districtName = item.select(".dname").text();
                            String temperatureText = item.select(".dtemp").text();
                            Log.d(TAG, "Processing district: " + districtName + " with temperature: " + temperatureText);

                            try {
                                double temperature = Double.parseDouble(temperatureText.replaceAll("[^\\d.]", ""));
                                if (locationIdMap.containsKey(districtName)) {
                                    int imageViewId = locationIdMap.get(districtName);
                                    ImageView imageView = findViewById(imageViewId);
                                    if (temperature >= 30) {
                                        imageView.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                                        Log.d(TAG, districtName + " set to RED");
                                    } else if (temperature >= 27 && temperature <= 29) {
                                        imageView.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                                        Log.d(TAG, districtName + " set to YELLOW");
                                    } else if (temperature >= 20 && temperature <= 26) {
                                        imageView.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                                        Log.d(TAG, districtName + " set to GREEN");
                                    } else if (temperature <= 19) {
                                        imageView.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                                        Log.d(TAG, districtName + " set to BLUE");
                                    }
                                } else {
                                    Log.d(TAG, "District not found in map: " + districtName);
                                }
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Error parsing temperature for " + districtName, e);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching weather data", error);
            }
        }
        );

        queue.add(stringRequest);

        final AtomicBoolean isMapVisible = new AtomicBoolean(true);

        temperatureicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMapVisible.get()) {
                    for (int id : locationIdMap.values()) {
                        findViewById(id).animate().alpha(0).setDuration(200);
                    }
                    isMapVisible.set(false);
                    Log.d(TAG, "Map icons hidden");
                } else {
                    for (int id : locationIdMap.values()) {
                        findViewById(id).animate().alpha(0.3f).setDuration(200);
                    }
                    isMapVisible.set(true);
                    Log.d(TAG, "Map icons shown");
                }
            }
        });


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, maptimeurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String datetime = response.getString("datetime");
                    String mapTime = datetime.split("T")[0];
                    maptime.setText(mapTime);
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        Volley.newRequestQueue(this).add(request);
        chatboxnoshowAnimation = AnimationUtils.loadAnimation(this, R.anim.chatboxnoshow);
        chatboxshoxAnimation = AnimationUtils.loadAnimation(this, R.anim.chatboxshox);

        userlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animatorBigX = ObjectAnimator.ofFloat(userloctionblue, "scaleX", 12f);
                ObjectAnimator animatorBigY = ObjectAnimator.ofFloat(userloctionblue, "scaleY", 12f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorBigX, animatorBigY);
                animatorSet.setDuration(1000);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator reverseAnimatorX = ObjectAnimator.ofFloat(userloctionblue, "scaleX", 0.1f);
                        ObjectAnimator reverseAnimatorY = ObjectAnimator.ofFloat(userloctionblue, "scaleY", 0.1f);
                        AnimatorSet reverseAnimatorSet = new AnimatorSet();
                        reverseAnimatorSet.playTogether(reverseAnimatorX, reverseAnimatorY);
                        reverseAnimatorSet.setDuration(1000);
                        reverseAnimatorSet.start();
                    }
                });

                animatorSet.start();
            }
        });

        MyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatboxnoshowAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        MapPicture.setVisibility(View.INVISIBLE);
                        Chat.setVisibility(View.INVISIBLE);
                        Imagetitle.setVisibility(View.INVISIBLE);
                        MapInt.setVisibility(View.INVISIBLE);
                        MapPlan.setVisibility(View.INVISIBLE);
                        buttonInt.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                MapPicture.startAnimation(chatboxnoshowAnimation);
                Chat.startAnimation(chatboxnoshowAnimation);
                Imagetitle.startAnimation(chatboxnoshowAnimation);
                MapInt.startAnimation(chatboxnoshowAnimation);
                MapPlan.startAnimation(chatboxnoshowAnimation);
                buttonInt.startAnimation(chatboxnoshowAnimation);
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        setUserLocationOnMap(latitude, longitude);
                    }
                });
    }

    private void setUserLocationOnMap(double latitude, double longitude) {
        double centerLatitude = latitude - 10;
        double centerLongitude = longitude - 10;
        int x = (int) (centerLatitude * -50);
        int y = (int) (centerLongitude * 6.4);
        ImageView userLocationButton = findViewById(R.id.userlocation);
        setXYPosition(userLocationButton, x, y);
    }

    private void setXYPosition(View view, int x, int y) {
        ConstraintLayout layout = (ConstraintLayout) view.getParent();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, x);
        constraintSet.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, y);

        constraintSet.applyTo(layout);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }


    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    private void setUpClickListener(View mapLocationView, String url) {
        mapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "Response received: " + response.toString());
                            JSONArray dataArray = response.getJSONArray("map");
                            Random rand = new Random();
                            int randomIndex = rand.nextInt(dataArray.length());
                            JSONObject randomData = dataArray.getJSONObject(randomIndex);

                            String name = randomData.getString("name");
                            String picture = randomData.getString("picture");

                            Imagetitle.setText(name);
                            Picasso.get().load(picture).into(MapPicture);
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing JSON response", e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching data", error);
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
                Log.d(TAG, "Request added to queue");
            }
        });
    }
}



