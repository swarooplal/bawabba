package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.profile.PortfolioAudio;

import java.util.List;

/**
 * Created by rninja4 on 10/29/17.
 */

public class AudioListingRecyclerviewAdapter extends RecyclerView.Adapter<AudioListingRecyclerviewAdapter.MainViewHolder> {

    FragmentActivity activity;
    List<PortfolioAudio> data;
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context mContext;


    public AudioListingRecyclerviewAdapter(Context context, List<PortfolioAudio> emails) {
        this.data = emails;
        this.mContext = context;
    }




    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_audio_list, parent, false);
        return new MainViewHolder(adapterView);
    }


    public void setOnClickListener(AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public interface AdvertisementsRecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView tvAudioName;

        ImageView ivPlay;
        ImageView ivEditAudio;
        ImageView tvDeleteAudio;


        public MainViewHolder(View itemView) {
            super(itemView);
            ivPlay = (ImageView) itemView.findViewById(R.id.ivPlay);
            ivEditAudio = (ImageView) itemView.findViewById(R.id.ivEditAudio);
            tvDeleteAudio = (ImageView) itemView.findViewById(R.id.tvDeleteAudio);
            tvAudioName = (AppCompatTextView) itemView.findViewById(R.id.tvAudioName);
            tvDeleteAudio.setOnClickListener(this);
            ivEditAudio.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindData(MainViewHolder holder, int position) {
            try {
                tvAudioName.setText(data.get(position).getTitle());
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
