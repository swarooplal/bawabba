package com.bawaaba.rninja4.rookie.BioTab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by rninja4 on 10/15/17.
 */

public class AdapterServices   extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;

    List<ServiceData> data= Collections.emptyList();

    public AdapterServices(Context context, List<ServiceData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.service_list, parent,false);
        AdapterServices.MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        ServiceData current=data.get(position);



        myHolder.Title.setText(current.Service_title);

        if(current.Service_Currency.matches("ASK")){
            myHolder.price.setText("Ask for Price");
        }else {
            myHolder.price.setText(current.Service_Currency + ' ' + current.Service_price);
        }
          myHolder.Description.setText(current.Service_description);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        private TextView Title;
        private TextView Description;
        private TextView price;
//        private SQLiteHandler db;
//        private SessionManager session;
//        private TextView Edit_services;
//        private TextView Add_services;
//        private LinearLayout serviceLayout;

        public MyHolder(View itemView) {
            super(itemView);

            Title = (TextView)itemView. findViewById(R.id.service_title);
            Description = (TextView)itemView. findViewById(R.id.service_description);
            price = (TextView)itemView. findViewById(R.id.service_prize);


        }
    }
}

