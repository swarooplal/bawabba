package com.bawaaba.rninja4.rookie.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;

//import com.example.rninja4.rookie.MainActivity;
//import com.example.rninja4.rookie.helper.SQLiteHandler;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    RelativeLayout family;
    String userRole = "0";
    private Button btnRegister;
    //private Button btnRegister_hire;
    private TextView btnLinkToLogin;
    //    private EditText FullName;
//    private EditText Email;
    private com.optimus.edittextfield.EditTextField FullName;
    private com.optimus.edittextfield.EditTextField Email;
    private EditText Password;
    private EditText confirm_Password;
    private AppCompatTextView InputLocation;
    private EditText contact_number;
    private EditText DateofBirth;
    private RadioGroup Gender;
    private RadioButton radiobutton;
    private RadioButton Male;
    private RadioButton Female;
    private RadioGroup Role;
    private RadioButton Hire;
    private RadioButton Work;
    private EditText Description;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Bitmap bitmap;
    private LinearLayout phone_layout;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private SQLiteHandler db;
    private AppCompatTextView tvHire;
    private AppCompatTextView tvWork;
    private static final int CAMERA_REQUEST = 100;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int REQUEST_WRITE_PERMISSION = 2;
    public static File IMG_URL = null;
    private static int GALLERY_REQUEST = 101;
    final CharSequence[] items = {"Take Photo",
            "Choose from Library",
            "Cancel"};
    String profile_image;
    private CircleImageView user_image;
    private ImageButton user_button;

    private int year = 0;
    private int month = 0;
    private int day;
    CountryCodePicker ccp;

    private TextView Faq;
    private TextView Termsofuse;
    private Button btnClear;
    private Button btnClear1;
    private CoordinatorLayout cordinator_layout;
    //  private  io.github.kobakei.materialfabspeeddial.FabSpeedDial fab;



    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//
//            Calendar userAge = new GregorianCalendar(year, month, day);
//            Calendar minAdultAge = new GregorianCalendar();
//            minAdultAge.add(Calendar.YEAR, -18);
//            if (minAdultAge.before(userAge)) {
//                Toast.makeText(getApplicationContext(),
//                        "You must be 18 yeasrs or older to create a Bawabba profile", Toast.LENGTH_LONG)
//                        .show();
//
//            }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar userAge = new GregorianCalendar(year,month,day);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                Toast.makeText(RegisterActivity.this, "You must be 18 years or older to create a Bawabba profile.", Toast.LENGTH_SHORT).show();

            }else{
                DateofBirth.setText(new StringBuilder().append(year)
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
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.new_reg);
        Role = (RadioGroup) findViewById(R.id.role);
        Role.check(R.id.freelance);

//        btnClear = (Button)findViewById(R.id.btn_clear);
//        btnClear1= (Button)findViewById(R.id.btn_clear1);
        Faq=(TextView)findViewById(R.id.faq);
        Termsofuse=(TextView)findViewById(R.id.terms);

        FullName = (com.optimus.edittextfield.EditTextField) findViewById(R.id.fullname);
        Email = (com.optimus.edittextfield.EditTextField) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        confirm_Password = (EditText) findViewById(R.id.confirm_password);
        InputLocation = (AppCompatTextView) findViewById(R.id.place);
        contact_number=(EditText)findViewById(R.id.phone);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        DateofBirth = (EditText) findViewById(R.id.dob);
        Description = (EditText) findViewById(R.id.description);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FullName.addTextChangedListener(textWatcher());
        // btnClear.setOnClickListener(onClickListener());

        Email.addTextChangedListener(textWatcher1());
        // btnClear1.setOnClickListener(onClickListener1());

        Password.addTextChangedListener(textWatcher2());

        confirm_Password.addTextChangedListener(textWatcher3());

        InputLocation.addTextChangedListener(textWatcher4());

        DateofBirth.addTextChangedListener(textWatcher5());

        //  contact_number.addTextChangedListener(textWatcher6());
        Description.addTextChangedListener(textWatcher7());



        /**/
        List<String> st = new ArrayList<>();
        ObjectFactory.getInstance().getNetworkManager(this).setCheckedItems(st);
        ObjectFactory.getInstance().getNetworkManager(this).setCheckedLangauges(st);

        intiViews();
        onClickListners();


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        user_image = (CircleImageView) findViewById(R.id.user_image);
        user_button = (ImageButton) findViewById(R.id.user_button);
        FullName = (com.optimus.edittextfield.EditTextField) findViewById(R.id.fullname);
        Email = (com.optimus.edittextfield.EditTextField) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        confirm_Password = (EditText) findViewById(R.id.confirm_password);
        InputLocation = (AppCompatTextView) findViewById(R.id.place);
        contact_number=(EditText)findViewById(R.id.phone);
        phone_layout=(LinearLayout)findViewById(R.id.phone_layout);
        Gender = (RadioGroup) findViewById(R.id.gender);
        Male = (RadioButton) findViewById(R.id.male);
        DateofBirth = (EditText) findViewById(R.id.dob);
        Female = (RadioButton) findViewById(R.id.female);
        Description = (EditText) findViewById(R.id.description);
        Role = (RadioGroup) findViewById(R.id.role);
        Hire = (RadioButton) findViewById(R.id.hire);
        Work = (RadioButton) findViewById(R.id.freelance);
        ImageButton home = (ImageButton) findViewById(R.id.home);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (TextView) findViewById(R.id.btnLinkToLoginScreen);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        io.github.kobakei.materialfabspeeddial.FabSpeedDial fab=(io.github.kobakei.materialfabspeeddial.FabSpeedDial)findViewById(R.id.fab);
        ccp.registerCarrierNumberEditText(contact_number);


        Role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                if (checkedId == R.id.hire) {
                    Description.setVisibility(View.GONE);


                } else {
                    Description.setVisibility(View.VISIBLE);

                }

            }
        });

        DateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDate();
            }
        });
        JSONObject response;
        Intent i3 = getIntent();
        String jsondata = i3.getStringExtra("FbProfile");
        db = new SQLiteHandler(getApplicationContext());
        Log.e("fb details", "" + jsondata);
        if (jsondata != null) {
            try {
                response = new JSONObject(jsondata);
                Email.setText(response.get("email").toString());
                FullName.setText(response.get("first_name").toString());
                Picasso.with(this).load(response.getString("url"))
                        .into(user_image);





//                Glide.with(this).load("imageUrl")
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .into(user_image);


//                Gender.setText(response.get("gender").toString());

//            profile_pic_data = new JSONObject(response.get("picture").toString());
//            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
//            Picasso.with(this).load(profile_pic_url.getString("url"))
//                    .into(user_picture);


//
//                Picasso.with(this)
//                        .load("https://graph.facebook.com/v2.2/" +.getUserId() + "/picture?height=120&type=normal")
//                        .transform(new CircleTransform())
//                        .resize(120, 120)
//                        .into(user_image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Bundle bundle = getIntent().getExtras();
        String First_name = bundle.getString("linkName");
        String Last_name = bundle.getString("lastName");
        String Email_address = bundle.getString("email");
        String picture = bundle.getString("picturl");
        if(First_name!=null) {
            FullName.setText(First_name);
            Log.e("FirstNmaeLink",First_name);
        }

        if(Email_address!=null){
            Email.setText(Email_address);
        }

        if(picture!=null){
            Glide.with(RegisterActivity.this).load(picture).into(user_image);
            Log.e("picture",picture);
        }


        //Log.e("LinkName",First_name);

//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, FacebookActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        fab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, LinkedinActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton fab, TextView textView, int one) {
                Intent intent = new Intent(RegisterActivity.this, LinkedinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fab.addOnMenuItemClickListener(new FabSpeedDial.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(FloatingActionButton fab, TextView textView, int two) {
                Intent intent = new Intent(RegisterActivity.this, FacebookActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, ProfileView.class);

            startActivity(intent);
            finish();
        }

        Faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_faq = new Intent(RegisterActivity.this, Faqwebpage.class);
                startActivity(to_faq);

            }
        });
        Termsofuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_login = new Intent(RegisterActivity.this, TermsOfService.class);
                startActivity(to_login);
            }
        });



        //if(Hire.isChecked()) {
        btnRegister.setOnClickListener(new View.OnClickListener() {

            RadioButton male = (RadioButton) findViewById(R.id.male);
            boolean isMale = male.isChecked();
            private RadioButton radioButton;
            private RadioGroup Gender;

            // final int role = (Hire.isChecked() ? 1 : 0 );

            @Override
            public void onClick(View v) {
                final Drawable profile_img = user_image.getDrawable();
                final String fullname = FullName.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                final String confirm_password = confirm_Password.getText().toString().trim();
                String phone = contact_number.getText().toString().trim();
                final String dateofbirth = DateofBirth.getText().toString().trim();
                final String place = InputLocation.getText().toString().trim();
                Gender = (RadioGroup) findViewById(R.id.gender);
                int selectedId = Gender.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                final String gender = (String) radioButton.getText();
                String description = Description.getText().toString().trim();
                String image = "";
                final String contact= ccp.getFullNumberWithPlus();
                ccp.registerCarrierNumberEditText(contact_number);
                int spaces = fullname.replace(" ", "").length();
                int password_spaces = password.replace(" ", "").length();
                int description_spaces = description.replace(" ", "").length();
                Log.e("spaces", String.valueOf(spaces));
                Log.e("fullNumber", String.valueOf(contact.length()));



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

//Profile image from Linked in to Bitmap
                Bundle bundle = getIntent().getExtras();
                String picture = bundle.getString("picturl");
                if(picture!=null){
                    Glide.with(RegisterActivity.this).load(picture).into(user_image);
                    user_image.buildDrawingCache();
                    Bitmap bitmap = user_image.getDrawingCache();
                    image = getStringImage(bitmap);
                }

                if (userRole.matches("1")) {
                    final String finalImage = image;
                    final String finalEmail = email;
                    final String finalPassword = password;
                    final String finalPhone = phone;

                    if (fullname.length() == 0) {
                        FullName.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        FullName.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (email.length() == 0) {
                        Email.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        Email.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (phone.length() == 0) {
                        phone_layout.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        phone_layout.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (password.length() == 0) {
                        Password.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        Password.setBackgroundResource(R.drawable.rectangular_edit2);
                    }
                    if (confirm_password.length() == 0) {
                        confirm_Password.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        confirm_Password.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (place.length() == 0) {
                        InputLocation.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        InputLocation.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (dateofbirth.length() == 0) {
                        DateofBirth.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        DateofBirth.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (!fullname.isEmpty() && !finalEmail.isEmpty() && !finalPassword.isEmpty() && !confirm_password.isEmpty() && !place.isEmpty() && !finalPhone.isEmpty() && !dateofbirth.isEmpty() && !gender.isEmpty() && !userRole.isEmpty()) {

                        if (TextUtils.isEmpty(description))
                            description = "";

                        if(spaces<3){
                            FullName.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(getApplicationContext(),
                                    "Minimum 3 characters are required for name", Toast.LENGTH_SHORT)
                                    .show();
                            return ;
                        }
                        if (!isValidEmail(email) && !finalEmail.isEmpty()) {
                            Email.setBackgroundResource(R.drawable.red_alert);
                            Log.e("check", "check");
                            Toast.makeText(RegisterActivity.this, "Please enter your valid email address", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (phone != null && contact.length()!=13) {
                            phone_layout.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Minimum 13 digits are required for contact number with country code", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if ( phone.length()==0) {
                            phone_layout.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Please enter your valid phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (password != null && password_spaces < 6) {
                            Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Minimum 6 characters required in password field.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!password.equals(confirm_password)) {
                            confirm_Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Password not matching.Please enter again", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (password != null && password_spaces > 50) {
                            Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Password should not exceed more than 50 characters!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!email.isEmpty()) {
                            final Dialog dialog = ObjectFactory.getInstance().getUtils(RegisterActivity.this).showLoadingDialog(RegisterActivity.this);
                            dialog.show();
                            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(RegisterActivity.this).getApiService().emailDescriptionValidation(
                                    "app-client",
                                    "123321",

                                    email, description
                            );

                            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    dialog.dismiss();
                                    if (response.body() != null) {
                                        try {
                                            String responseString = new String(response.body().bytes());
                                            if (responseString != null) {
                                                JSONObject jsonObject = new JSONObject(responseString);
                                                //System.out.println("PortfolioVideoEditActivity.onResponse " + responseString);
                                                Log.e("registerresult", responseString);

                                                if (jsonObject != null) {
                                                    if (!jsonObject.getBoolean("error")) {
                                                        registerUser(fullname, finalEmail, finalPassword, dateofbirth, place, contact, gender, userRole, finalImage);

                                                    }else{
                                                        Toast.makeText(RegisterActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "failed..", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter required details", Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    final String finalImage1 = image;
                    final String finalEmail1 = email;
                    final String finalPassword1 = password;
                    final String finalDescription1 = description;
                    final String finalPhone1 = phone;

                    if (fullname.length() == 0) {
                        FullName.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        FullName.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (email.length() == 0) {
                        Email.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        Email.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (phone.length() == 0) {
                        phone_layout.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        phone_layout.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (password.length() == 0) {
                        Password.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        Password.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (confirm_password.length() == 0) {
                        confirm_Password.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        confirm_Password.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (place.length() == 0) {
                        InputLocation.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        InputLocation.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (dateofbirth.length() == 0) {
                        DateofBirth.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        DateofBirth.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (description.length() == 0) {
                        Description.setBackgroundResource(R.drawable.red_alert);
                    }else{
                        Description.setBackgroundResource(R.drawable.rectangular_edit2);
                    }

                    if (!fullname.isEmpty() && !finalEmail1.isEmpty() && !finalPassword1.isEmpty() && !confirm_password.isEmpty() && !place.isEmpty() && !finalPhone1.isEmpty() && !dateofbirth.isEmpty() && !gender.isEmpty() && !userRole.isEmpty() && !finalDescription1.isEmpty()) {

                        if(spaces<3){
                            FullName.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(getApplicationContext(),
                                    "Minimum 3 characters are required for name", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (!isValidEmail(email) && !finalEmail1.isEmpty()) {
                            Email.setBackgroundResource(R.drawable.red_alert);
                            Log.e("check", "check");
                            Toast.makeText(RegisterActivity.this, "Please enter your valid email address", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (phone.length()==0) {
                            phone_layout.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Please enter your valid phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if((contact.length()!=13)){
                            Log.e("contactlenght", String.valueOf(contact.length()));
                            Log.e("problem1","problem1");
                            phone_layout.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Minimum 13 digits are required for contact number with country code", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (password != null && password_spaces < 6) {
                            Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Minimum 6 characters required in password field.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!password.equals(confirm_password)) {
                            confirm_Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Password not matching.Please enter again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (password != null && password.length() > 50) {
                            Password.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Password should not exceed more than 50 characters!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (description != null && description_spaces < 50) {
                            Description.setBackgroundResource(R.drawable.red_alert);
                            Toast.makeText(RegisterActivity.this, "Description should be minimum of 50 characters.", Toast.LENGTH_SHORT).show();
                            return;
                        }
//                        final Dialog dialog = ObjectFactory.getInstance().getUtils(RegisterActivity.this).showLoadingDialog(RegisterActivity.this);
//                        dialog.show();

                        if(!email.isEmpty()) {
                            if (TextUtils.isEmpty(description)) {
                                description = "";
                            }
                            final String finalDescription = description;

                            Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(RegisterActivity.this).getApiService().emailDescriptionValidation(
                                    "app-client",
                                    "123321",

                                    email, finalDescription
                            );

                            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    // dialog.dismiss();
                                    if (response.body() != null) {
                                        try {
                                            String responseString = new String(response.body().bytes());
                                            if (responseString != null) {
                                                JSONObject jsonObject = new JSONObject(responseString);
                                                System.out.println("PortfolioVideoEditActivity.onResponse " + responseString);
                                                if (jsonObject != null) {
                                                    if (!jsonObject.getBoolean("error")) {
                                                        Log.e("Role is checked", userRole);

                                                        Intent to_registercategory = new Intent(RegisterActivity.this, Registration_Category.class);
                                                        to_registercategory.putExtra("role", userRole);
                                                        to_registercategory.putExtra("fullname", fullname);
                                                        to_registercategory.putExtra("email", finalEmail1);
                                                        to_registercategory.putExtra("password", finalPassword1);
                                                        to_registercategory.putExtra("location", place);
                                                        to_registercategory.putExtra("phone", contact);
                                                        to_registercategory.putExtra("dob", dateofbirth);
                                                        to_registercategory.putExtra("description", finalDescription);
                                                        to_registercategory.putExtra("gender", gender);
                                                        to_registercategory.putExtra("profile_img", finalImage1);
                                                        startActivity(to_registercategory);
//                                                        finish();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // dialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "failed..", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter required details", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
            private boolean isValidEmail(String email) {
                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();
            }
        });
//        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),
//                        LoginActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });

        user_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int result = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA);
                int result1 = ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    if (result1 == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                        Glide.with(RegisterActivity.this).load(RegisterActivity.IMG_URL).fitCenter().into(user_image);
                    } else {
                        alert();
                    }
                } else {
                    buildAlertMessageNoCamera();
                }
            }
        });
    }

    private TextWatcher textWatcher7() {
        return new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Description.getText().toString().equals("")) { //if edittext include text
                    Description.setBackgroundResource(R.drawable.rectangular_edit2);
                }else{
                    Description.setBackgroundResource(R.drawable.red_alert);
                }
//
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

//    private TextWatcher textWatcher6() {
//        return new TextWatcher(){
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (!ccp.registerCarrierNumberEditText(contact_number)..equals("")) { //if edittext include text
//                    phone_layout.setBackgroundResource(R.drawable.rectangular_edit2);
//                }else{
//                    phone_layout.setBackgroundResource(R.drawable.red_alert);
//                }
////
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//    }


    private TextWatcher textWatcher5() {
        return new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!DateofBirth.getText().toString().equals("")) { //if edittext include text
                    DateofBirth.setBackgroundResource(R.drawable.rectangular_edit2);
                }else{
                    DateofBirth.setBackgroundResource(R.drawable.red_alert);
                }
//
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private TextWatcher textWatcher4() {

        return new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!InputLocation.getText().toString().equals("")) { //if edittext include text
                    InputLocation.setBackgroundResource(R.drawable.rectangular_edit2);
                }else{
                    InputLocation.setBackgroundResource(R.drawable.red_alert);
                }
//
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

                if (!confirm_Password.getText().toString().equals("")) { //if edittext include text
                    confirm_Password.setBackgroundResource(R.drawable.rectangular_edit2);
                }else{
                    confirm_Password.setBackgroundResource(R.drawable.red_alert);
                }
//
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

                if (!Password.getText().toString().equals("")) { //if edittext include text
                    Password.setBackgroundResource(R.drawable.rectangular_edit2);
                }else{
                    Password.setBackgroundResource(R.drawable.red_alert);
                }
//
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
        return super.dispatchTouchEvent(ev);
    }

    private TextWatcher textWatcher1() {
        return new TextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!Email.getText().toString().equals("")) { //if edittext include text
                    // btnClear1.setVisibility(View.VISIBLE);
                    Email.setBackgroundResource(R.drawable.rectangular_edit2);

                } else { //not include text
                    //  btnClear1.setVisibility(View.GONE);
                    Email.setBackgroundResource(R.drawable.red_alert);
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
//    private View.OnClickListener onClickListener1() {
//        return new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Email.setText("");
//            }
//        };
//    }

    //    private View.OnClickListener onClickListener() {
//        return new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                FullName.setText("");
//            }
//        };
//    }
    private TextWatcher textWatcher() {
        return new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!FullName.getText().toString().equals("")) { //if edittext include text
                    // btnClear.setVisibility(View.VISIBLE);
                    FullName.setBackgroundResource(R.drawable.rectangular_edit2);

                } else { //not include text
                    // btnClear.setVisibility(View.GONE);
                    FullName.setBackgroundResource(R.drawable.red_alert);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle(Constants.ProfileFragment.ADD_PHOTO);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(Constants.ProfileFragment.TAKE_PHOTO)) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //    buildAlertMessageNoCamera();
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        // Glide.with(getActivity()).load(ObjectFactory.getInstance().getLoginManager(getContext()).getUserPhoto()).fitCenter().into(mProfilePic);

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
        ActivityCompat.requestPermissions(RegisterActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);
    }

    private void buildAlertMessageNoCamera() {
        ActivityCompat.requestPermissions(RegisterActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("working","working");
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
                Glide.with(this).load(RegisterActivity.IMG_URL).fitCenter().into(user_image);
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
                Random rand = new Random();
                int num = rand.nextInt(5000) + 1;
                File path = new File(directory, "" + num + ".jpg");

                UCrop.of(Uri.fromFile(photopath), Uri.fromFile(path))
                        .withAspectRatio(9, 9)
                        .withMaxResultSize(200, 200)
                        .start(RegisterActivity.this);

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
                Glide.with(this).load(RegisterActivity.IMG_URL).fitCenter().into(user_image);
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("patientphoto", Context.MODE_PRIVATE);
                Random rand = new Random();
                int num = rand.nextInt(5000) + 1;
                File pathDest = new File(directory, "" + num + ".jpg");

                UCrop.of(Uri.fromFile(path), Uri.fromFile(pathDest))
                        .withAspectRatio(9, 9)
                        .withMaxResultSize(200, 200)
                        .start(RegisterActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            InputLocation.setText(place.getAddress());

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

    private void onClickListners() {
        tvHire.setOnClickListener(this);
        tvWork.setOnClickListener(this);
    }

    private void intiViews() {
        tvHire = (AppCompatTextView) findViewById(R.id.tvHire);
        tvWork = (AppCompatTextView) findViewById(R.id.tvWork);
    }

    private void registerUser(final String fullname, final String email, final String password, final String dateofbirth, final String place, final String phone, final String gender, String userRole, String finalImage) {
        final Dialog dialog = ObjectFactory.getInstance().getUtils(RegisterActivity.this).showLoadingDialog(RegisterActivity.this);
        dialog.show();
        if(finalImage.isEmpty()){
            finalImage="no";
        }
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(RegisterActivity.this).getApiService().signUp_hire(
                "app-client",
                "123321",
                email, fullname,place,phone,dateofbirth, gender, password, this.userRole,finalImage);
        //Log.e("profile_imagecheck",profile_image);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject != null) {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                //   startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                // startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
//            case R.id.fab:
//                animateFAB();
//                break;
//
//            case R.id.fab1:
//                break;
//            case R.id.fab2:
//                break;
            case R.id.tvWork:
                Description.setVisibility(View.VISIBLE);
                btnRegister.setText("Continue");
                userRole = "0";
                tvWork.setBackground(getResources().getDrawable(R.drawable.register_box));
                tvHire.setBackground(getResources().getDrawable(R.drawable.reg_box_hirer));
                tvWork.setTextColor(getResources().getColor(R.color.white));
                tvHire.setTextColor(getResources().getColor(R.color.portfolioText));
                break;
            case R.id.tvHire:
                btnRegister.setText("Sign Up");
                Description.setVisibility(View.GONE);
                tvHire.setBackground(getResources().getDrawable(R.drawable.register_box));
                tvWork.setBackground(getResources().getDrawable(R.drawable.reg_box_hirer));
                tvHire.setTextColor(getResources().getColor(R.color.white));
                tvWork.setTextColor(getResources().getColor(R.color.portfolioText));
                userRole = "1";
                break;
            default:
                break;
        }
    }

//    private void animateFAB() {
//        if (isFabOpen) {
//
//            fab.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);
//            family.setVisibility(GONE);
//            isFabOpen = false;
//        } else {
//            fab.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);
//            family.setVisibility(View.VISIBLE);
//            isFabOpen = true;
//        }
//    }

    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
}



