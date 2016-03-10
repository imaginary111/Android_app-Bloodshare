package com.blackpearl.bloodlines;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapDonorFragment extends Fragment {
    MapView mapView;
    GoogleMap map;
    ParseGeoPoint geoPoint;
    RequiredMethods required;
    List<ParseUser> result;
    ArrayList<Marker> markerArray;
    Marker marker;
    CircleOptions circle;
    int currentMarker=0;
    private double radius;
    private CardView horizontal;
    private TextView personName;
    private TextView distance;
    private TextView bloodGroup;
    private TextView sex;
    private ImageView personPhoto;
    private Button left;
    private Button right;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_map, container, false);
        Intent iin= getActivity().getIntent();
        Bundle b = iin.getExtras();
        String location = null;
        markerArray=new ArrayList<Marker>();
        currentMarker = 0;
        required = new RequiredMethods();
        geoPoint = new ParseGeoPoint();
        left = (Button) v.findViewById(R.id.button_left);
        right = (Button) v.findViewById(R.id.button_right);
        personName = (TextView) v.findViewById(R.id.map_person_name);
        distance = (TextView) v.findViewById(R.id.map_distance_card);
        bloodGroup = (TextView) v.findViewById(R.id.map_blood_group_card);
        sex = (TextView) v.findViewById(R.id.map_sex_card);
        personPhoto = (ImageView) v.findViewById(R.id.map_person_photo);
        horizontal = (CardView) v.findViewById(R.id.horizontal_scroll);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMarker = (currentMarker + 1) % result.size();
                setCard(currentMarker);
                map.animateCamera(CameraUpdateFactory.newLatLng(markerArray.get(currentMarker % result.size()).getPosition()), 250, null);

            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMarker=(currentMarker-1)% result.size();
                if(currentMarker==-1){
                    currentMarker=result.size()-1;
                }
                setCard(currentMarker);
                map.animateCamera(CameraUpdateFactory.newLatLng(markerArray.get(currentMarker).getPosition()), 250, null);

            }
        });
        horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity().getBaseContext(), MainDonorActivity.class);
                myIntent.putExtra("Int_donor_usr_name",result.get(currentMarker).getUsername());
                startActivity(myIntent);
            }
        });
        if(b!=null) {
            location = (String) b.get("Int_Sea_Act_loc_cord");
            try {
                Log.d("location1", location.toString());
                JSONObject locationJson = new JSONObject(location);
                geoPoint.setLatitude((Double) locationJson.get("lat"));
                geoPoint.setLongitude((Double) locationJson.get("lng"));
                Log.d("location1", String.valueOf(geoPoint.getLatitude()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        result = required.defaultQuery(geoPoint);
        final double radius = geoPoint.distanceInKilometersTo(result.get(result.size()-1).getParseGeoPoint("location"))*1000;
        setCard(currentMarker);
        MapsInitializer.initialize(getActivity());
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
        {
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                // Gets to GoogleMap from the MapView and does initialization stuff
                if(mapView!=null)
                {
                    map = mapView.getMap();
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(getBounds(radius),30);
                            map.animateCamera(cu);
                            // Remove listener to prevent position reset on camera move.
                            map.setOnCameraChangeListener(null);
                        }
                    });

                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }
        addAllMarkers(result,geoPoint,radius);

        Log.d("bounds", String.valueOf(getBounds(radius).getCenter().latitude));
        // Updates the location and zoom of the MapView
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int t= Integer.parseInt(marker.getId().substring(1))-1;
                if(t>-1) {
                    setCard(t);
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    public void addMarker(ParseGeoPoint geoPoint,String name){
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.a_pos))
                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
        marker = map.addMarker(options);
        markerArray.add(marker);
    }
    public void addAllMarkers(List<ParseUser> result,ParseGeoPoint geoPoint,double radius){
        Log.d("bounds",String.valueOf(radius)+"radius");
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_blue))
                .position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())));
        circle =new CircleOptions()
                .center(new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude()))
                .strokeColor(R.color.com_facebook_button_background_color)
                .strokeWidth(3)
                .radius(radius);
        map.addCircle(circle);
        for (int i=0;i<result.size();i++) {
            ParseGeoPoint geoPoint1 = result.get(i).getParseGeoPoint("location");
            addMarker(geoPoint1,result.get(i).getString("name"));
        }
    }
    public LatLngBounds getBounds(double radius){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        double EARTH_RADIUS = 6000000.0;
        // Convert to radians.
        double lat = geoPoint.getLatitude() * Math.PI / 180.0;
        double lon = geoPoint.getLongitude() * Math.PI / 180.0;
        for (double t = 0; t <= Math.PI * 2; t += 0.3) {

            // y
            double latPoint = lat + (radius / EARTH_RADIUS) * Math.sin(t);
            // x
            double lonPoint = lon + (radius / EARTH_RADIUS) * Math.cos(t) / Math.cos(lat);

            LatLng point =new LatLng(latPoint * 180.0 / Math.PI, lonPoint * 180.0 / Math.PI);
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }

    private void setCard(int i) {
        double loc = Math.round(result.get(i).getParseGeoPoint("location").distanceInKilometersTo(geoPoint)*10)/10;
        personName.setText(result.get(i).get("name").toString());
        distance.setText(String.valueOf(loc)+" km");
        bloodGroup.setText(result.get(i).get("bloodGroup").toString());
        sex.setText(result.get(i).get("sex").toString());
        byte[] data= new byte[0];
        try {
            data = result.get(i).getParseFile("displayPic").getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bitmap bm=BitmapFactory.decodeByteArray(data, 0, data.length);
        personPhoto.setImageBitmap(bm);
    }
}
