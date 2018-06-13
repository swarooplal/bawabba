package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.Skills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 18-02-2018.
 */

public class ActivitySearchSetSkills extends AppCompatActivity {
    private List<Skills> skillList;
    private List<Skills> mValues;
    ArrayList<String>strSkills;
    private List<Skills> filteredList;
    public SimpleItemRecyclerViewAdapter mAdapter;
    // Search edit box
    private EditText searchBox;
    private boolean isMultiSelect = true;
    public List<Integer> selectedIds = new ArrayList<>();
    RecyclerView recyclerView;
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_set_skills);
        getSupportActionBar().hide();
        strSkills=new ArrayList<String>();
        new HttpGetTask().execute();
        btnDone= (Button) findViewById(R.id.btnDone);
        searchBox = (EditText) findViewById(R.id.search_box);
        recyclerView= (RecyclerView) findViewById(R.id.item_list);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }


        }));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                StringBuilder stringBuilder = new StringBuilder();

                if (strSkills.size()>0){

                    for (int i=0;i<strSkills.size();i++) {
                        if (strSkills.size()==1){
                            stringBuilder.append(strSkills.get(i).toString());

                        }else {
                            stringBuilder.append(strSkills.get(i).toString()).append(",");

                        }

                    }
                    returnIntent.putExtra("result",stringBuilder.toString());
                    setResult(4,returnIntent);
                    finish();
                    Log.v("skillss",stringBuilder.toString());
                }
                else{
                    returnIntent.putExtra("result",stringBuilder.toString());
                    setResult(5,returnIntent);
                    finish();
                }
            }
        });

        // search suggestions using the edittext widget
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void multiSelect(int position) {
        Skills data = mAdapter.getItem(position);
        if (data != null) {

            if (selectedIds.contains(Integer.parseInt(data.getId()))){
                selectedIds.remove(Integer.valueOf(data.getId()));
                mValues.get(position).setChk(false);
                mAdapter.setSelectedIds(selectedIds);
                if (strSkills.size()>0){
                    if (strSkills.contains(data.getSkills())){
                        strSkills.remove(position);
                    }
                }

            }
            else{
                mValues.get(position).setChk(true);
                selectedIds.add(Integer.parseInt(data.getId()));
                mAdapter.setSelectedIds(selectedIds);
                strSkills.add(mValues.get(position).getSkills());

            }




        }
    }


    class HttpGetTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String url = "http://test378.bawabba.com/assets/skills.json";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONArray json = new JSONArray(jsonStr);
                    skillList=new ArrayList<>();

                    Log.e("ResponseCity", jsonStr);
                    for (int i = 0; i < json.length(); i++) {
                        final JSONObject e = json.getJSONObject(i);
                        String skill = e.getString("name");
                        Skills skills=new Skills();
                        skills.setSkills(skill);
                        skills.setId(e.getString("id"));

                        skillList.add(skills);

//                        Log.e("ResponseCity", String.valueOf(responseList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            filteredList = new ArrayList<Skills>();
            filteredList.addAll(skillList);
            assert recyclerView != null;
            mAdapter = new SimpleItemRecyclerViewAdapter(filteredList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    // create a custom RecycleViewAdapter class
    public   class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
        private CustomFilter mFilter;
        private List<Integer> selectedIds = new ArrayList<>();

        public SimpleItemRecyclerViewAdapter(List<Skills> items) {
            mValues = items;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mContentView.setText(mValues.get(position).getSkills());
            if (mValues.get(position).isChk()){
                holder.imgChk.setVisibility(View.VISIBLE);
            }else{
                holder.imgChk.setVisibility(View.GONE);

            }
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            ImageView imgChk;
            public Skills mItem;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                imgChk = (ImageView) view.findViewById(R.id.imgchk);

            }
            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
        public void setSelectedIds(List<Integer> selectedIds) {
            this.selectedIds = selectedIds;

            notifyDataSetChanged();
        }
        public class CustomFilter extends Filter {
            private SimpleItemRecyclerViewAdapter mAdapter;
            private CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(skillList);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for ( Skills mWords : skillList) {
                        if (mWords.getSkills().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((List<Skills>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
        public Skills getItem(int position){
            return mValues.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();

        returnIntent.putExtra("result","");
        setResult(5,returnIntent);
        finish();
    }


}
