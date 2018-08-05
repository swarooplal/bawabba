package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bawaaba.rninja4.rookie.Information;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.profile.Profileresponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rninja4 on 1/10/18.
 */

public class CategoryListingAdapterRegister extends RecyclerView.Adapter<CategoryListingAdapterRegister.MainViewHolder> {
    FragmentActivity activity;
    List<Information> data;
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context mContext;
    List<String> checkedItems = new ArrayList<>();
    List<String> items = new ArrayList<>();

    public CategoryListingAdapterRegister(Context context, ArrayList<Information> emails) {
        this.data = emails;
        this.mContext = context;
    }

    @Override
    public CategoryListingAdapterRegister.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (data.size() > 0) {
            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category_list, parent, false);
            return new MainViewHolder(adapterView);
        } else {
            View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_data, parent, false);
            return new CategoryListingAdapterRegister.MainViewHolder(adapterView);
        }
    }
    public void setOnClickListener(AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }


    @Override
    public void onBindViewHolder(CategoryListingAdapterRegister.MainViewHolder holder, int position) {
        holder.bindData(holder, position);

        Log.e("position check", String.valueOf(position));

        String response = ObjectFactory.getInstance().getAppPreference(getApplicationContext()).getProfileResponse();
        final Profileresponse profileresponse = new Gson().fromJson(response, Profileresponse.class);

//        String category;
//        category= String.valueOf(profileresponse.getUserData().getCategory());
//
//        if(category.matches(String.valueOf(position))){
//            holder.linearlayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
//        }else {
//            holder.linearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }
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
        LinearLayout linearlayout;

        public MainViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvCategoryName = (AppCompatTextView) itemView.findViewById(R.id.tvCategoryName);
            linearlayout = (LinearLayout)itemView.findViewById(R.id.linear_category);
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