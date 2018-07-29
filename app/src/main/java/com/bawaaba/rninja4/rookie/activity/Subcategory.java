package com.bawaaba.rninja4.rookie.activity;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.SkillView;
import com.bawaaba.rninja4.rookie.dashboard_new.BaseBottomHelperActivity;
import com.bawaaba.rninja4.rookie.dashboard_new.ChatFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.ProfileViewFragment;
import com.bawaaba.rninja4.rookie.dashboard_new.SearchFragment;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.Utils;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bawaaba.rninja4.rookie.utils.Constants.BASE_URL;

public class Subcategory extends TabActivity {

    JSONParser jsonParser = new JSONParser();
    ArrayList<String> subcategoryList = new ArrayList<String>();
    ArrayList<String> skillList = new ArrayList<String>();
    int onStartCount = 0;
    GridView androidGridView;
    private String TAG = Subcategory.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private DrawerLayout mDrawer;
    private Toolbar toolbar2;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private SQLiteHandler db;
    private SessionManager session;
    private TextBadgeItem textBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");
        textBadgeItem = Utils.getTextBadge();

//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        nvDrawer = (NavigationView) findViewById(R.id.nvView);
//        setupDrawerContent(nvDrawer);
        BottomNavigationBar bottomNavigationView = (BottomNavigationBar)
                findViewById(R.id.bottom_bar);
        bottomNavigationView.setFirstSelectedPosition(3);
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        BaseBottomHelperActivity.start(getApplicationContext(),null,null,null);
                        /*Intent to_main = new Intent(Subcategory.this, MainActivity.class);
                        startActivity(to_main);*/
                        finish();
                        break;
                    case 1:
                        BaseBottomHelperActivity.start(getApplicationContext(), SearchFragment.class.getName(),null,null);
                        /*Intent to_search = new Intent(Subcategory.this, SearchActivity.class);
                        startActivity(to_search);*/
                        finish();
                        break;
                    case 2:
                        if (session.isLoggedIn() && db_id != null) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
                            BaseBottomHelperActivity.start(getApplicationContext(), ChatFragment.class.getName(),null,null);
                           /* Intent to_inbox = new Intent(Subcategory.this, com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity.class);
                            startActivity(to_inbox);*/
                            finish();
                        } else {
                            Intent to_login = new Intent(Subcategory.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
                        break;
                    case 3:
                        AppPreference appPreference=ObjectFactory.getInstance().getAppPreference(getApplicationContext());
                        BaseBottomHelperActivity.start(getApplicationContext(), ProfileViewFragment.class.getName(),appPreference.getUserId(),appPreference.getUserName());
                        /*Intent to_profile = new Intent(Subcategory.this, ProfileView.class);
                        startActivity(to_profile);*/
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

        //For Animation
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }

        final ImageButton home = (ImageButton) findViewById(R.id.home);
//        final ImageButton reg = (ImageButton) findViewById(R.id.register);
//        ImageButton search = (ImageButton) findViewById(R.id.search);


//        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar2, R.string.drawer_open, R.string.drawer_close) {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//            }
//        };
//        mDrawer.addDrawerListener(drawerToggle);
//        mDrawer.setScrimColor(getResources().getColor(android.R.color.secondary_text_dark));
//        mDrawer.post(new Runnable() {
//            @Override
//            public void run() {
//                drawerToggle.syncState();
//            }
//        });
        new GetCategory(this).execute();
    }


//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//
//            int position = 0;
//
//            @Override
//            public boolean onNavigationItemSelected(final MenuItem menuItem) {
//                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Intent to_subcategory = new Intent(Subcategory.this, Subcategory.class);
//                        to_subcategory.putExtra("id", position);
//                        startActivity(to_subcategory);
//                        finish();
//                        return true;
//                    }
//                });
//
//                switch (menuItem.getItemId()) {
//                    case R.id.nav_menu1:
//                        position = 0;
//                        break;
//                    case R.id.nav_menu2:
//                        position = 1;
//                        break;
//                    case R.id.nav_menu3:
//                        position = 2;
//                        break;
//                    case R.id.nav_menu4:
//                        position = 3;
//                        break;
//                    case R.id.nav_menu5:
//                        position = 4;
//                        break;
//                    case R.id.nav_menu6:
//                        position = 5;
//                        break;
//                    case R.id.nav_menu7:
//                        position = 6;
//                        break;
//                    case R.id.nav_menu8:
//                        position = 7;
//                        break;
//                    case R.id.nav_menu9:
//                        position = 8;
//                        break;
//                    case R.id.nav_menu10:
//                        position = 9;
//                        break;
//                    case R.id.nav_menu11:
//                        position = 10;
//                        break;
//                    case R.id.nav_menu12:
//                        position = 11;
//                        break;
//                    case R.id.nav_menu13:
//                        position = 12;
//                        break;
//                    case R.id.nav_menu14:
//                        position = 13;
//                        break;
//                    case R.id.nav_menu15:
//                        position = 14;
//                        break;
//                    case R.id.nav_menu16:
//                        position = 15;
//                        break;
//                    case R.id.nav_menu17:
//                        position = 16;
//                        break;
//                    case R.id.nav_menu18:
//                        position = 17;
//                        break;
//                    case R.id.nav_menu19:
//                        position = 18;
//                        break;
//                    case R.id.nav_menu20:
//                        position = 19;
//                        break;
//                }
//
//                // Log.e(TAG, "value" + menuItem);
//                return true;
//            }
//
//
//        });
//    }

    @Override
    protected void onPause() {
        super.onPause();
        unread_notification();
    }

    @Override
    protected void onResume() {
        super.onResume();


        unread_notification();
    }

    private void unread_notification() {

        Call<ResponseBody> responseBodyCall = ObjectFactory.getInstance().getRestClient(getApplicationContext()).getApiService().notification("app-client",
                "123321",
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getLoginToken(),
                ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUserId());

        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response)
            {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        if (responseString != null) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseString);
                                String count=jsonObject.getString("count");
                                if(!count.equals("0")){
                                    textBadgeItem.setText(count);
                                    textBadgeItem.show(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private class GetCategory extends AsyncTask<Void, Void, Void> {
        TabHost tabHost = getTabHost();
        private Context context;

        public GetCategory(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // pDialog.setMessage("Loading Skills ...");
            pDialog = new ProgressDialog(Subcategory.this);
               /*pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Intent from_mainactivity = getIntent();
            final int category_id = from_mainactivity.getIntExtra("id", 0);
            String Client_service = "app-client";
            String Auth_key = "123321";
            String token = "";
            String user_id = "";
            String Subcategory_URL = BASE_URL + "api/user/categoryskills/?category=" + category_id;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(Subcategory_URL, "GET", params, Client_service, Auth_key, token, user_id);
            Log.e("ssssssss", "="+json.toString());
            if (json != null) {
                try {
                    JSONArray subcategory = json.getJSONArray("subcategory");
                    Log.e("Subcategory8", String.valueOf(subcategory));
                    for (int i = 0; i < subcategory.length(); i++) {
                        String subcategory_name = subcategory.getString(i);
                        if (subcategory_name != null) {
                            subcategoryList.add(String.valueOf(subcategory_name));
                        }
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "cat id." + category_id);

                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < subcategoryList.size(); j++) {
                        String subcategory_name = subcategoryList.get(j); //s is subcategory name
                        Log.e("Subcategory100", "valueof s" + String.valueOf(subcategory_name));
                        String SUBCATEGORY_SPEC = subcategory_name;
                        TabHost.TabSpec tab = tabHost.newTabSpec(SUBCATEGORY_SPEC);
                        tab.setIndicator(SUBCATEGORY_SPEC, getResources().getDrawable(R.drawable.icon_tab));
                        Intent to_skillview = new Intent(Subcategory.this, SkillView.class);
                        to_skillview.putExtra("subcat_name", SUBCATEGORY_SPEC);
                        tab.setContent(to_skillview);
                        tabHost.addTab(tab);
                        tab.setIndicator(getTabIndicator(tabHost.getContext()));
                    }
                }
            });
            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                        // tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000")); // unselected
                        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                        tv.setTextColor(Color.parseColor("#dfdfdf"));
                        //  tv.setTextSize(8);
                    }
                    //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#0000FF")); // selected
                    TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                    tv.setTextColor(Color.parseColor("#00BFFF"));

                    final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
                    for (int i = 0; i < tw.getChildCount(); ++i)
                    {
                        final View tabView = tw.getChildTabViewAt(i);
                        final TextView ts = (TextView)tabView.findViewById(android.R.id.title);
                        ts.setTextSize(13);
                    }
                }
            });
            return null;
        }
        private View getTabIndicator(Context context) {

            View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);

            return view;
        }
    }
}