package com.blackpearl.bloodlines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * Created by Anubhav on 05/09/15.
 */
public class DispatchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            if(ParseUser.getCurrentUser().getBoolean("gotDetails")==true) {
                startActivity(new Intent(this, StartingActivity.class));
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
            else {
                startActivity(new Intent(this, GetDetails.class));
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
