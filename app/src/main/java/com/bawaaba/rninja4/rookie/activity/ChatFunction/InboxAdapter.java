package com.bawaaba.rninja4.rookie.activity.ChatFunction;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.model.inbox.Email;

import java.util.List;

/**
 * Created by rninja4 on 6/19/18.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {
    private Context context;
    List<Email> emails;
   AdvertisementsRecyclerViewClickListener clickListener;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public AppCompatTextView tvName, tvTime, tvMessage;

        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            tvName = (AppCompatTextView) itemView.findViewById(R.id.tvName);
            tvTime = (AppCompatTextView) itemView.findViewById(R.id.tvTime);
            tvMessage = (AppCompatTextView) itemView.findViewById(R.id.tvMessage);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClicked(getAdapterPosition(), v);
        }
    }


    public InboxAdapter(Context context, List<Email> emails) {
        this.context = context;
        this.emails = emails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inbox, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Email item = emails.get(position);
        holder.tvName.setText(emails.get(position).getName());
        holder.tvTime.setText(emails.get(position).getDate());
        holder.tvMessage.setText(emails.get(position).getMessage());


    }
    public void setOnClickListener(InboxAdapter.AdvertisementsRecyclerViewClickListener listener) {
        this.clickListener = listener;
    }
    @Override
    public int getItemCount() {

        return emails.size();
    }

    public interface AdvertisementsRecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public void removeItem(int position) {
        emails.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

//    public void restoreItem(Item item, int position) {
//        cartList.add(position, item);
//        // notify item added by position
//        notifyItemInserted(position);
//    }
}

