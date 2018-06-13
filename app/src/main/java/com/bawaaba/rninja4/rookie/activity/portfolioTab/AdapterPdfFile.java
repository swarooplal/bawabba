package com.bawaaba.rninja4.rookie.activity.portfolioTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.PDFdisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/11/17.
 */

public class AdapterPdfFile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterPdfFile.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private List<PdfData> data;

    public AdapterPdfFile(Context context, List<PdfData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.pdf_list, parent, false);
        AdapterPdfFile.MyHolder holder = new AdapterPdfFile.MyHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdapterPdfFile.MyHolder myHolder = (AdapterPdfFile.MyHolder) holder;
        PdfData current = data.get(position);

        myHolder.Pdf_Title.setText(current.PDF_Title);
        Glide.with(context).load(current.PDF_Thumbnail)
                .into(myHolder.pdf_button);

        Intent from_port_pdf = ((Activity) context).getIntent();
        final String portfolio_file = from_port_pdf.getStringExtra("portfolio_doc");

        try {
            final ArrayList<String> pdf_list = new ArrayList<String>();
            final ArrayList<String> pdf_list_title = new ArrayList<String>();
            final ArrayList<String> pdf_list_thumbanail = new ArrayList<String>();
            JSONArray pdf_data = new JSONArray(portfolio_file);

            for(int i = 0; i < pdf_data.length(); i++){

                JSONObject object = pdf_data.getJSONObject(i);
                final String pdf_link = object.getString("documents");
                final String pdf_title = object.getString("title");
                final String pdf_thumbnail = object.getString("thumbnail");
                pdf_list.add(pdf_link.toString());
                pdf_list_title.add(pdf_title.toString());
                pdf_list_thumbanail.add(pdf_thumbnail.toString());
            }

            myHolder.setItemClickListener_file(new ItemClickListener_file() {
                @Override
                public void onItemClick(int pos) {
                    Log.e("position_audio", String.valueOf(pos));
                    Intent to_pdf_display = new Intent(context,PDFdisplay.class);
                    to_pdf_display.putExtra("documents",pdf_list.get(pos));

                    context.startActivity(to_pdf_display);
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

        private ImageButton pdf_button;
        private TextView Pdf_Title;

        ItemClickListener_file itemClickListener;
        public MyHolder(View itemView) {
            super(itemView);

            pdf_button=(ImageButton)itemView.findViewById(R.id.pdf_button);
            Pdf_Title=(TextView) itemView.findViewById(R.id.text_pdf);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_file(ItemClickListener_file itemClickListener){

            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
