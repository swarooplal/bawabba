package com.bawaaba.rninja4.rookie.activity.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.inbox.Email;

import java.util.List;

/**
 * Created by rninja4 on 10/17/17.
 */

public class InboxrecyclerviewAdapter extends RecyclerView.Adapter<InboxrecyclerviewAdapter.MainViewHolder> {

    FragmentActivity activity;
    List<Email> emails;
    AdvertisementsRecyclerViewClickListener clickListener;
    private Context mContext;


    public InboxrecyclerviewAdapter(Context context, Activity activity, List<Email> emails) {
        this.activity = (FragmentActivity) activity;
        this.emails = emails;
        this.mContext = context;
    }



    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_inbox, parent, false);
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
        return emails.size();
    }

    public interface AdvertisementsRecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        AppCompatTextView tvMessage;
        public MainViewHolder(View itemView) {
            super(itemView);
            tvName = (AppCompatTextView) itemView.findViewById(R.id.tvName);
            tvTime = (AppCompatTextView) itemView.findViewById(R.id.tvTime);
            tvMessage = (AppCompatTextView) itemView.findViewById(R.id.tvMessage);
            itemView.setOnClickListener(this);
        }

        public void bindData(MainViewHolder holder, int position) {
            try {

                holder.tvName.setText(emails.get(position).getName());
                holder.tvTime.setText(emails.get(position).getDate());
                holder.tvMessage.setText(emails.get(position).getMessage());
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
