package com.blackpearl.bloodlines;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import java.util.Timer;
public class AppLogoActivity extends Activity {
    private static final long DELAY = 3000;
    private boolean scheduled = false;
    private Timer splashTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_logo);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                     /* Create an intent that will start the main activity. */
                Intent mainIntent = new Intent(AppLogoActivity.this,DispatchActivity.class);
                        startActivity(mainIntent);
                     /* Finish splash activity so user cant go back to it. */
                AppLogoActivity.this.finish();

                     /* Apply our splash exit (fade out) and main
                        entry (fade in) animation transitions. */
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
            }
        }, 1000);
    }
}
