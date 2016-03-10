package com.blackpearl.bloodlines;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.rey.material.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class MainDonorActivity extends AppCompatActivity {
    private ParseQuery<ParseUser> query;
    private List<ParseUser> user = null;
    private TextView name;
    private TextView distance;
    private TextView city;
    private TextView bloodGroup;
    private TextView mobileNo;
    private ImageView proPic;
    private Button requestBlood;
    private ParseObject requestObject;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_donor);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_main_donor); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String userName = null;
        if (b != null) {
            userName = (String) b.get("Int_donor_usr_name");
        }
        query = ParseUser.getQuery();
        query.whereEqualTo("username", userName);
        try {
            user = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        requestObject = new ParseObject("BloodRequests");
        name = (TextView) findViewById(R.id.donor_name);
        distance = (TextView) findViewById(R.id.donor_distance);
        proPic = (ImageView) findViewById(R.id.donor_image);
        city = (TextView) findViewById(R.id.donor_city);
        bloodGroup = (TextView) findViewById(R.id.donor_blood);
        requestBlood = (Button) findViewById(R.id.request_blood);
        name.setText(user.get(0).getString("name"));
        distance.setText(String.valueOf(user.get(0).getParseGeoPoint("location").distanceInKilometersTo(ParseUser.getCurrentUser().getParseGeoPoint("location"))));
        bloodGroup.setText(user.get(0).getString("bloodGroup"));
        city.setText(user.get(0).getString("city"));

        try {
            proPic.setImageBitmap(BitmapFactory.decodeByteArray(user.get(0).getParseFile("displayPic").getData(), 0, user.get(0).getParseFile("displayPic").getData().length));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        requestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestObject.put("ToUser", user.get(0).getUsername());
                requestObject.put("FromUser", ParseUser.getCurrentUser().getUsername());
                requestObject.put("city", ParseUser.getCurrentUser().getString("city"));
                requestObject.put("location", ParseUser.getCurrentUser().getParseGeoPoint("location"));
                requestObject.put("mobileNo", ParseUser.getCurrentUser().get("mobileNo"));
                requestObject.put("name", ParseUser.getCurrentUser().get("name"));
                requestObject.put("sex", ParseUser.getCurrentUser().get("sex"));
                requestObject.put("bloodGroup", ParseUser.getCurrentUser().get("bloodGroup"));
                requestObject.put("modeOfRequest", 0);
                requestObject.saveInBackground();
                Toast.makeText(MainDonorActivity.this, "Blood Requested To the User", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(MainDonorActivity.this, DispatchActivity.class);
                startActivity(myIntent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}