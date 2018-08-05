package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.model.profile.PortfolioDoc;
import com.bawaaba.rninja4.rookie.R;

import java.util.List;

/**
 * Created by rninja4 on 10/31/17.
 */

public class FileEditRecyclerviewAdapter  extends RecyclerView.Adapter<FileEditRecyclerviewAdapter.MainViewHolder>{

    FragmentActivity activity;
    List<PortfolioDoc> data;
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context mContext;


    public FileEditRecyclerviewAdapter(Context context, List<PortfolioDoc> data) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public FileEditRecyclerviewAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_edit_file_list, parent, false);
        return new MainViewHolder(adapterView);
    }

    public void setOnClickListener(AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }


    @Override
    public void onBindViewHolder(FileEditRecyclerviewAdapter.MainViewHolder holder, int position) {
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

        AppCompatTextView pdf_title;
        ImageView pdf_img;
        ImageView ivDeleteFile;
        ImageView ivEditFile;

        public MainViewHolder(View itemView) {
            super(itemView);

            pdf_title = (AppCompatTextView) itemView.findViewById(R.id.pdf_title);
            pdf_img = (ImageView) itemView.findViewById(R.id.pdf_img);
            ivDeleteFile = (ImageView) itemView.findViewById(R.id.ivDeleteFile);
            ivEditFile = (ImageView) itemView.findViewById(R.id.ivEditFile);
            itemView.setOnClickListener(this);
            ivEditFile.setOnClickListener(this);
            ivDeleteFile.setOnClickListener(this);
        }

        public void bindData(MainViewHolder holder, int position) {
            try {

                holder.pdf_title.setText(data.get(position).getTitle());
                Glide.with(mContext).load(data.get(position).getThumbnail()).into(holder.pdf_img);
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
