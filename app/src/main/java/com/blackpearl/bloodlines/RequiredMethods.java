package com.blackpearl.bloodlines;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Anubhav on 05/09/15.
 */
public class RequiredMethods {
    public JSONObject makeJson(double distance, boolean a_pos, boolean a_neg, boolean b_pos, boolean b_neg, boolean ab_pos, boolean ab_neg, boolean o_pos, boolean o_neg, boolean male, boolean female, int limit){
        JSONObject mainJson = new JSONObject();
        try {
            mainJson.put("limit",limit);
            mainJson.put("male",male);
            mainJson.put("female",female);
            mainJson.put("a_pos",a_pos);
            mainJson.put("a_neg",a_neg);
            mainJson.put("b_pos",b_pos);
            mainJson.put("b_neg",b_neg);
            mainJson.put("o_pos",o_pos);
            mainJson.put("o_neg",o_neg);
            mainJson.put("ab_pos",ab_pos);
            mainJson.put("ab_neg",ab_neg);
            mainJson.put("distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainJson;
    }
    public JSONObject defaultJson(){
        JSONObject defJson = null;
        defJson = makeJson(5000,false,false,false,false,false,false,false,false,false,false,20);
        return defJson;
    }

    public static List<ParseUser> mainQuery(JSONObject filter, ParseGeoPoint location) throws JSONException {
        List<ParseUser> result = null;
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereWithinKilometers("location", location, filter.getDouble("distance"));
        query.setLimit(filter.getInt("limit"));
        query.whereNear("location", location);
        if(filter.getBoolean("a_pos")){
            query.whereEqualTo("bloodGroup","A+");
        }
        if(filter.getBoolean("a_neg")){
            query.whereEqualTo("bloodGroup","A-");
        }
        if(filter.getBoolean("b_pos")){
            query.whereEqualTo("bloodGroup","B+");
        }
        if(filter.getBoolean("a_neg")){
            query.whereEqualTo("bloodGroup","B+");
        }
        if(filter.getBoolean("ab_pos")){
            query.whereEqualTo("bloodGroup","AB+");
        }
        if(filter.getBoolean("ab_neg")){
            query.whereEqualTo("bloodGroup","AB-");
        }
        if(filter.getBoolean("o_pos")){
            query.whereEqualTo("bloodGroup","O+");
        }
        if(filter.getBoolean("o_neg")){
            query.whereEqualTo("bloodGroup","O-");
        }
        if(filter.getBoolean("male")){
            query.whereEqualTo("sex","Male");
        }
        if(filter.getBoolean("female")){
            query.whereEqualTo("sex","Female");
        }

        try {
            result = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ParseUser> defaultQuery(ParseGeoPoint pos){
        List<ParseUser> result = null;
        try {
            result = mainQuery(defaultJson(),pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getJSONFromUrl(String url) {
        Log.d("face", "entered");
        // Making HTTP request
        InputStream is = null;
        JSONObject jobj = null;
        String json = null;
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();


            StringBuilder buffer = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                json = buffer.toString();

            } finally {
                is.close();
                reader.close();
            }

            try {
                jobj = new JSONObject(json);
                System.out.println("JSON : " + jobj);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // try parse the string to a JSON object
        Log.d("face",jobj.toString());
        // return JSON String
        return jobj;

    }

    //    public static List<ParseObject> EventQuery(ParseGeoPoint location) throws JSONException {
//        List<ParseObject> eventResult = null;
//        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
//        query.whereWithinKilometers("locationPoint", location, 5000);
//        query.whereNear("locationPoint", location);
//        try {
//            eventResult = query.find();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return eventResult;
//    }
    public static List<ParseObject> EventQuery(ParseGeoPoint location) {
        List<ParseObject> eventResult = null;
//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
//    query.whereWithinKilometers("locationPoint", location, 5000);
//    query.whereNear("locationPoint", location);
//    query.findInBackground(new FindCallback<ParseObject>() {
//        @Override
//        public void done(List<ParseObject> parseObjects, ParseException e) {
//            if (e == null) {
//                eventResult = parseObjects;
//            } else {
//                Log.d("query","object retrival failed");
//            }
//        }
//    });
        return eventResult;
    }
    public class hasActiveInternetConnection extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    public boolean isUserLoggedIn(Context context){
        if(ParseUser.getCurrentUser()==null){
            context.startActivity(new Intent(context, SearchActivity.class));
            return false;
        }
        else {
            return true;
        }
    }
    public void checkForNet(Context context){

//            boolean test = new com.anubhav.bloodylines.hasActiveInternetConnection(context).execute().get();
        boolean test =isNetworkAvailable(context);
//        if(!test){
//            InternetCheckDialog dialog =new InternetCheckDialog(context);
//            dialog.show();
//        }

    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
