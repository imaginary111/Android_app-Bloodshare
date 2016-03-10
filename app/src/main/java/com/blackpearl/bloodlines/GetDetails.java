package com.blackpearl.bloodlines;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rey.material.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

/**
 * Created by Anubhav on 05/09/15.
 */
public class GetDetails extends Activity{

    private String[] items = new String[] {"A+", "A-", "B+","B-","O+","O-","AB+","AB-"};
    private static int RESULT_LOAD_IMAGE = 1;
    Uri myPicture = null;
    private Double latitude = 30.889931,longitude = 75.799114;
    private Random random= new Random();
    private Double ran1 = (double) random.nextInt(600000)/10000000;
    private Double ran2 = (double) random.nextInt(200000)/10000000;
    private Spinner spinner;
    private EditText fullName;
    private EditText mobileNo;
    private EditText city;
    private RadioGroup sex;
    private String sexText;
    private ImageView proPic;
    private Bitmap bitmap;
    private byte[] image;
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        fullName = (EditText) findViewById(R.id.details_act_name);
        mobileNo = (EditText) findViewById(R.id.details_act_phone_no);
        city = (EditText) findViewById(R.id.details_act_city);
        sex=(RadioGroup) findViewById(R.id.details_act_radioSex);
        spinner = (Spinner) findViewById(R.id.details_act_spinner);
        proPic = (ImageView) findViewById(R.id.details_act_pro_pic);
        next = (Button) findViewById(R.id.details_act_next);
        if(ParseUser.getCurrentUser().getParseFile("displayPic")!=null) {
            try {
                byte[] data = ParseUser.getCurrentUser().getParseFile("displayPic").getData();
                proPic.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        RadioButton radioButtonMale = (RadioButton) findViewById(R.id.details_act_radioMale);
        sexText = radioButtonMale.getText().toString();

        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String text = radioButton.getText().toString();
                sexText =text;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder("Please");
                if (isEmpty(fullName)) {
                    validationError = true;
                    validationErrorMessage.append("Enter your Name");
                }
                if (isEmpty(mobileNo)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("Enter your Mobile No");
                }
                if (isEmpty(city)) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("Enter your City");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(GetDetails.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                BitmapDrawable drawable = (BitmapDrawable) proPic.getDrawable();
                bitmap = drawable.getBitmap();
                bitmap = getResizedBitmap(bitmap,200,200);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                image = stream.toByteArray();

                final ProgressDialog dlg = new ProgressDialog(GetDetails.this);
                ParseGeoPoint point = new ParseGeoPoint(latitude+ran1, longitude+ran2);
                ParseFile file = new ParseFile("picture.jpg", image);

                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().put("location", point);
                    ParseUser.getCurrentUser().put("name", fullName.getText().toString());
                    ParseUser.getCurrentUser().put("city", city.getText().toString());
                    ParseUser.getCurrentUser().put("sex", sexText);
                    ParseUser.getCurrentUser().put("mobileNo",mobileNo.getText().toString());
                    ParseUser.getCurrentUser().put("bloodGroup", spinner.getSelectedItem().toString());
                    ParseUser.getCurrentUser().put("gotDetails", true);
                    ParseUser.getCurrentUser().put("displayPic", file);
                    dlg.dismiss();ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("gps", "SAVE SUCCESSFUL");

                            } else {
                                Log.d("gps", "SAVE FAILED " + e.getCause());
                            }
                        }
                    });

                }
                Intent intent = new Intent(GetDetails.this, DispatchActivity.class);
                startActivity(intent);

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

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                performCrop(data.getStringExtra("picturePath"));
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                ImageView imageView = (ImageView) findViewById(R.id.details_act_pro_pic);
                imageView.setImageBitmap(selectedBitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

    }

}
