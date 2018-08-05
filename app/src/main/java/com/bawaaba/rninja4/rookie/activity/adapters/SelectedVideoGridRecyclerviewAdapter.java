package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bawaaba.rninja4.rookie.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/24/17.
 */

public class SelectedVideoGridRecyclerviewAdapter extends RecyclerView.Adapter<SelectedVideoGridRecyclerviewAdapter.MainViewHolder> {
    FragmentActivity activity;
    RecyclerViewClickListener clickListener;
    List<String> checkedItems = new ArrayList<>();
    List<String> items = new ArrayList<>();
    private List<String> dataArrayList;
    private Context mContext;

    public SelectedVideoGridRecyclerviewAdapter(Context context, List<String> dataArrayList) {
        this.dataArrayList = dataArrayList;
        this.mContext = context;
    }
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_selected_videos, parent, false);
        return new MainViewHolder(adapterView);

    }
    public void setOnClickListener(SelectedVideoGridRecyclerviewAdapter.RecyclerViewClickListener listener) {
        this.clickListener = listener;
    }
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (dataArrayList.size() > 0) {

            String file_path=dataArrayList.get(0);
            File file = new File(String.valueOf(Uri.parse(file_path)/*.replaceAll(" ", "%20"))*/));
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//            Glide.with(mContext).load(bMap).into(holder.ivImag/e);
            holder.ivImage.setImageBitmap(bMap);
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
//                ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
                ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
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