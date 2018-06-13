
package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.adapters.ImagePagerAdapterGallery;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;

import java.util.ArrayList;

public class ImageSlidingActivity extends AppCompatActivity {

    private ViewPager vpImageSlider;
    private ArrayList<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sliding);
        getSupportActionBar().hide();
        initViews();
    }

    private void initViews() {
        vpImageSlider = (ViewPager) findViewById(R.id.vpImageSlider);
        images = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getImages();
        ImagePagerAdapterGallery gallery = new ImagePagerAdapterGallery(ImageSlidingActivity.this, images);
        vpImageSlider.setAdapter(gallery);
        vpImageSlider.setCurrentItem(ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getImagesCurrentPosition());
    }
}
