package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/22/17.
 */

public class Adapterother_file  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = Adapterother_file.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<OtherFile_data> data;

    public Adapterother_file(Context context, List<OtherFile_data> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.otherfile_list, parent, false);
        Adapterother_file.MyHolder holder = new Adapterother_file.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Adapterother_file.MyHolder myHolder = (Adapterother_file.MyHolder) holder;
        OtherFile_data current = data.get(position);

        myHolder.File_Title.setText(current.other_title);
        myHolder.File_Link.setText(current.other_link);
        myHolder.File_Description.setText(current.other_description);

        Intent from_other_file = ((Activity) context).getIntent();
        final String portfolio_link = from_other_file.getStringExtra("portfolio_link");

        try {

            final ArrayList<String> file_list_link = new ArrayList<String>();
            final ArrayList<String> file_list_title = new ArrayList<String>();
            final ArrayList<String> file_list_description = new ArrayList<String>();
            JSONArray filedata = new JSONArray(portfolio_link);

            for (int i = 0; i < filedata.length(); i++) {
                JSONObject object = filedata.getJSONObject(i);
                final String file_link = object.getString("links");
                final String file_title = object.getString("title");
                final String file_description = object.getString("description");
                file_list_link.add(file_link.toString());
                file_list_title.add(file_title.toString());
                file_list_description.add(file_description.toString());
            }


            myHolder.setItemClickListener_Other_Url(new ItemClickListener_Other_Url() {
                @Override
                public void onItemClick(int pos) {
                    Log.e("position_link", String.valueOf(pos));
//                    Intent to_link_browser = new Intent(context,LinkBrowser.class);
//                    to_link_browser.putExtra("links",file_list_link.get(pos));
//                    context.startActivity(to_link_browser);
////                    new FinestWebView.Builder(context).show("https://");
////                    Log.e("linkchecking",portfolio_link);
                    new FinestWebView.Builder(context).show(("https://"+file_list_link.get(pos)));
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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView File_Title;
        private TextView File_Link;
        private TextView File_Description;
        ItemClickListener_Other_Url itemClickListener;

        public MyHolder(View itemView) {
            super(itemView);

            File_Title=(TextView)itemView.findViewById(R.id.file_title);
            File_Link=(TextView)itemView.findViewById(R.id.file_link);
            File_Description=(TextView)itemView.findViewById(R.id.file_description);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_Other_Url(ItemClickListener_Other_Url itemClickListener){

            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
