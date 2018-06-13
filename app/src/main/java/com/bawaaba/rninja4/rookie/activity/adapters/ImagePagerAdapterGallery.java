package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.Image;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

/**
 * Created by rninja4 on 10/22/17.
 */

public class ImagePagerAdapterGallery  extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<Image> advertisements;

    public ImagePagerAdapterGallery(Context mContext, ArrayList<Image> homeSliderses) {
        this.mContext = mContext;
        this.advertisements = homeSliderses;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return advertisements.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_image, container, false);

        ZoomageView imageView = (ZoomageView) itemView.findViewById(R.id.ivitem_icon);
        // imageView.setImageResource(mResources[position]);
        try {
            Glide.with(mContext)
                    .load(advertisements.get(position).getLarge())
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
