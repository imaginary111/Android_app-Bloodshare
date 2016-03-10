package com.blackpearl.bloodlines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Anubhav on 05/09/15.
 */
public class BloodRequest extends AppCompatActivity {
    private EditText name;
    private EditText mobile;
    private EditText city;
    private EditText description;
    private RadioGroup sex;
    private Button createRequest;
    private RequiredMethods required;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView location;
    private Spinner bloodGroup;
    private String sexText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] items = new String[] {"A+", "A-", "B+","B-","O+","O-","AB+","AB-"};
        Log.d("BloodRequest", "ActivityStarted");
        setContentView(R.layout.activity_req_blood);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_request_blood); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ParseObject bloodRequest = new ParseObject("BloodRequests");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        required = new RequiredMethods();
        name = (EditText) findViewById(R.id.requester_name);
        mobile = (EditText) findViewById(R.id.requester_phone_no);
        city = (EditText) findViewById(R.id.requester_city);
        description = (EditText) findViewById(R.id.description_requester);
        sex = (RadioGroup) findViewById(R.id.radioSex_requester);
        createRequest = (Button) findViewById(R.id.btn_requester);
        location = (TextView) findViewById(R.id.requester_location);
        location.setVisibility(View.GONE);
        bloodGroup = (Spinner) findViewById(R.id.blood_group_requester);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroup.setAdapter(adapter);
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BloodRequest.this, SearchActivity.class);
//                intent.putExtra("caller", "MainActivity");
//                startActivity(intent);
//            }
//        });
        RadioButton radioButtonMale = (RadioButton) findViewById(R.id.radioMale_requester);
        sexText = radioButtonMale.getText().toString();
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String text = radioButton.getText().toString();
                sexText =text;
            }
        });
        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder("Please");
                if (isEmpty(name)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your Name");
                }
                if (isEmpty(mobile)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your Mobile No");
                }

                if (isEmpty(city)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your City");
                }
                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(BloodRequest.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }


                final ProgressDialog dlg = new ProgressDialog(BloodRequest.this);
                dlg.show();
                bloodRequest.put("name", name.getText().toString());
                bloodRequest.put("city", city.getText().toString());
                bloodRequest.put("mobileNo", mobile.getText().toString());
                Log.v("spinnere",bloodGroup.getSelectedItem().toString()+"  "+ sexText);
                bloodRequest.put("bloodGroup", bloodGroup.getSelectedItem().toString());
                bloodRequest.put("sex",sexText);
                bloodRequest.put("modeOfRequest",1);
                bloodRequest.put("description",description.getText().toString());
                bloodRequest.put("FromUser", ParseUser.getCurrentUser().getUsername());
                bloodRequest.put("location",ParseUser.getCurrentUser().getParseGeoPoint("location"));
                bloodRequest.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dlg.dismiss();
                            Log.d("gps", "SAVE SUCCESSFUL");
                            Toast.makeText(BloodRequest.this, "Your request was Successfully added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BloodRequest.this, DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            dlg.dismiss();
                            Log.d("gps", "SAVE FAILED " + e.getCause());
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BloodRequest", "ActivityResumed");
        required = new RequiredMethods();
        required.checkForNet(this);
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
