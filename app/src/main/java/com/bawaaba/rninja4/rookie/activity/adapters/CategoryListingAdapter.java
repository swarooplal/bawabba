package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bawaaba.rninja4.rookie.Information;
import com.bawaaba.rninja4.rookie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 11/22/17.
 */

public class CategoryListingAdapter  extends RecyclerView.Adapter<CategoryListingAdapter.MainViewHolder>{

    FragmentActivity activity;
    List<Information> data;
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context mContext;

    public CategoryListingAdapter(Context context, ArrayList<Information> emails) {
        this.data = emails;
        this.mContext = context;
    }

    @Override
    public CategoryListingAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (data.size() > 0) {
            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category_list, parent, false);
            return new MainViewHolder(adapterView);
        } else {
            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_data, parent, false);
            return new CategoryListingAdapter.MainViewHolder(adapterView);
        }
    }


    public void setOnClickListener(AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(CategoryListingAdapter.MainViewHolder holder, int position) {
        holder.bindData(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (data.size() > 0) {
            return data.size();
        } else {
            return 1;
        }
    }

    public interface AdvertisementsRecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tvCategoryName;
        ImageView ivIcon;

        public MainViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvCategoryName = (AppCompatTextView) itemView.findViewById(R.id.tvCategoryName);
            itemView.setOnClickListener(this);
        }

        public void bindData(MainViewHolder holder, int position) {
            try {
                holder.ivIcon.setImageResource(data.get(position).imageID);
                holder.tvCategoryName.setText(data.get(position).titile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {

            clickListener.onClicked(getAdapterPosition(), v);

        }
    }
}
