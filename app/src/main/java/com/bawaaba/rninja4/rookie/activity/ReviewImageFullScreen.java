package com.bawaaba.rninja4.rookie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;

public class ReviewImageFullScreen extends Activity {

 private ImageView review_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_image_full_screen);

        review_image=(ImageView)findViewById(R.id.fullScreenImageView);

               Log.e("review image check","stafdyasfyasfdasd");

                 Intent from_adapter_review = getIntent();
                 String image = from_adapter_review.getStringExtra("review_image");
               //  Log.e("review image check",image);

        Glide.with(getApplicationContext()).load(image).into(review_image);




    }
}
