package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/4/17.
 */

public class AdapterVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterVideo.class.getSimpleName();
    private Context context;
    private SQLiteHandler db;

    private LayoutInflater inflater;
    private final List<PortfolioVideoData> data;


    public AdapterVideo(Context context, List<PortfolioVideoData> data) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.video_list, parent, false);
        AdapterVideo.MyHolder holder = new AdapterVideo.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdapterVideo.MyHolder myHolder = (AdapterVideo.MyHolder) holder;
         PortfolioVideoData current=data.get(position);



       // myHolder.bind(items.get(position), listener);

        Glide.with(context).load(current.video_thumbnail)
                .into(myHolder.thumbnail_video);

        Intent from_port_video = ((Activity) context).getIntent();
        final String portfolio_video = from_port_video.getStringExtra("portfolio_video");
       // Log.e("video", portfolio_video);

        try {

            final ArrayList<String> video_list = new ArrayList<String>();
            JSONArray video_data = new JSONArray(portfolio_video);

            for (int i = 0; i < video_data.length(); i++) {
                JSONObject object = video_data.getJSONObject(i);
                final String videolink = object.getString("link");
                video_list.add(videolink.toString());

            }
           // Log.e("videoweb check", String.valueOf(video_list.get(1)));
            myHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int pos) {
                    Log.e("position", String.valueOf(pos));
                    Intent to_video_web = new Intent(context,Portfolio_Video_Webview.class);
                    to_video_web.putExtra("link",video_list.get(pos));
                    context.startActivity(to_video_web);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }




//
//         myHolder.thumbnail_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("working", "working");
//
//                        Intent to_video_web = new Intent(v.getContext(), Portfolio_Video_Webview.class);
//                        to_video_web.putExtra("portfolio_video", portfolio_video);
//                        v.getContext().startActivity(to_video_web);
//
//                    }
//                });






            }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private ImageView thumbnail_video;
        ItemClickListener itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);

            thumbnail_video = (ImageView) itemView.findViewById(R.id.thumbnail_button);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener){

            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(this.getLayoutPosition());

        }
    }
}