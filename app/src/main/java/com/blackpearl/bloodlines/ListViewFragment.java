package com.blackpearl.bloodlines;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class ListViewFragment extends Fragment{
    private RecyclerView recyclerview;
    private RequiredMethods required;
    private ParseGeoPoint geoPoint;
    private List<ParseUser> result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recycler_donor, container, false);
        recyclerview = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerview.setLayoutManager(llm);

        required = new RequiredMethods();
        geoPoint = new ParseGeoPoint();

        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();
        String location;
        if(b!=null) {
            location = (String) b.get("Int_Sea_Act_loc_cord");

            try {
                JSONObject locationJson = new JSONObject(location);
                geoPoint.setLatitude((Double) locationJson.get("lat"));
                geoPoint.setLongitude((Double) locationJson.get("lng"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("result", String.valueOf(geoPoint.getLatitude()));
        result = required.defaultQuery(geoPoint);
        Log.d("result", String.valueOf(result.size()));
        if(result.size()!=0){
            ListViewAdapter adapter = new ListViewAdapter(result,geoPoint,getActivity());
            recyclerview.setAdapter(adapter);
            recyclerview.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
//                        Gson gson = new Gson();
//                        String details = gson.toJson(result.get(position));
//                        Log.d("recy", details);
                            Intent myIntent = new Intent(getActivity().getBaseContext(), MainDonorActivity.class);
                            myIntent.putExtra("Int_donor_usr_name",result.get(position).getUsername());
                            startActivity(myIntent);
                        }
                    })
            );
            recyclerview.setVisibility(View.VISIBLE);


        }
        else {

            Log.d("query","Object retrieval failed");
        }
        return view;
    }

}
