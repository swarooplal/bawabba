//package com.example.rninja4.rookie.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.rninja4.rookie.R;
//import com.example.rninja4.rookie.activity.portfolioTab.ItemClickListener_chat;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by rninja4 on 10/15/17.
// */
//
//public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private static final String TAG = AdapterChat.class.getSimpleName();
//    private Context context;
//    private LayoutInflater inflater;
//    private final List<InboxData> data;
//
//    public AdapterChat(Context context, List<InboxData> data) {
//        this.context = context;
//        this.inflater = inflater;
//        this.data = data;
//    }
//
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.inbox_list, parent, false);
//        AdapterChat.MyHolder holder = new AdapterChat.MyHolder(view);
//        return holder;
//    }
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        AdapterChat.MyHolder myHolder = (AdapterChat.MyHolder) holder;
//        InboxData current = data.get(position);
//
//        myHolder.chat_Name.setText(current.Chat_Name);
//        myHolder.chat_Message.setText(current.Chat_Message);
//        myHolder.chat_Date.setText(current.Chat_Date);
//
//        Intent from_inbox_fragment = ((Activity) context).getIntent();
//        final String chat_data = from_inbox_fragment.getStringExtra("emails");
//
//        try {
//            final ArrayList<String> chat_title_list = new ArrayList<String>();
//            final ArrayList<String> chat_message_list = new ArrayList<String>();
//            final ArrayList<String> chat_date_list = new ArrayList<String>();
//
//            JSONArray chat_details = new JSONArray("emails");
//            for (int i = 0; i < chat_details.length(); i++){
//
//                JSONObject object = chat_details.getJSONObject(i);
//                final String chat_name = object.getString("name");
//                final String chat_message = object.getString("message");
//                final String chat_date = object.getString("date");
//                chat_title_list.add(chat_name.toString());
//                chat_message_list.add(chat_message.toString());
//                chat_date_list.add(chat_date.toString());
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView chat_Name;
//        private TextView chat_Message;
//        private TextView chat_Date;
//
//        ItemClickListener_chat itemClickListener;
//
//        public MyHolder(View itemview) {
//            super(itemview);
//
//            chat_Name=(TextView)itemView.findViewById(R.id.name_chat);
//            chat_Message=(TextView)itemView.findViewById(R.id.meassage_chat);
//            chat_Date=(TextView)itemView.findViewById(R.id.date_chat);
//            itemView.setOnClickListener(this);
//        }
//        public void setItemClickListener_chat(ItemClickListener_chat itemClickListener){
//
//            this.itemClickListener=itemClickListener;
//        }
//
//
//        @Override
//        public void onClick(View v) {
//
//            this.itemClickListener.onItemClick(this.getLayoutPosition());
//
//        }
//    }
//}
