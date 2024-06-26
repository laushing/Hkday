package com.example.hkday;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Calendar_cell extends AppCompatActivity {

    private RecyclerView eventRecyclerView;
    private CalendarView calendarView;
    private Button cancelButton;
    private Button addButton;
    private long selectedEventDate = 0;
    private List<EventData> events = new ArrayList<>();
    private EventAdapter eventAdapter;
    private int selectedEventIndex = -1;



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar_cell);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        calendarView = findViewById(R.id.calendarView);
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        cancelButton = findViewById(R.id.btnCancel);
        addButton = findViewById(R.id.btnAdd);

        eventAdapter = new EventAdapter(events);
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* Add an event when the add button is clicked */

        addButton.setOnClickListener(v -> {
            Dialog addEventDialog = new Dialog(Calendar_cell.this);
            addEventDialog.setContentView(R.layout.input_prompt);

            EditText EventName = addEventDialog.findViewById(R.id.event_name_input);
            EditText EventSummary = addEventDialog.findViewById(R.id.event_description_input);
            Button saveButton = addEventDialog.findViewById(R.id.dialog_save);
            Button cancelButton = addEventDialog.findViewById(R.id.dialog_cancel);

            /* Store the selected date in the selectedEventDate variable */

            final String url = "http://10.0.2.2/hkday_file/eventDate.php";
            new EventAsyncTask(EventName.getText().toString(), EventSummary.getText().toString(),
                    selectedEventDate).execute(url);

            addEventDialog.show();

            cancelButton.setOnClickListener(view -> addEventDialog.dismiss());

            saveButton.setOnClickListener(view -> {
                String eventName = EventName.getText().toString();
                String eventSummary = EventSummary.getText().toString();
                long eventDate = selectedEventDate;

                    EventData newEvent = new EventData(0,eventName," ",
                            eventDate, eventSummary);
                    events.add(newEvent);
                    eventAdapter.notifyItemInserted(events.size() - 1);
                    addEventDialog.dismiss();

                    final String url1 = "http://10.0.2.2/hkday_file/eventDate.php";
                    new EventAsyncTask(eventName, eventSummary, eventDate).execute(url1);

            });

        });

        /* Remove the selected event when the cancel button is clicked */

        cancelButton.setOnClickListener(v -> {
            if (selectedEventIndex != -1) {
                events.remove(selectedEventIndex);
                eventAdapter.notifyItemRemoved(selectedEventIndex);
                selectedEventIndex = -1;
            }
        });

        /* Store the selected date in the selectedEventDate variable */

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedEventDate = calendar.getTimeInMillis();

            /* Filter events for the selected date and update the event list */

            List<EventData> eventsForSelectedDate = getEventsForDate(year, month, dayOfMonth);
            eventAdapter.events = eventsForSelectedDate;
            eventAdapter.updateEvents(eventsForSelectedDate);
            eventAdapter.notifyDataSetChanged();
        });
    }

        void selectEvent(int index) {
            selectedEventIndex = index;
            eventAdapter.notifyDataSetChanged();
        }

        /* Function to send HTTP POST request to the server */

        public String executeHttpPost (String url, String EventName, String EventSummary, long eventDate)
        throws IOException {

            URL urlobj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)
            urlobj.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("eventName", EventName));
            nameValuePairs.add(new BasicNameValuePair("eventSummary", EventSummary));
            nameValuePairs.add(new BasicNameValuePair("eventDate", String.valueOf(eventDate)));


            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("eventName", EventName);
                jsonParams.put("eventSummary", EventSummary);
                jsonParams.put("date", eventDate);

            } catch (JSONException e) {
                e.printStackTrace();
                return "Error: Failed to create JSON object";
            }

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParams.toString());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())
                );
                String innputLine;
                StringBuilder response = new StringBuilder();
                while ((innputLine = in.readLine()) != null) {
                    response.append(innputLine);
                }
                in.close();
                return response.toString();
            } else {
                return "Error: Failed to connect to server" + responseCode;
            }
    }

        /* AsyncTask class to send HTTP POST request to the server */

        private class EventAsyncTask extends AsyncTask<String, Void, String> {
            private String eventName;
            private String eventSummary;
            private long eventDate = selectedEventDate;

            public EventAsyncTask(String eventName, String eventSummary, long eventDate) {
                this.eventName = eventName;
                this.eventSummary = eventSummary;
                this.eventDate = eventDate;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(Calendar_cell.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... url) {
                try {
                    return Calendar_cell.this.executeHttpPost(url[0], eventName, eventSummary, eventDate);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        /* Function to filter events for the selected date */

        private List<EventData> getEventsForDate(int year, int month, int dayOfMonth) {
            List<EventData> filteredEvents = new ArrayList<>();
            for (EventData event : events) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                long startOfDay = calendar.getTimeInMillis();
                calendar.set(year, month, dayOfMonth, 23, 59,59 );
                long endOfDay = calendar.getTimeInMillis();

                if (event.eventDate >= startOfDay && event.eventDate <= endOfDay) {
                    filteredEvents.add(event);
                }
            }
            return filteredEvents;

        }




    }
