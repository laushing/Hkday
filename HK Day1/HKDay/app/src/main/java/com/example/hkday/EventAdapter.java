package com.example.hkday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<EventData> events;
    private Object context;

    public EventAdapter(List<EventData> events) {
        this.events = events;
    }

    public void updateEvents(List<EventData> newEvents) { this.events = newEvents; notifyDataSetChanged();}

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventSummary;
        public TextView eventDate;
        public TextView eventDescription;
        public TextView eventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventSummary = itemView.findViewById(R.id.eventSummary);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventName = itemView.findViewById(R.id.eventName);
        }
    }

    public void removeEvent(int index) {
        events.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        context = parent.getContext();

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventData event = events.get(position);
        holder.eventName.setText(event.eventName);
        holder.eventSummary.setText(event.summary);

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dataFormat.format(new Date(event.eventDate));
        holder.eventDate.setText(formattedDate);
        holder.eventDescription.setText(event.eventDes);
        
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof Calendar_cell) {
                ((Calendar_cell) context).selectEvent(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }
}

