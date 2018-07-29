package com.bawaaba.rninja4.rookie.BioTab;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;
import com.bawaaba.rninja4.rookie.utils.Constants;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class EditDetails extends BaseActivity {

    private static final String TAG = EditDetails.class.getSimpleName();
    private static final int CAMERA_REQUEST = 100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_WRITE_PERMISSION = 2;
    public static File IMG_URL = null;
    private static int GALLERY_REQUEST = 101;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    JSONParser jsonParser = new JSONParser();
    String profile_image;
    private EditText FullName;
    private AppCompatTextView Dateofbirth;
    private EditText ContactNumber;
    private EditText ProfileUrl;
    private AppCompatTextView Location;
    private CircleImageView user_image;
    private ImageButton user_button;
    private Uri mImageCaptureUri;
    private android.app.AlertDialog dialog;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private SQLiteHandler db;
    private AppCompatTextView Save;
    private RadioGroup Gender;
    private RadioButton male;
    private RadioButton female;
    private Toolbar toolbar;
    private int year = 0;
    private int month = 0;
    private int day;
    private LinearLayout linearLayout;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar userAge = new GregorianCalendar(year, month, day);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                Toast.makeText(EditDetails.this, "You must be 18 years or older to create a Bawabba profile.", Toast.LENGTH_SHORT).show();

            } else {
                Dateofbirth.setText(new StringBuilder().append(year)
                        .append("-").append(month + 1).append("-").append(day)
                        .append(" "));
            }
//            year = year;
//            month = month;
//            day = day;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actiontitle_layout);

            male = (RadioButton) findViewById(R.id.male);
            female = (RadioButton) findViewById(R.id.female);

        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().hide();

        Intent from_profile = getIntent();
        String gender = from_profile.getStringExtra("gender");
        Log.e("Gendercheck", gender);

        if (gender.equalsIgnoreCase("Male")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        /*my edits*/
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        user_image = (CircleImageView) findViewById(R.id.user_image);
        user_button = (ImageButton) findViewById(R.id.user_button);
        FullName = (EditText) findViewById(R.id.fullname);
        Dateofbirth = (AppCompatTextView) findViewById(R.id.dob);
        ContactNumber = (EditText) findViewById(R.id.phone);
        ProfileUrl = (EditText) findViewById(R.id.profile_url);
        Location = (AppCompatTextView) findViewById(R.id.locat);
        Gender = (RadioGroup) findViewById(R.id.gender);
        linearLayout=(LinearLayout)findViewById(R.id.liner);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });


        FullName .addTextChangedListener(textWatcher());
        Dateofbirth .addTextChangedListener(textWatcher1());
        ContactNumber .addTextChangedListener(textWatcher2());
        ProfileUrl.addTextChangedListener(textWatcher3());
        Location .addTextChangedListener(textWatcher4());

        //  captureImageInitialization();
        Dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDate();
            }
        });
        Save = (AppCompatTextView) findViewById(R.id.editbut);
        Save.setOnClickListener(new View.OnClickListener() {

            // RadioButton male = (RadioButton) findViewById(R.id.male);
            private RadioButton radioButton;
            private RadioGroup Gender;

            //boolean isMale = male.isChecked();
            @Override
            public void onClick(View v) {
                Drawable profile_img = user_image.getDrawable();
                String fullname = FullName.getText().toString().trim();
                String dob = Dateofbirth.getText().toString().trim();
                String phone = ContactNumber.getText().toString().trim();
                String profile_url = ProfileUrl.getText().toString().trim();
                String place = Location.getText().toString().trim();
                Gender = (RadioGroup) findViewById(R.id.gender);
                int selectedId = Gender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                final String gender = (String) radioButton.getText();
                String image = "";

                if (fullname.length() == 0) {
                    FullName.setBackgroundResource(R.drawable.red_alert_round);
                }else{
                    FullName.setBackgroundResource(R.drawable.grey_box);
                }

                if (dob.length() == 0) {
                    Dateofbirth.setBackgroundResource(R.drawable.red_alert_round);
                }else{
                    Dateofbirth.setBackgroundResource(R.drawable.grey_box);
                }

                if(phone.length() == 0 &&phone.length()<13){
                    ContactNumber.setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(getApplicationContext(),
                            "Minimum 13 digits are required for contact number with country code", Toast.LENGTH_SHORT)
                            .show();
                    return ;
                }
                if(phone.length()<13){
                    ContactNumber.setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(getApplicationContext(),
                            "Minimum 13 digits are required for contact number with country code", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if(profile_url.length() == 0){
                    ProfileUrl.setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(getApplicationContext(),
                            "Mininmum 3 characters required for profile URL", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if(profile_url.length()<3){
                    ProfileUrl.setBackgroundResource(R.drawable.red_alert_round);
                    Toast.makeText(getApplicationContext(),
                            "Mininmum 3 characters required for profile URL", Toast.LENGTH_SHORT)
                            .show();
                    return ;
                }

                if(place.length()==0){
                    Location.setBackgroundResource(R.drawable.red_alert_round);
                }else{
                    Location.setBackgroundResource(R.drawable.grey_box);
                }

                if (bitmap == null) {
                    try {
                        URL url = new URL(profile_image);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        image = getStringImage(imageBitmap);

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                } else {
                    image = getStringImage(bitmap);
                }

                if (!fullname.isEmpty() && !dob.isEmpty() && !phone.isEmpty()&& !profile_url.isEmpty() && !place.isEmpty() && !gender.isEmpty()) {
                    Intent from_profile = getIntent();
                    String user_id = from_profile.getStringExtra("user_id");
                    editdetailsUser(user_id,image,fullname, dob, phone, profile_url, place, gender);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        user_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/


                int result = ContextCompat.checkSelfPermission(EditDetails.this, Manifest.permission.CAMERA);
                int result1 = ContextCompat.checkSelfPermission(EditDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                        //      Glide.with(EditDetails.this).load(EditDetails.IMG_URL).placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).fitCenter().into(user_image);
                    } else {
                        alert();
                    }
                } else {
                    buildAlertMessageNoCamera();
                }
            }
        });

        new Editdet().execute();
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private TextWatcher textWatcher4() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Location.getText().toString().equals("")) { //if edittext include text
                    Location.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    Location.setBackgroundResource(R.drawable.red_alert_round);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher textWatcher3() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!ProfileUrl.getText().toString().equals("")) { //if edittext include text
                    ProfileUrl.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    ProfileUrl.setBackgroundResource(R.drawable.red_alert_round);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher textWatcher2() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!ContactNumber.getText().toString().equals("")) { //if edittext include text
                    ContactNumber.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    ContactNumber.setBackgroundResource(R.drawable.red_alert_round);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher textWatcher1() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Dateofbirth.getText().toString().equals("")) { //if edittext include text
                    Dateofbirth.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    Dateofbirth.setBackgroundResource(R.drawable.red_alert_round);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher textWatcher() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!FullName.getText().toString().equals("")) { //if edittext include text
                    FullName.setBackgroundResource(R.drawable.grey_box);

                } else { //not include text
                    FullName.setBackgroundResource(R.drawable.red_alert_round);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void takePhoto() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditDetails.this);
            builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //    buildAlertMessageNoCamera();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        //  Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);//

                    } else if (items[item].equals(Constants.ProfileFragment.FROM_LIBRARY)) {

                        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryintent, GALLERY_REQUEST);

                    } else if (items[item].equals(Constants.ProfileFragment.CANCEL)) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alert() {
        ActivityCompat.requestPermissions(EditDetails.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);

    }

    //    private void captureImageInitialization() {
//
//
//        final String[] items = new String[]{"Take from camera",
//                "Select from gallery"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.select_dialog_item, items);
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//
//        builder.setTitle("Select Image");
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (item == 0) {
//                    Log.e(TAG, "bitmap");
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    mImageCaptureUri = Uri.fromFile(new File(Environment
//                            .getExternalStorageDirectory(), "tmp_avatar_"
//                            + String.valueOf(System.currentTimeMillis())
//                            + ".jpg"));
//
//                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//                            mImageCaptureUri);
//                    try {
//                        Log.e(TAG, "bitmap1");
//                        intent.putExtra("return-data", true);
//                        startActivityForResult(intent, PICK_FROM_CAMERA);
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Log.e(TAG, "bitmap2");
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,
//                            "Complete action using"), PICK_FROM_FILE);
//                }
//            }
//        });
//        dialog = builder.create();
//    }
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(TAG, "bitmap compression");
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            user_image.setImageBitmap(bitmap);
        }
    }*/

    private void buildAlertMessageNoCamera() {
        ActivityCompat.requestPermissions(EditDetails.this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void editdetailsUser(final String user_id, final String image, final String fullname, final String dob, final String phone, final String profile_url, final String place, final String gender) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(EditDetails.this).showLoadingDialog(EditDetails.this);
        dialog.show();

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().editUserDeatils("app-client",
                "123321", ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(), ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                user_id,image,fullname,dob,phone,profile_url,place,gender);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body() != null) {
                    dialog.dismiss();
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            Toast.makeText(getApplicationContext(),
                                    "Your details have been updated successfully", Toast.LENGTH_SHORT).show();

                            AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                            BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                           /* Intent to_profile = new Intent(EditDetails.this, ProfileView.class);
                            startActivity(to_profile);*/
                            finish();
                        }else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

//    private void editdetailmsUser(final String user_id, final String fullname, final String dob, final String phone, final String profile_url, final String place, final String image, final String gender) {
//
//        final Dialog dialog = ObjectFactory.getInstance().getUtils(EditDetails.this).showLoadingDialog(EditDetails.this);
//        dialog.show();
//
//        db = new SQLiteHandler(getApplicationContext());
//
//        HashMap<String, String> user = db.getUserDetails();
//        final String token = user.get("token");//token value after the user logedin
//
//        String tag_string_req = "req_register";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL_EDIT_DETAILS, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//
//                try {
//                    Log.e(TAG, response);
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
//                        Toast.makeText(getApplicationContext(),
//                                "Your details have been updated successfully", Toast.LENGTH_SHORT).show();
//
//                        Intent to_profile = new Intent(EditDetails.this, ProfileView.class);
//                        startActivity(to_profile);
//                        finish();
//                    }else {
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                //hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", user_id);
//                params.put("profile_img", image);
//                params.put("fullname", fullname);
//                params.put("dob", dob);
//                params.put("phone", phone);
//                params.put("profile_url", profile_url);
//                params.put("location", place);
//                params.put("gender", gender);
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map headers = new HashMap();
//                headers.put("Client-Service", "app-client");
//                headers.put("Auth-Key", "123321");
//                headers.put("Token", token);
//                headers.put("User-Id", user_id);
//                return headers;
//            }
//        };
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    /*my edits*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            final Bitmap photo;
            try {
                photo = (Bitmap) data.getExtras().get("data");
                bitmap = photo;
                user_image.setImageBitmap(bitmap);

                File photopath = null;
                photopath = saveToInternalStorage(photo);
                IMG_URL = photopath;
                Glide.with(this).load(EditDetails.IMG_URL).fitCenter().into(user_image);
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
                Random rand = new Random();
                int num = rand.nextInt(5000) + 1;
                File path = new File(directory, "" + num + ".jpg");

                UCrop.of(Uri.fromFile(photopath), Uri.fromFile(path))
                        .withAspectRatio(9, 9)
                        .withMaxResultSize(200, 200)
                        .start(EditDetails.this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_REQUEST && (data != null)) {
            try {
                Uri selected_image = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(selected_image, filepath, null, null, null);
                cursor.moveToFirst();
                int columIntex = cursor.getColumnIndex(filepath[0]);
                String filePath = cursor.getString(columIntex);
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                bitmap = yourSelectedImage;

                File path = new File(filePath);
                IMG_URL = path;
                Glide.with(this).load(EditDetails.IMG_URL).fitCenter().into(user_image);
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
                Random rand = new Random();
                int num = rand.nextInt(5000) + 1;
                File pathDest = new File(directory, "" + num + ".jpg");
                UCrop.of(Uri.fromFile(path), Uri.fromFile(pathDest))
                        .withAspectRatio(9, 9)
                        .withMaxResultSize(200, 200)
                        .start(EditDetails.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        } else if (requestCode == 12) {
//            if (resultCode == RESULT_OK) {
//                // retrive the data by using getPlace() method.
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
//                Location.setText(place.getAddress());
//
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }

        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            IMG_URL = new File(resultUri.getPath());
//            Glide.with(this).load(EditDetails.IMG_URL).placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).fitCenter().into(user_image);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                user_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }else if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
            Location.setText(place.getAddress());

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
        }

    private File saveToInternalStorage(Bitmap bitmap) throws IOException {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
        }
        user_image.setImageBitmap(bitmap);

        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
        Random rand = new Random();
        int num = rand.nextInt(5000) + 1;
        File path = new File(directory, "" + num + ".jpg");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return path;
    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 12);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        {
//            if (requestCode == 1) {
//                if (resultCode == RESULT_OK) {
//                    // retrive the data by using getPlace() method.
//                    Place place = PlaceAutocomplete.getPlace(this, data);
//                    Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
//                    Location.setText(place.getAddress());
//
//                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(this, data);
//
//                } else if (resultCode == RESULT_CANCELED) {
//                    // The user canceled the operation.
//                }
//
//            }
//        }
//    }


    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        showDialog(132);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 132:

            return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }
    private class Editdet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            Intent from_profile = getIntent();
            //  String user_id = from_profile.getStringExtra("user_id");
            profile_image = from_profile.getStringExtra("profile_img");
            final String name = from_profile.getStringExtra("name");
            final String dob = from_profile.getStringExtra("dob");
            final String phone = from_profile.getStringExtra("phone");
            final String gender = from_profile.getStringExtra("gender");
            final String location = from_profile.getStringExtra("location");
            final String profile_url = from_profile.getStringExtra("profile_url");

            EditDetails.this.runOnUiThread(new Runnable() {


                @Override
                public void run() {
                    //  user_image = (ImageView) findViewById(R.id.user_image);
                    FullName.setText(name);
                    Dateofbirth.setText(dob);
                    ContactNumber.setText(phone);
                    ProfileUrl.setText(profile_url);
                    Location.setText(location);

                    if(gender.equalsIgnoreCase("Male")){
                        male.setChecked(true);
                    }else{
                        female.setChecked(true);
                    }
                    Glide.with(getApplicationContext()).load(profile_image).transform(new CircleTransform(getApplicationContext())).into(user_image);
                }
            });
            return null;
        }
    }
}

