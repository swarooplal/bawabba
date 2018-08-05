package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.Edit_Audio_Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/10/17.
 */

public class AdapterAudio  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterAudio.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<Edit_Audio_Data> data;

    public AdapterAudio(Context context, List<Edit_Audio_Data> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.audio_list, parent, false);
        AdapterAudio.MyHolder holder = new AdapterAudio.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdapterAudio.MyHolder myHolder = (AdapterAudio.MyHolder) holder;
        Edit_Audio_Data current = data.get(position);

        myHolder.Audio_Title.setText(current.Audio_Title);
        myHolder.play_button.setImageURI(Uri.parse(current.Audio_Link));

        Intent from_port_audio = ((Activity) context).getIntent();
        final String portfolio_audio = from_port_audio.getStringExtra("portfolio_audio");
        try {

            final ArrayList<String> audio_list = new ArrayList<String>();
            final ArrayList<String> audio_list_title = new ArrayList<String>();
            JSONArray audio_data = new JSONArray(portfolio_audio);

            for (int i = 0; i < audio_data.length(); i++) {
                JSONObject object = audio_data.getJSONObject(i);
                final String audio_link = object.getString("audio_link");
                final String audio_title = object.getString("title");
                audio_list.add(audio_link.toString());
                audio_list_title.add(audio_title.toString());
            }

            myHolder.setItemClickListener_audio(new ItemClickListener_audio() {
                @Override
                public void onItemClick(int pos) {
                    Log.e("position_audio", String.valueOf(pos));
                    Intent to_audio_web = new Intent(context,Portfolio_Audio_Web.class);
                    to_audio_web.putExtra("audio_link",audio_list.get(pos));

                    context.startActivity(to_audio_web);

                }
            });




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageButton play_button;
        private TextView Audio_Title;
        private LinearLayout Audio_layout;

        ItemClickListener_audio itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);
            play_button=(ImageButton)itemView.findViewById(R.id.audio_button);
            Audio_Title=(TextView)itemView.findViewById(R.id.text_audio);
            Audio_layout=(LinearLayout)itemView.findViewById(R.id.audio_linearlayout);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_audio(ItemClickListener_audio itemClickListener){

            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }}