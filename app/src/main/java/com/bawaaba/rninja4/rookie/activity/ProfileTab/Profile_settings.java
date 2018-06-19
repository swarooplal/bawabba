package com.bawaaba.rninja4.rookie.activity.ProfileTab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bawaaba.rninja4.rookie.Account.Account_settings;
import com.bawaaba.rninja4.rookie.Account.BlockList;
import com.bawaaba.rninja4.rookie.Account.Deactivate_Profile;
import com.bawaaba.rninja4.rookie.Account.GeneralSettingsActivity;
import com.bawaaba.rninja4.rookie.Account.Verfy_Profile1;
import com.bawaaba.rninja4.rookie.Account.Verify_Profile;
import com.bawaaba.rninja4.rookie.Account.Verify_profil2;
import com.bawaaba.rninja4.rookie.Account.Verify_profile3;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.firbase.Config;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.gdacciaro.iOSDialog.iOSDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Profile_settings extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private SQLiteHandler db;
    private SessionManager session;

    private Button cancel;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);
        bottomNavigationView.setFirstSelectedPosition(3);
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(3)
                .initialise();

        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent to_main = new Intent(Profile_settings.this, MainActivity.class);
                        startActivity(to_main);
                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(Profile_settings.this, SearchActivity.class);
                        startActivity(to_search);
                        finish();
                        break;
                    case 2:
                        Intent to_inbox = new Intent(Profile_settings.this, ChatActivity.class);
                        startActivity(to_inbox);
                        finish();
                        break;
                    case 3:
                        Intent to_profile = new Intent(Profile_settings.this, ProfileView.class);
                        startActivity(to_profile);
                        finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        getSupportActionBar().hide();
        Intent from_profile = getIntent();
        final String verify = from_profile.getStringExtra("verify");
        final String profile_image = from_profile.getStringExtra("profile_img");
        //Log.e("verifyyyyyyydg",profile_image);
        mainListView = (ListView) findViewById(R.id.mainListView);
        String[] account = new String[]{"General Settings", "Account Settings","Blocking", "Verify Profile", "Deactivate Profile",
                "Logout"};
        int[] drawableIds = {R.drawable.generalsettings, R.drawable.account_padlock,R.drawable.settings_block, R.drawable.verifyid_image, R.drawable.deactivate_image, R.drawable.logout_profile};
        CustomAdapter adapter = new CustomAdapter(this, account, drawableIds);
        ArrayList<String> profileList = new ArrayList<String>();
        profileList.addAll(Arrays.asList(account));

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, profileList);
        mainListView.setAdapter(adapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent[] intent = {null};
                switch (position) {

                    case 0:
                        intent[0] = new Intent(Profile_settings.this, GeneralSettingsActivity.class);
                        //  finish();
                        // for general settings
                        break;


                    case 1:
                        intent[0] = new Intent(Profile_settings.this, Account_settings.class);
                        // for account settings
                        // finish();
                        break;

                    case 2:
                        intent[0] = new Intent(Profile_settings.this, BlockList.class);
                        //  finish();
                        // for  deactivate profile
                        break;


                    case 3:
                        if (verify.matches("0")) {
                            intent[0] = new Intent(Profile_settings.this, Verify_profile3.class);// for verify profile
                            intent[0].putExtra("profile_img", profile_image);
                            //   finish();
                            break;

                        }
                        if (verify.matches("1")) {

                            intent[0] = new Intent(Profile_settings.this, Verify_profil2.class); // for verify profile
                            //   finish();
                            break;
                        } else if (verify.matches("2")) {
                            intent[0] = new Intent(Profile_settings.this, Verfy_Profile1.class); // for verify profile
                            finish();
                            break;
                        } else {
                            intent[0] = new Intent(Profile_settings.this, Verify_Profile.class); // for verify profile
                            // finish();
                            break;
                        }
                    case 4:
                        intent[0] = new Intent(Profile_settings.this, Deactivate_Profile.class);
                        //  finish();
                        // for  deactivate profile
                        break;
                    case 5:
                        final iOSDialog iOSDialog = new iOSDialog(Profile_settings.this);
                        iOSDialog.setTitle("Logout");
                        iOSDialog.setSubtitle("Are you sure you want to logout?");
                        iOSDialog.setNegativeLabel("Cancel");
                        iOSDialog.setPositiveLabel("Logout");
                        iOSDialog.setBoldPositiveLabel(true);

                        iOSDialog.setNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                iOSDialog.dismiss();
                            }
                        });
                        iOSDialog.setPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                logout_token();
                            }
                        });
                        iOSDialog.show();
                }

                startActivity(intent[0]);
                //finish();
            }
        });
    }
    private void logout_token() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
       String reg_id = pref.getString("regId", null);

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(Profile_settings.this).getApiService().logout(
                "app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),

                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),reg_id);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                // dialog.dismiss();
                if (response.body()!= null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = new JSONObject(responseString);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {

                                Log.e("working", String.valueOf(jsonObject));
                                Toast.makeText(Profile_settings.this, "You have logged out", Toast.LENGTH_SHORT).show();

                                // Toast.makeText(Profile_settings.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                db.deleteUsers();
                                session.setLogin(false);
                                Intent intent = new Intent(Profile_settings.this, MainActivity.class);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Log.e("working", "forget");
                                Toast.makeText(Profile_settings.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Profile_settings.this, "Network Error", Toast.LENGTH_SHORT).show();
                //   dialog.dismiss();
            }
        });
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception ignored) {
        }
    }

//    @Override
//    public void onBackPressed() {
//            super.onBackPressed();
//        }

}

