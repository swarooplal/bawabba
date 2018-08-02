package com.bawaaba.rninja4.rookie.Account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.model.BlockModel;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivacyListsManager;
import com.quickblox.chat.listeners.QBPrivacyListListener;
import com.quickblox.chat.model.QBPrivacyList;
import com.quickblox.chat.model.QBPrivacyListItem;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 2/18/18.
 */

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.MyViewHolder>{
    private static final String TAG = BlockListAdapter.class.getSimpleName();
    QBPrivacyList qbPrivacyList;
    QBPrivacyListsManager privacyListsManager;
    private List<BlockModel> moviesList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        Button btnDelete;
        ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txttitle);
            imageView = (ImageView) view.findViewById(R.id.img);

            btnDelete = (Button) view.findViewById(R.id.btndelete);

        }
    }

    public BlockListAdapter(Context context,List<BlockModel> moviesList) {
        this.moviesList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blockuser_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BlockListAdapter.MyViewHolder holder, final int position) {

        final BlockModel movie = moviesList.get(position);
        holder.txtTitle.setText(movie.getNameOfUser());
        Glide.with(context)   // pass Context
                .load("https://test378.bawabba.com/assets/"+movie.getImagpath())    // pass the image url
                .transform(new CircleTransform(context))
                .into(holder.imageView);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qbPrivacyList=new QBPrivacyList();
                qbPrivacyList.setName("public");
                QBPrivacyListItem item1 = new QBPrivacyListItem();

                item1.setType(QBPrivacyListItem.Type.USER_ID);

                item1.setAllow(true);
                //   item1.setMutualBlock(false);


                item1.setType(QBPrivacyListItem.Type.USER_ID);
                item1.setValueForType(String.valueOf(movie.getUserId()));

                ArrayList<QBPrivacyListItem> items = new ArrayList<QBPrivacyListItem>();

                items.add(item1);

                qbPrivacyList.setItems(items);

                try {
                    privacyListsManager= QBChatService.getInstance().getPrivacyListsManager();

                    privacyListsManager.setPrivacyList(qbPrivacyList);

                    privacyListsManager.setPrivacyListAsDefault("public");
                    privacyListsManager.setPrivacyListAsActive("public");
                    privacyListsManager.addPrivacyListListener(new QBPrivacyListListener() {
                        @Override
                        public void setPrivacyList(String s, List<QBPrivacyListItem> list) {

                        }

                        @Override
                        public void updatedPrivacyList(String s) {

                        }
                    });
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SmackException.NotConnectedException while setting privacy list :- " + e.getMessage());
                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();
                    Log.e(TAG, "XMPPException.XMPPErrorException while setting privacy list :- " + e.getMessage());
                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "SmackException.NoResponseException while setting privacy list :- " + e.getMessage());
                }

// Remove the item on remove/button click
                moviesList.remove(position);

                /*
                    public final void notifyItemRemoved (int position)
                        Notify any registered observers that the item previously located at position
                        has been removed from the data set. The items previously located at and
                        after position may now be found at oldPosition - 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(position);


            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
