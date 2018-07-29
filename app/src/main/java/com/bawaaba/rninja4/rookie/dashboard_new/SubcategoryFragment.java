package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.JSONParser;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.SkillView;
import com.bawaaba.rninja4.rookie.activity.Subcategory;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bawaaba.rninja4.rookie.utils.Constants.BASE_URL;

public class SubcategoryFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int categoryid;
    private PagerAdapter adapter;
    private TabLayout.OnTabSelectedListener tabSelectedListener;


    public static SubcategoryFragment newInstance(int categoryId) {

        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        SubcategoryFragment fragment = new SubcategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_dash_subcategory, container, false);
        categoryid = getArguments().getInt("categoryId", 0);
        initViews(v);
        if (adapter == null  || adapter.getList().isEmpty())
            new GetCategory().execute();
        else
            setupTabs(adapter.getList());
        return v;
    }

    private void initViews(View v) {
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
    }

    private void setupTabs(ArrayList<String> list) {
        try {
            if (list == null) {
                Toast.makeText(getContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else if (list.isEmpty()) {
                Toast.makeText(getContext(), "Nothing found", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                adapter = new PagerAdapter(getChildFragmentManager(), list);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                if (tabSelectedListener == null)
                    tabSelectedListener = new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            setTabPress(tab, true);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            setTabPress(tab, false);
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    };
                tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                tabLayout.addOnTabSelectedListener(tabSelectedListener);
                for (int i = 0, j = list.size(); i < j; i++) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, null);
                    TextView txtTitle = view.findViewById(R.id.textView);
                    txtTitle.setText(list.get(i));
//            ((TextView) view.findViewById(R.id.txtTitle)).setText(model.getTitleId());
                    tabLayout.getTabAt(i).setCustomView(view);
                }

            }
        } catch (Exception e) {
        }

    }

    private void setTabPress(TabLayout.Tab tab, boolean isSelected) {
        try {
            View view = tab.getCustomView();
            view.findViewById(R.id.textView).setSelected(isSelected);
        } catch (Exception ignored) {
        }
    }


    private class GetCategory extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((BaseBottomHelperActivity) getActivity()).showProgress("Fetching Details", false);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {


            try {
                String Client_service = "app-client";
                String Auth_key = "123321";
                String token = "";
                String user_id = "";
                String Subcategory_URL = BASE_URL + "api/user/categoryskills/?category=" + categoryid;

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = new JSONParser().makeHttpRequest(Subcategory_URL, "GET", params, Client_service, Auth_key, token, user_id);
                ArrayList<String> subcategoryList = new ArrayList<>();
                try {
                    JSONArray subcategory = json.getJSONArray("subcategory");
                    for (int i = 0; i < subcategory.length(); i++) {
                        String subcategory_name = subcategory.getString(i);
                        if (subcategory_name != null) {
                            subcategoryList.add(String.valueOf(subcategory_name));
                        }
                    }
                } catch (final JSONException e) {
                }
                return subcategoryList;

            } catch (Exception e) {
                Log.e("Async", "", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            try {
                if (getActivity() != null) {
                    ((BaseBottomHelperActivity) getActivity()).dismissProgress();
                    setupTabs(strings);
                }
            } catch (Exception e) {
            }

        }
    }


}
