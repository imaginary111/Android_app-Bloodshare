package com.blackpearl.bloodlines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.rey.material.widget.Button;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;

public class LoginActivity extends Activity {

    private EditText userId;
    private EditText userPassword;
    private Button normal_login;
    private TextView signUp;
    private Button facebook_login;
    private TextView forgotPassword;
    private Collection<String> facebook_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        facebook_permission= Arrays.asList("public_profile", "email");
        normal_login = (Button) findViewById(R.id.login_act_normal_log);
        signUp = (TextView) findViewById(R.id.log_act_create_id);
        userId = (EditText) findViewById(R.id.login_act_id);
        userPassword = (EditText) findViewById(R.id.login_act_pass);
        facebook_login =(Button) findViewById(R.id.login_act_fb_log);
        forgotPassword = (TextView) findViewById(R.id.log_act_for_pass);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground((Activity) v.getContext(), facebook_permission, facebookLoginCallbackV4);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
        normal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder("Please ");
                if (isEmpty(userId)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your E-mail");
                }
                if (isEmpty(userPassword)) {
                    if (validationError) {
                        validationErrorMessage.append(" and\n");
                    }
                    validationError = true;
                    validationErrorMessage.append("Enter your Password");
                }
                validationErrorMessage.append(".");

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Logging in.  Please wait.");
                dlg.show();
                ParseUser.logInInBackground(userId.getText().toString(), userPassword.getText()
                        .toString(), new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private LogInCallback facebookLoginCallbackV4 = new LogInCallback(){

        @Override
        public void done(ParseUser parseUser, ParseException e) {

        }
    };

}
