package com.blackpearl.bloodlines;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.rey.material.widget.Button;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Anubhav on 05/09/15.
 */
public class SearchActivity extends Activity {
    private static JSONArray predsJsonArray;
    private static final String LOG_TAG = "Google";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBqqPjGzyOLQWg92SgqO5iZp7GMaW38sNc";
    private static JSONObject JsonArraya = null;
    private Button currentlocation;
    private ImageButton goback;
    private ImageButton delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.search_act_autoCompleteTextView);
        currentlocation = (Button) findViewById(R.id.search_act_current_loc);
        delete = (ImageButton) findViewById(R.id.delete_text);
        goback = (ImageButton) findViewById(R.id.search_act_go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompView.setText("");
            }
        });
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SearchActivity.this, SearchResponse.class);
                myIntent.putExtra("Int_Sea_Act_loc_cord","{\"lat\":30.908888,\"lng\":75.849463}");
                myIntent.putExtra("Int_Sea_Act_loc_name","Ludhiana");
                if(getCurrentFocus()!=null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.google_places_autcom));
        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, final View view, int position, long id) {
                String str = (String) adapterView.getItemAtPosition(position);
                try {
                    final String place_id = (String) predsJsonArray.getJSONObject(position).getString("place_id");
                    final String name =(String) predsJsonArray.getJSONObject(position).getString("description");
                    Log.d("place", place_id);
                    new Thread() {
                        @Override
                        public void run() {
                            HttpURLConnection conn = null;
                            StringBuilder jsonResults = new StringBuilder();
                            try {
                                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
                                sb.append("?key=" + API_KEY);
                                sb.append("&placeid=" + place_id);
                                URL url = new URL(sb.toString());
                                conn = (HttpURLConnection) url.openConnection();
                                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                                // Load the results into a StringBuilder
                                int read;
                                char[] buff = new char[1024];
                                while ((read = in.read(buff)) != -1) {
                                    jsonResults.append(buff, 0, read);
                                }
                            } catch (MalformedURLException e) {
                                Log.e(LOG_TAG, "Error processing Places API URL", e);

                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Error connecting to Places API", e);

                            }
                            JSONObject jsonObjnew = null;
                            try {
                                jsonObjnew = new JSONObject(jsonResults.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                JsonArraya = jsonObjnew.getJSONObject("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                Log.d("place", JsonArraya.getJSONObject("geometry").getString("location"));
                                Intent myIntent = new Intent(SearchActivity.this, SearchResponse.class);
                                myIntent.putExtra("Int_Sea_Act_loc_cord",JsonArraya.getJSONObject("geometry").getString("location"));
                                myIntent.putExtra("Int_Sea_Act_loc_name",name);
                                if(getCurrentFocus()!=null) {
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                }
                                startActivity(myIntent);
                                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
        @Override
        public int getCount() {
            return resultList.size();
        }
        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}