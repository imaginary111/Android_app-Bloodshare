package com.blackpearl.bloodlines;

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
public class EventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequiredMethods required;
    private ParseGeoPoint geoPoint;
    private List<ParseObject> result;
    private ImageView noEvent;
    private com.rey.material.widget.ProgressView progress;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_events);
        required = new RequiredMethods();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_recycler_events); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_events);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        noEvent = (ImageView) findViewById(R.id.no_event_image);
        progress = (com.rey.material.widget.ProgressView) findViewById(R.id.event_progress);
        geoPoint = ParseUser.getCurrentUser().getParseGeoPoint("location");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereWithinKilometers("locationPoint", geoPoint, 500);
        query.whereNear("locationPoint", geoPoint);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.d("query", String.valueOf(parseObjects.size()));
                if (e == null&&parseObjects.size()!=0) {
                    result = parseObjects;
                    recyclerView.setVisibility(View.VISIBLE);
                    EventAdapter adapter = new EventAdapter(result,geoPoint,getBaseContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
//                                    Gson gson = new Gson();
//                                    String details = gson.toJson(result.get(position));
//                                    Log.d("recy", details);
//                                    Intent myIntent = new Intent(EventsActivity.this, MainEventActivity.class);
//                                    myIntent.putExtra("EventObjectId",result.get(position).getObjectId());
//                                    startActivity(myIntent);
                                }
                            })
                    );
                } else {

                    noEvent.setVisibility(View.VISIBLE);
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
