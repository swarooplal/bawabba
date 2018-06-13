package com.bawaaba.rninja4.rookie;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bawaaba.rninja4.rookie.App.AppConfig;
import com.bawaaba.rninja4.rookie.App.AppController;
import com.bawaaba.rninja4.rookie.activity.SearchResult;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bawaaba.rninja4.rookie.utils.Constants.BASE_URL;

public class SkillView extends ListActivity {
    private String TAG = SkillView.class.getSimpleName();
    JSONParser jsonParser = new JSONParser();
    ArrayList skillsList;
    ListView androidListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_list);
        androidListView = (ListView) findViewById(android.R.id.list);
        skillsList = new ArrayList<>();
        pDialog = new ProgressDialog(SkillView.this);
        androidListView.addHeaderView(new View(getApplicationContext()));
        androidListView.addFooterView(new View(getApplicationContext()));
        new LoadSkill().execute();
    }

    private void searchUser(final String keyword, final String skills, final String location) {

        if (keyword.isEmpty() && skills.isEmpty() && location.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter some values", Toast.LENGTH_LONG).show();
        } else {
            final Dialog dialog = ObjectFactory.getInstance().getUtils(SkillView.this).showLoadingDialog(SkillView.this);
            dialog.show();
            String tag_string_req = "req_search";
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SEARCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e(TAG, "Search Responses: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).setSearchResult(response);

                            JSONArray user = jObj.getJSONArray("user");

                            Intent to_searchresult = new Intent(SkillView.this, SearchResult.class);
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
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No profiles are available!", Toast.LENGTH_LONG).show();

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

    private class LoadSkill extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading Skills ...");
            //            pDialog.setIndeterminate(false);
            //            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... args) {
            Intent from_subcategory = getIntent();
            String subcat_name = from_subcategory.getStringExtra("subcat_name");

            String Client_service = "app-client";
            String Auth_key = "123321";
            String token = "";
            String user_id = "";
            // Log.e("Subcategory58", subcat_name);

            String SUBCATEGORY_URL = null;
            try {
                SUBCATEGORY_URL =BASE_URL+"api/user/getskills?sub_cat=" + URLEncoder.encode(subcat_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(SUBCATEGORY_URL, "GET",
                    params, Client_service, Auth_key, token, user_id);
            try {
                JSONArray skills = json.getJSONArray("skills");
                for (int i = 0; i < skills.length(); i++) {
                    String d = skills.getString(i);
                    if (d != null) {
                        HashMap<String, String> skill1 = new HashMap<>();
                        skill1.put("skills", d);
                        skillsList.add(skill1);
                    }
                }
                Log.e("value", String.valueOf(skills));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapterViewAndroid = new SimpleAdapter(
                            SkillView.this, skillsList, R.layout.activity_skill, new String[]{"skills"}, new int[]{R.id.skill});
                    // updating listview
                    setListAdapter(adapterViewAndroid);
                    androidListView.setAdapter(adapterViewAndroid);
                    androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            HashMap<String, String> map = (HashMap<String, String>) androidListView.getItemAtPosition(position);

                            String skills = String.valueOf(skillsList.get((int) id)).substring(String.valueOf(skillsList.get((int) id)).indexOf("=") + 1, String.valueOf(skillsList.get((int) id)).indexOf("}"));
                            String keyword = "";
                            String location = "";
                            searchUser(keyword, skills, location);
                        }
                    });

                }

            });

        }
    }
}

