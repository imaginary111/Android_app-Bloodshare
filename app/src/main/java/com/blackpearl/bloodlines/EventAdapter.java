package com.blackpearl.bloodlines;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.PersonViewHolder>{
    List<ParseObject> result;
    ParseGeoPoint location;
    Context context;
    EventAdapter(List<ParseObject> result,ParseGeoPoint location,Context context){
        this.result = result;
        this.location = location;
        this.context =context;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_events, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        double loc = Math.round(result.get(i).getParseGeoPoint("locationPoint").distanceInKilometersTo(location)*10)/10;
        holder.eventOrganiser.setText(result.get(i).get("organiser").toString());
        holder.date.setText(result.get(i).getDate("date").toString());
        holder.locationArea.setText(result.get(i).get("location").toString());
        holder.distance.setText(String.valueOf(loc)+" km");
        Picasso.with(context).load(result.get(i).getParseFile("displayPic").getUrl()).into(holder.event_img);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView eventOrganiser;
        TextView locationArea;
        TextView date;
        TextView distance;
        ImageView event_img;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.act_event_cardview);
            eventOrganiser = (TextView)itemView.findViewById(R.id.cardview_event_organiser);
            locationArea = (TextView)itemView.findViewById(R.id.cardview_event_location);
            date = (TextView)itemView.findViewById(R.id.cardview_event_eventdate);
            distance = (TextView) itemView.findViewById(R.id.cardview_event_distance);
            event_img = (ImageView) itemView.findViewById(R.id.cardview_event_pro_pic);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
