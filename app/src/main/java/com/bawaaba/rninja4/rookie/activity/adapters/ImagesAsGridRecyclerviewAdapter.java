package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.profile.PortfolioImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/22/17.
 */

public class ImagesAsGridRecyclerviewAdapter extends RecyclerView.Adapter<ImagesAsGridRecyclerviewAdapter.MainViewHolder> {

    FragmentActivity activity;
    RecyclerViewClickListener clickListener;
    List<String> checkedItems = new ArrayList<>();
    List<String> items = new ArrayList<>();
    private List<PortfolioImage> dataArrayList;
    private Context mContext;


    public ImagesAsGridRecyclerviewAdapter(Context context, List<PortfolioImage> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        this.mContext = context;
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_images, parent, false);
        return new MainViewHolder(adapterView);

    }
    public void setOnClickListener(ImagesAsGridRecyclerviewAdapter.RecyclerViewClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (dataArrayList.size() > 0) {
            Glide.with(mContext).load(dataArrayList.get(position).getImage()).into(holder.ivImage);
        }

    }
    @Override
    public long getItemId(int position) {
        if (dataArrayList.size() > 0) {
            return dataArrayList.size();
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        if (dataArrayList.size() > 0) {
            return dataArrayList.size();
        } else {
            return 1;
        }
    }

    public interface RecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivDelete;
        private ImageView ivImage;

        public MainViewHolder(View itemView) {
            super(itemView);
            if (dataArrayList.size() > 0) {
                ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
                ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
                ivDelete.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            try {
                clickListener.onClicked(getAdapterPosition(), v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
