package com.bawaaba.rninja4.rookie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bawaaba.rninja4.rookie.Data;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.CategoryListingAdapter;
import com.bawaaba.rninja4.rookie.utils.BaseActivity;

public class Registration_Category extends BaseActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private String role;
    private String fullname;
    private String email;
    private String location;
    private String contactNumber;
    private String password;
    private String DateofBirth;
    private String description;
    private String gender;
    private String profile_image;
    private RecyclerView rvShowCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__category);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");
        fullname = bundle.getString("fullname");
        email = bundle.getString("email");
        password = bundle.getString("password");
        DateofBirth = bundle.getString("dob");
        password = bundle.getString("password");
        location = bundle.getString("location");
        contactNumber = bundle.getString("phone");
        description = bundle.getString("description");
        gender = bundle.getString("gender");
        profile_image = bundle.getString("profile_img");
        initViews();
    }

    private void initViews() {
        rvShowCategory = (RecyclerView) findViewById(R.id.rvShowCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvShowCategory.setLayoutManager(layoutManager);
        CategoryListingAdapter categoryListingAdapter = new CategoryListingAdapter(getApplicationContext(), Data.getdata());
        rvShowCategory.setAdapter(categoryListingAdapter);

        categoryListingAdapter.setOnClickListener(new CategoryListingAdapter.AdvertisementsRecyclerViewClickListener() {
            @Override
            public void onClicked(int position, View v) {
                Intent to_registerskills = new Intent(getApplicationContext(), Register_Skills.class);
                to_registerskills.putExtra("category",String.valueOf(position));
                to_registerskills.putExtra("role", role);
                to_registerskills.putExtra("fullname", fullname);
                to_registerskills.putExtra("email", email);
                to_registerskills.putExtra("password", password);
                to_registerskills.putExtra("dob", DateofBirth);
                to_registerskills.putExtra("location",location);
                to_registerskills.putExtra("phone",contactNumber);
                to_registerskills.putExtra("description", description);
                to_registerskills.putExtra("gender", gender);
                to_registerskills.putExtra("profile_img", profile_image);
                startActivity(to_registerskills);
               //finish();

            }
        });

    }


    // mainListView = (ListView) findViewById(R.id.mainListView);

//        String[] NinjaCategories = new String[]{"Accounting and Legal", "Programming", "Architecture and Engineering",
//                "Arts and Crafts", "Beauty and Skincare", "Child and pet Care",
//                "Cleaning,Maintenance and Moving", "Consultants", "Designers",
//                "Digital Designers", "Entertainment and Events","Food and Beverage",
//                "Health and Well Being", "Hobby and Academic Classes", "Marketing",
//                "Music and Audio", "Sports Coaching", "Travel and Tourism",
//                "Video and Photography", "Writers and Translators"};

//        int[] drawableIds = {R.drawable.accounting_bawaba, R.drawable.analytics_bawaba, R.drawable.architecture_bawaba, R.drawable.arts_bawaba, R.drawable.beauty_bawaba,
//                             R.drawable.child_bawaba,R.drawable.cleaning_bawaba,R.drawable.consultant_bawaba,R.drawable.design_bawaba,R.drawable.digital_bawaba,
//                              R.drawable.events_bawaba,R.drawable.food_bawaba,R.drawable.health_bawaba,R.drawable.education_bawaba,R.drawable.marketing_bawaba,
//                               R.drawable.music_bawaba,R.drawable.sports_bawaba,R.drawable.travel_bawaba,R.drawable.photo_bawaba,R.drawable.writer_bawaba
//
//        };

    // ArrayList<String> profileList = new ArrayList<String>();
    //  profileList.addAll(Arrays.asList(NinjaCategories));

//        listAdapter = new ArrayAdapter<String>(this, R.layout.category_row, profileList);
//        mainListView.setAdapter(listAdapter);

//        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent to_registerskills = new Intent(Registration_Category.this, Register_Skills.class);
//                to_registerskills.putExtra("category",String.valueOf(position));
//                to_registerskills.putExtra("role", role);
//                to_registerskills.putExtra("fullname", fullname);
//                to_registerskills.putExtra("email", email);
//                to_registerskills.putExtra("password", password);
//                to_registerskills.putExtra("dob", DateofBirth);
//                to_registerskills.putExtra("location", "Dubai - United Arab Emirates");
//                to_registerskills.putExtra("description", description);
//                to_registerskills.putExtra("gender", gender);
//                to_registerskills.putExtra("profile_img", profile_image);
//                startActivity(to_registerskills);
//                finish();
//
//            }
//        });
//@Override
//public void onBackPressed() {
//    super.onBackPressed();
//    Intent intent = new Intent(Registration_Category.this, RegisterActivity.class);
//    startActivity(intent);
//    finish();
//}


}
