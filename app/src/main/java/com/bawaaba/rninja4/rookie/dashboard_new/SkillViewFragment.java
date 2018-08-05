package com.bawaaba.rninja4.rookie.dashboard_new;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.SkillView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bawaaba.rninja4.rookie.utils.Constants.BASE_URL;

public class SkillViewFragment extends Fragment {

    private RecyclerView rvList;
    private ProgressBar progressBar;
    private boolean isCallingWeb;
    private String subCategoryName;

    public static SkillViewFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        SkillViewFragment fragment = new SkillViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_skill_view, container, false);
        subCategoryName = getArguments().getString("title");
        init(v);
        return v;
    }

    private void init(View v) {
        rvList = v.findViewById(R.id.rvList);
        progressBar = v.findViewById(R.id.progressBar);

        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        if(!isCallingWeb) {
            new LoadSkill().execute();
        }

    }

    private void setList(List<String> list) {
        try {
            if (list == null) {
                Toast.makeText(getContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
            } else if (list.isEmpty()) {
                Toast.makeText(getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
            } else {
                rvList.setAdapter(new SkillsAdapter(list));
            }
        } catch (Exception e) {
        }
    }

    private class LoadSkill extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressBar.setVisibility(View.VISIBLE);
                isCallingWeb=true;
            } catch (Exception e) {
            }
        }


        @Override
        protected List<String> doInBackground(Void... args) {


            try {
                String Client_service = "app-client";
                String Auth_key = "123321";
                String token = "";
                String user_id = "";
                // Log.e("Subcategory58", subcat_name);

                String SUBCATEGORY_URL = null;
                try {
                    SUBCATEGORY_URL = BASE_URL + "api/user/getskills?sub_cat=" + URLEncoder.encode(subCategoryName, "UTF-8");
                } catch (Exception e) {
                }
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = new JSONParser().makeHttpRequest(SUBCATEGORY_URL, "GET",
                        params, Client_service, Auth_key, token, user_id);
                Log.e("Json", "=" + json.toString());
                JSONArray skills = json.getJSONArray("skills");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < skills.length(); i++) {
                    String d = skills.getString(i);
                    if (d != null) {
                        list.add(d);
                    }
                }
                return list;
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            try {
                isCallingWeb=false;
                progressBar.setVisibility(View.GONE);
                setList(s);
            } catch (Exception e) {
            }
            /*pDialog.dismiss();

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

            });*/

        }
    }
}
