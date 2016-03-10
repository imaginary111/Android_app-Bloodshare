package com.blackpearl.bloodlines;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.PersonViewHolder>{
    List<ParseUser> result;
    ParseGeoPoint location;
    Context context;
    ListViewAdapter(List<ParseUser> result,ParseGeoPoint location,Context context){
        this.result = result;
        this.location = location;
        this.context =context;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_donor, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int i) {
        double loc = Math.round(result.get(i).getParseGeoPoint("location").distanceInKilometersTo(location)*10)/10;
        holder.personName.setText(result.get(i).get("name").toString());
        holder.distance.setText(String.valueOf(loc)+" km");
        holder.bloodGroup.setText(result.get(i).get("bloodGroup").toString());
        holder.sex.setText(result.get(i).get("sex").toString());
        holder.city.setText(result.get(i).get("city").toString());
        Picasso.with(context).load(result.get(i).getParseFile("displayPic").getUrl()).into(holder.personPhoto);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView personName;
        TextView distance;
        TextView bloodGroup;
        TextView sex;
        TextView city;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.act_donor_cardview);
            personName = (TextView)itemView.findViewById(R.id.cardview_donor_name);
            distance = (TextView)itemView.findViewById(R.id.cardview_donor_distance);
            bloodGroup = (TextView)itemView.findViewById(R.id.cardview_donor_bld_grp);
            sex = (TextView)itemView.findViewById(R.id.cardview_donor_sex_card);
            city = (TextView) itemView.findViewById(R.id.cardview_donor_city);
            personPhoto = (ImageView)itemView.findViewById(R.id.cardview_donor_pro_pic);
        }
        @Override
        public void onClick(View v) {
        }
    }

}
