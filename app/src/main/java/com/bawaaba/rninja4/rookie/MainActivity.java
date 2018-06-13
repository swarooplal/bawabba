package com.bawaaba.rninja4.rookie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.App.AppConfig;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.activity.LoginActivity;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.SearchActivity;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;
import com.bawaaba.rninja4.rookie.helper.SessionManager;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.model.searchResult.SearchResultResponse;
import com.bawaaba.rninja4.rookie.utils.IConsts;
import com.bawaaba.rninja4.rookie.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements IConsts {

    RecyclerView recyclerView;
    MycustomAdapter adapter;
    Toolbar toolbar;
    GridView androidGridView;

    private SQLiteHandler db;
    private SessionManager session;
    private AppCompatEditText tvSearchHere;
    private TextBadgeItem textBadgeItem;

    ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
        @Override
        public Shader resize(int width, int height) {
            LinearGradient lg = new LinearGradient(0, 0, width, height,
                    new int[]{Color.GREEN, Color.GREEN, Color.WHITE, Color.WHITE},
                    new float[]{0, 0.5f, .55f, 1}, Shader.TileMode.REPEAT);
            return lg;
        }
    };
    PaintDrawable paintDrawable = new PaintDrawable();

    {
        paintDrawable.setShape(new RectShape());
        paintDrawable.setShaderFactory(shaderFactory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        final String db_id = user.get("uid");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actiontitle_layout);
        ActionBar actionBar = getSupportActionBar();
        //  actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        tvSearchHere = (AppCompatEditText) findViewById(R.id.tvSearchHere);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        tvSearchHere.setInputType(InputType.TYPE_CLASS_TEXT);
//        tvSearchHere.requestFocus();
//        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mgr.showSoftInput(tvSearchHere, InputMethodManager.SHOW_FORCED);

//        tvSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    if (!TextUtils.isEmpty(tvSearchHere.getText().toString().trim()))
//                        searchUser(tvSearchHere.getText().toString().trim(), "", "");
//                }
//                return false;
//            }
//
//
//        });
        tvSearchHere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchUser(tvSearchHere.getText().toString().trim(), "", "");
                    return true;
                }
                return false;
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });

//        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(tvSearchHere.getWindowToken(), 0);
//        imm.showSoftInput(tvSearchHere, 0);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new MycustomAdapter(this, Data.getdata());
        recyclerView.setAdapter(adapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mgridLayoutManager);
        EventBus.getDefault().register(this);

        BottomNavigationBar bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setFirstSelectedPosition(0);
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveCurrentActivity("MainActivity");
        textBadgeItem = Utils.getTextBadge();
        isMessageArrived();
        bottomNavigationView
                .addItem(new BottomNavigationItem(R.drawable.ic_home1, "Home").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_search1, "Search").setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_inbox1, "Inbox").setBadgeItem(textBadgeItem).setActiveColorResource(R.color.bottomnavigation))
                .addItem(new BottomNavigationItem(R.drawable.ic_profile, "Profile").setActiveColorResource(R.color.bottomnavigation))
                .setFirstSelectedPosition(0)
                .initialise();
        isMessageArrived();
        bottomNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        Intent to_main = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(to_main);

                        finish();
                        break;
                    case 1:
                        Intent to_search = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(to_search);
                        finish();
                        break;

                    case 2:
                        if (session.isLoggedIn() && db_id != null) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(false);
                            Intent to_inbox = new Intent(MainActivity.this, com.bawaaba.rninja4.rookie.activity.ChatFunction.ChatActivity.class);
                            startActivity(to_inbox);
                            finish();
                        } else {
                            Intent to_login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
                        break;
                    case 3:
                        if (session.isLoggedIn() && db_id != null) {
                            Intent to_profile = new Intent(MainActivity.this, ProfileView.class);
                            startActivity(to_profile);
                            finish();
                        }else{
                            Intent to_login = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(to_login);
                            finish();
                        }
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

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        isMessageArrived();
    }

    public void isMessageArrived() {
        try {
            boolean isMessageArrived = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).isNewMessageArrived();
            if (isMessageArrived) {
                //showUnreadMessages();
                int total = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getUnreadMessage();
                if (total > 0) {
                    textBadgeItem.setText("" + total);
                    textBadgeItem.show(false);
                } else {
                    hideText();
                }
            } else {
                hideText();
            }
        } catch (Exception e) {
        }
    }
    private void hideText(){
        textBadgeItem.setText("");
        textBadgeItem.hide();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void searchUser(final String keyword, final String skills, final String location) {
        if (keyword.isEmpty() && skills.isEmpty() && location.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter some values", Toast.LENGTH_LONG).show();
        } else {
            String tag_string_req = "req_search";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SEARCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            SearchResultResponse resultResponse = new Gson().fromJson(response, SearchResultResponse.class);
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setSearchResult(response);
                            JSONArray user = jObj.getJSONArray("user");

                            Intent to_searchresult = new Intent(MainActivity.this, SearchResult.class);
                            to_searchresult.putExtra("search_result", user.toString());
                            startActivity(to_searchresult);

                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
//
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to search url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("keyword", keyword);
                    params.put("skills", skills);
                    params.put("location", location);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}














