package com.blackpearl.bloodlines;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseGeoPoint;
import com.rey.material.widget.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anubhav on 05/09/15.
 */
public class SearchResponse extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView addressText;
    private String state;
    private GoogleApiClient mGoogleApiClient;
    private String toolbarText;
    private FrameLayout fragment;
    private ParseGeoPoint geoPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_response);
        geoPoint = new ParseGeoPoint();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        String location;
        if (b != null) {
            toolbarText = (String) b.get("Int_Sea_Act_loc_name");
            location = (String) b.get("Int_Sea_Act_loc_cord");
            try {
                Log.d("location1", location.toString());
                JSONObject locationJson = new JSONObject(location);
                geoPoint.setLatitude((Double) locationJson.get("lat"));
                geoPoint.setLongitude((Double) locationJson.get("lng"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        state = "card";
        fragment = (FrameLayout) findViewById(R.id.search_res_fragment);
        toolbar = (Toolbar) findViewById(R.id.search_res_toolbar);
        addressText = (TextView) findViewById(R.id.search_res_toolbar_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        addressText = (TextView) findViewById(R.id.search_res_toolbar_text);
        addressText.setText(toolbarText);

        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResponse.this, SearchActivity.class));
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        addWithAsync(state);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_res, menu);//Menu Resource, Menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    int id =item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        if(id==R.id.menu_sear_res_home){
            startActivity(new Intent(this, StartingActivity.class));
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if(id==R.id.menu_sear_res_search){
            startActivity(new Intent(this, SearchActivity.class));
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
        if(id==R.id.menu_sear_res_list_view){
            if(state.equals("card")){
                item.setIcon(R.drawable.map_marker_radius);
                removeFragment();
                state ="map";
                addWithAsync(state);
//               addFragment(state);
            }
            else {
                removeFragment();
                item.setIcon(R.drawable.ic_sort_white_24dp);
                state = "card";
                addWithAsync(state);
//               addFragment(state);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }
    private void addFragment(String name){
        Fragment mainFragment;
        if(name.equals("card")) {
            mainFragment = new ListViewFragment();

        }
        else {
            mainFragment = new MapDonorFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.search_res_fragment,mainFragment,"mainFragment").commit();

    }
    private void removeFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("mainFragment")).commit();
    }
    private class AsyncWork extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

        @Override
        protected Void doInBackground(String... params) {
            addFragment(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    private void addWithAsync(String type){
        new AsyncWork().execute(type);
    }
}
