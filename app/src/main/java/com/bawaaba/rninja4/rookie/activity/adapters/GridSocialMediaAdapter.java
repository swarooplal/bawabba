package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.SocialMediaListing;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/19/17.
 */

public class GridSocialMediaAdapter  extends BaseAdapter {

    private final List<SocialMediaListing> categories;
    private Context mContext;
//   SubCategories[] subCategories;

    public GridSocialMediaAdapter(Context context, ArrayList<SocialMediaListing> categories) {
        this.mContext = context;
        this.categories = categories;

    }


    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grig_item_social_media, null);
            AppCompatImageView ivCategoryImage = (AppCompatImageView) grid.findViewById(R.id.ivSocialMediaicon);

            ivCategoryImage.setImageResource(categories.get(position).getSocialImage());
            ivCategoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + categories.get(position).getUrl()));
//                    mContext.startActivity(browserIntent);
                    new FinestWebView.Builder(mContext).show("https://" + categories.get(position).getUrl());
                }
            });

        } else {

            grid = (View) convertView;
        }

        return grid;
    }
}
