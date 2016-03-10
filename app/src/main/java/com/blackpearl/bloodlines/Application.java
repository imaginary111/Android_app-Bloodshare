package com.blackpearl.bloodlines;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by Anubhav on 05/09/15.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
