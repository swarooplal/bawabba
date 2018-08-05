package com.bawaaba.rninja4.rookie.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.dashboard.DashboardActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.Utilities;
import com.bawaaba.rninja4.rookie.firbase.Config;
import com.bawaaba.rninja4.rookie.firbase.NotificationUtils;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.sample.core.ui.activity.CoreBaseActivity;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

//import com.example.rninja4.rookie.MainActivity;
//import com.example.rninja4.rookie.helper.SQLiteHandler;

public class LoginActivity extends CoreBaseActivity implements IConsts {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private TextView forgotPassword;
    private TextView Faq;
    private TextView Termsofuse;
    private SessionManager session;
    private SQLiteHandler db;
    private ListView userListView;
    private TextInputLayout Textemail, Textpassword;
    EditText editText;
    private ImageButton btnClear;
    private LinearLayout linearLayout;
    private LinearLayout password_layout;
    private String strLogin = "arjunpv200@gmail.com";
    private String strPass = "asdasd";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public String reg_id;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        // ButterKnife.bind(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        TextView Signup = (TextView) findViewById(R.id.signup);
        forgotPassword = (TextView) findViewById(R.id.forgot);
        Faq = (TextView) findViewById(R.id.faq);
        Termsofuse = (TextView) findViewById(R.id.terms);
        btnClear = (ImageButton) findViewById(R.id.btn_clear);
        inputEmail = (EditText) findViewById(R.id.email);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        password_layout = (LinearLayout) findViewById(R.id.password_layout);
        Faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_login = new Intent(LoginActivity.this, Faqwebpage.class);
                startActivity(to_login);

            }
        });
        Termsofuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent to_login = new Intent(LoginActivity.this, TermsOfService.class);
                startActivity(to_login);

            }
        });

        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        inputEmail.addTextChangedListener(textWatcher());
        btnClear.setOnClickListener(onClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        // check issue for old API
        SpannableString WordtoSpan = SpannableString.valueOf(Signup.getText());
        WordtoSpan.setSpan(new ForegroundColorSpan(getColor(R.color.textdark)), 22, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Signup.setText(WordtoSpan);

        ImageButton home = (ImageButton) findViewById(R.id.home);
        inputEmail = (EditText) findViewById(R.id.email);
        // Textemail = (TextInputLayout) findViewById(R.id.textemail);
        inputPassword = (EditText) findViewById(R.id.password);
        // Textpassword = (TextInputLayout) findViewById(R.id.textpassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
               /*btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);*/
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_login = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(to_login);
                finish();
            }
        });

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            if(getIntent()==null || !getIntent().getBooleanExtra(EXTRA_ONLY_NEED_RESULT,false)) {
                // User is already logged in. Take him to profile activity
                AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                /*Intent intent = new Intent(LoginActivity.this, ProfileView.class);
                startActivity(intent);*/
            }
            finish();
        }

        inputEmail.setText(strLogin);
        inputPassword.setText(strPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Utilities.hideKeyBoard(LoginActivity.this);
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (email.length() == 0) {
                    inputEmail.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(getApplicationContext(),
                            "Please fill required fields", Toast.LENGTH_LONG)
                            .show();
                } else {
                    inputEmail.setBackgroundResource(R.drawable.rectangular_edit2);
                }
                if (password.length() == 0) {
                    password_layout.setBackgroundResource(R.drawable.red_alert);
                    Toast.makeText(getApplicationContext(),
                            "Please fill required fields", Toast.LENGTH_LONG)
                            .show();
                } else {
                    password_layout.setBackgroundResource(R.drawable.rectangular_edit2);
                }

                //Log.e("Subcategory",String.valueOf(email));
                if (!email.isEmpty() || !password.isEmpty()) {
                    if (!isValidEmail(email)) {
                        inputEmail.setBackgroundResource(R.drawable.red_alert_round);
                        Toast.makeText(getApplicationContext(),
                                "Please enter your valid email address", Toast.LENGTH_LONG)
                                .show();
                    } else if (!isValidPassword(password)) {
                        // inputPassword.setError("Invalid Password");
                        inputPassword.setBackgroundResource(R.drawable.red_alert_round);
                    } else {
                        checkLogin(email, password);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please fill required fields", Toast.LENGTH_LONG)
                            .show();
                }
            }

            private boolean isValidEmail(String email) {

                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();

            }

            private boolean isValidPassword(String password) {
                if (password != null && password.length() >= 6) {
                    return true;
                }
                return false;
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
            }
        });

        /*
        swaroop :

        firebase broadcast receiver
         */

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

        displayFirebaseRegId();

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        reg_id = pref.getString("regId", null);
      Log.d("regid.....",reg_id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        displayFirebaseRegId();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private View.OnClickListener onClickListener() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                inputEmail.setText("");
            }
        };
    }

    private TextWatcher textWatcher() {

        return new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!inputEmail.getText().toString().equals("")) { //if edittext include text
                    btnClear.setVisibility(View.VISIBLE);

                } else { //not include text
                    btnClear.setVisibility(View.GONE);


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


//    private void buildUsersList() {
//
//        List<String> tags = new ArrayList<>();
//
//        tags.add(Appquick.getSampleConfigs().getUsersTag());
//
//        QBUsers.getUsersByTags(tags, null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
//            @Override
//            public void onSuccess(ArrayList<QBUser> result, Bundle params) {
//                UsersAdapter adapter = new UsersAdapter(LoginActivity.this, result);
//                userListView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                ErrorUtils.showSnackbar(userListView, R.string.login_cant_obtain_users, e,
//                        R.string.dlg_retry, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                buildUsersList();
//                            }
//                        });
//            }
//        });
//
//    }
//    @OnClick(R.id.btnLogin)
//    public void onLoginClick(){
//
//    }


    private void qbSignin() {
        try {
            final String uuid = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId();
            QBUser qbUser = new QBUser(uuid, QB_PASSWORD);
            QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                }

                @Override
                public void onError(QBResponseException errors) {
                    qbSignup();
                }
            });
        } catch (Exception e) {
        }
    }

    private void qbSignup() {
        try {
            QBUser qbUser = new QBUser();
            final String uid = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId();
            qbUser.setLogin(uid);
            qbUser.setPassword(QB_PASSWORD);
            qbUser.setFullName(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserName());
            StringifyArrayList<String> tagList = new StringifyArrayList<>();
            tagList.add("dev");
            qbUser.setTags(tagList);
            String fullProfileImg = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileImg();
            if (fullProfileImg.startsWith("http://test378.bawabba.com") || fullProfileImg.startsWith("http://test378.bawabba.com")) {
                qbUser.setCustomData(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getShortProfileImg());
            } else {
                qbUser.setCustomData(fullProfileImg);
            }

            QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                }

                @Override
                public void onError(QBResponseException error) {
                }
            });
        } catch (Exception e) {
        }
    }
    private void checkLogin(final String email, final String password) {
        pDialog.setMessage("Logging in ...");
        showDialog();
Log.e("Check","started");
        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().login("app-client",
                "123321",email,password,reg_id,"android"
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideDialog();

                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jObj = new JSONObject(responseString);
                        boolean error = jObj.getBoolean("error");
                        if (!error)
                        {
                            session.setLogin(true);
                            JSONObject user = jObj.getJSONObject("user");
                            String name = user.getString("name");
                            String uid = user.getString("uid");
                            String email = user.getString("email");
                            String token = user.getString("token");
                            String verify_code = user.getString("verify_code");
                            String profile_img = user.getString("profile_img");
                            int index = profile_img.lastIndexOf("/");
                            String shortUrl = profile_img.substring(index + 1, profile_img.length());
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveLogin(true);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveLoginToken(token);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveUserId(uid);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveUserName(name);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setEmail(email);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveProfileImg(profile_img);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveShortProfileImg(shortUrl);
                            qbSignin();
                            boolean result = db.addUser(name, email, uid, token, verify_code, password);
                            if (result) {
                                if(getIntent()==null || !getIntent().getBooleanExtra(EXTRA_ONLY_NEED_RESULT,false)) {
                                    AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                                    BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                                    /*Intent intent = new Intent(LoginActivity.this, ProfileView.class);
                                    startActivity(intent);*/
                                }
                                finish();
                            }

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                try {
                    Log.e("eee","",t);
                    hideDialog();
                } catch (Exception e) {
                }
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
    public void onBackPressed() {
       if(getIntent()!=null && getIntent().getBooleanExtra(EXTRA_ONLY_NEED_RESULT,false)) {
           super.onBackPressed();
       } else {
           //swaroop commented
           // perviously redirected log in actvity
//           Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
           BaseBottomHelperActivity.start(LoginActivity.this,null,null,null);
          /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
           startActivity(intent);*/
           finish();
       }



    }


    public static final String EXTRA_ONLY_NEED_RESULT="extra_need_result";

}











