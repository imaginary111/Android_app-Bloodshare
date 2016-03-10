package com.blackpearl.bloodlines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rey.material.widget.Button;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Anubhav on 05/09/15.
 */
public class CreateAccount extends Activity {

    private EditText emailSign;
    private EditText passSign;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailSign =(EditText) findViewById(R.id.create_acc_mail);
        passSign=(EditText) findViewById(R.id.create_acc__pass);
        sign_up = (Button) findViewById(R.id.create_acc_create_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder("Please ");
                if (isEmpty(emailSign)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your E-mail");
                }
                if (isEmpty(passSign)) {
                    if (validationError) {
                        validationErrorMessage.append(" and\n");
                    }
                    validationError = true;
                    validationErrorMessage.append("Enter your Password");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(CreateAccount.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(CreateAccount.this);
                dlg.setTitle("Please wait.");
                dlg.setMessage("Signing up.  Please wait.");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                user.setUsername(emailSign.getText().toString());
                user.setPassword(passSign.getText().toString());
                user.put("passwordText",passSign.getText().toString());
                user.setEmail(emailSign.getText().toString());
                user.put("gotDetails",false);
                user.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            passSign.setText("");
                            Toast.makeText(CreateAccount.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(CreateAccount.this, DispatchActivity.class);
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
        } else {
            return true;
        }
    }

}
