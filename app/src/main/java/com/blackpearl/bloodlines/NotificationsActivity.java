package com.blackpearl.bloodlines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequiredMethods required;
    private ParseGeoPoint geoPoint;
    private List<ParseObject> result;
    private ImageView noNotification;
    private com.rey.material.widget.ProgressView progress;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_notifications);
        required = new RequiredMethods();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_recycler_notifications); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notifications);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        progress = (com.rey.material.widget.ProgressView) findViewById(R.id.notification_progress);
        geoPoint = ParseUser.getCurrentUser().getParseGeoPoint("location");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BloodRequests");
//        query.whereEqualTo("ToUser",ParseUser.getCurrentUser().getString("email"));
        query.whereWithinKilometers("location", geoPoint, 5000);
        query.whereNear("location", geoPoint);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.d("query", String.valueOf(parseObjects.size()));
                if (e == null&&parseObjects.size()!=0) {
                    result = parseObjects;
                    Log.d("notif",result.get(0).getString("bloodGroup"));
                    NotificationAdapter adapter = new NotificationAdapter(result,geoPoint,getBaseContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
//                                    Gson gson = new Gson();
//                                    String details = gson.toJson(result.get(position));
//                                    Intent myIntent = new Intent(NotificationsActivity.this, MainNotificationActivity.class);
//                                    myIntent.putExtra("EventObjectId",result.get(position).getObjectId());
//                                    startActivity(myIntent);
                                }
                            })
                    );
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    noNotification = (ImageView) findViewById(R.id.no_notification_image);
                    noNotification.setVisibility(View.VISIBLE);
                    Log.d("query","Object retrieval failed");
                }

                progress.setVisibility(View.GONE);
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
