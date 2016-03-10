package com.blackpearl.bloodlines;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import com.parse.ParseUser;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Anubhav on 05/09/15.
 */
public class StartingActivity extends Activity {

    private TextView search;
    private LinearLayout event_layout;
    private TextView profile;
    private TextView notifications;
    private TextView logout;
    private TextView rate;
    private ImageButton showMenu;
    private TextView emergency;
    private TextView callUs;
    private TextView share;
    private TextView events;
    private TextView helpUs;
    private DrawerLayout drawer;
    private LinearLayout drawerPanel;
    private LinearLayout reqBlood;
    private CircularImageView pro_pic;
    private TextView user_name;
    private TextView bld_grp;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        pro_pic = (CircularImageView) findViewById(R.id.starting_act_pro_pic);
        user_name = (TextView) findViewById(R.id.starting_act_userName);
        bld_grp = (TextView) findViewById(R.id.starting_act_blood_group);
        search = (TextView) findViewById(R.id.starting_act_search);
        event_layout = (LinearLayout) findViewById(R.id.act_str_nearby_evts);
        reqBlood = (LinearLayout) findViewById(R.id.act_str_req_bld);
        profile = (TextView) findViewById((R.id.starting_act_profile_bar));
        events = (TextView) findViewById(R.id.starting_act_events_bar);
        notifications = (TextView) findViewById(R.id.starting_act_notifications_bar);
        logout = (TextView) findViewById(R.id.starting_act_logout_bar);
        rate = (TextView) findViewById(R.id.starting_act_rate_bar);
        showMenu = (ImageButton) findViewById(R.id.starting_act_show_menu);
        emergency = (TextView) findViewById(R.id.starting_act_emergency_bar);
        callUs = (TextView) findViewById(R.id.starting_act_call_us_bar);
        share = (TextView) findViewById(R.id.starting_act_share_bar);
        helpUs = (TextView) findViewById(R.id.starting_act_help_bar);
        drawer = (DrawerLayout) findViewById((R.id.starting_act_drawerLayout));
        drawerPanel = (LinearLayout) findViewById(R.id.starting_act_drawer_panel);

        Picasso.with(getBaseContext()).load(ParseUser.getCurrentUser().getParseFile("displayPic").getUrl()).into(pro_pic);
        user_name.setText(ParseUser.getCurrentUser().getString("name"));
        bld_grp.setText(ParseUser.getCurrentUser().getString("bloodGroup"));
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(StartingActivity.this, UserProfile.class);
//                drawer.closeDrawer(drawerPanel);
//                startActivity(myIntent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, SearchActivity.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        event_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, EventsActivity.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        reqBlood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, BloodRequest.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent myIntent = new Intent(StartingActivity.this, DispatchActivity.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                drawer.closeDrawer(drawerPanel);
                callIntent.setData(Uri.parse("tel:09779237774"));
                startActivity(callIntent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(drawerPanel);

            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, EventsActivity.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartingActivity.this, NotificationsActivity.class);
                drawer.closeDrawer(drawerPanel);
                startActivity(myIntent);
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(StartingActivity.this, EmergencyActivity.class);
//                drawer.closeDrawer(drawerPanel);
//                startActivity(myIntent);
            }
        });
        helpUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(StartingActivity.this, HelpUs.class);
//                drawer.closeDrawer(drawerPanel);
//                startActivity(myIntent);
            }
        });
        
    }
}
