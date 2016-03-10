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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.PersonViewHolder>{
    private List<ParseObject> result;
    private ParseGeoPoint location;
    private Context context;
    NotificationAdapter(List<ParseObject> result, ParseGeoPoint location, Context context){
        this.result = result;
        this.location = location;
        this.context =context;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_notifications, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int i) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        Log.d("notifc",result.get(i).get("FromUser").toString());
        query.whereEqualTo("username",result.get(i).get("FromUser").toString() );
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    double loc = Math.round(objects.get(0).getParseGeoPoint("location").distanceInKilometersTo(location) * 10)/10;
                    holder.notification_name.setText(objects.get(0).get("name").toString());
                    holder.distance.setText(String.valueOf(loc)+" km");
                    holder.bld_grp.setText(objects.get(0).get("bloodGroup").toString());
                    holder.city.setText(objects.get(0).get("city").toString());
                    Picasso.with(context).load(objects.get(0).getParseFile("displayPic").getUrl()).into(holder.notification_img);
                } else {
                    // Something went wrong.
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView notification_name;
        TextView distance;
        ImageView notification_img;
        TextView city;
        TextView bld_grp;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.act_notification_cardview);
            notification_name = (TextView)itemView.findViewById(R.id.cardview_notification_name);
            distance = (TextView) itemView.findViewById(R.id.cardview_notification_distance);
            notification_img = (ImageView) itemView.findViewById(R.id.cardview_notification_pro_pic);
            city = (TextView) itemView.findViewById(R.id.cardview_notification_city);
            bld_grp = (TextView) itemView.findViewById(R.id.cardview_notification_bld_grp);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
