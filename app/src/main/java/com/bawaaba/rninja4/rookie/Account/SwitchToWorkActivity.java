package com.bawaaba.rninja4.rookie.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bawaaba.rninja4.rookie.Data;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.CategoryListingAdapterRegister;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SwitchToWorkActivity extends BaseActivity {

    private RecyclerView rvShowCategory;
    List<String> selectedItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_to_work);

        getSupportActionBar().hide();
        initViews();

     //   Intent from_genral = getIntent();
     //   String category = from_genral.getStringExtra("category");


        //    Log.e("category_value",category);
//
//        try {
//            JSONArray category_data = new JSONArray(category);
//            for (int i = 0; i < category_data.length(); i++) {
//                final String selected_category = category_data.getString(i);
////                textSkills.append(skill + "\t \t \t");
//                selectedItems.add(selected_category);
//                Log.e("selectedcategory",selected_category);
//
//            }
//            for (int i = 0; i < selectedItems.size(); i++) {
//                System.out.println("SkillEditActivity.onCreate " + selectedItems.get(i));
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void initViews() {
        Intent from_genral = getIntent();
        final String skills = from_genral.getStringExtra("skills");
        rvShowCategory = (RecyclerView) findViewById(R.id.rvShowCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvShowCategory.setLayoutManager(layoutManager);
        CategoryListingAdapterRegister categoryListingAdapterRegister = new CategoryListingAdapterRegister(getApplicationContext(), Data.getdata());
        rvShowCategory.setAdapter(categoryListingAdapterRegister);
        //ObjectFactory.getInstance().getNetworkManager(SwitchToWorkActivity.this).setCheckedCategory(selectedItems);

        categoryListingAdapterRegister.setOnClickListener(new CategoryListingAdapterRegister.AdvertisementsRecyclerViewClickListener() {
            @Override
            public void onClicked(int position, View v) {
                Intent to_registerskills = new Intent(getApplicationContext(), SubcategoryListActivity.class);
                to_registerskills.putExtra("id", String.valueOf(position));
                to_registerskills.putExtra("skills",skills);
                startActivity(to_registerskills);
               // finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SwitchToWorkActivity.this, GeneralSettingsActivity.class);
        startActivity(intent);
        finish();
    }

}



