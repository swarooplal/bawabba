package com.bawaaba.rninja4.rookie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_grid;

import java.util.ArrayList;

/**
 * Created by rninja4 on 8/29/17.
 */

public class MycustomAdapter  extends RecyclerView.Adapter<MycustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<Information> data;
    LayoutInflater inflator;
    public MycustomAdapter(Context context, ArrayList<Information> data) {

        this.context = context;
        this.data = data;
        inflator = LayoutInflater.from(context);


    }


    @Override
    public MycustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflator.inflate(R.layout.main_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewholder, final int position) {

        myViewholder.textview.setText(data.get(position).titile);
        myViewholder.imageview.setImageResource(data.get(position).imageID);


//        myViewholder.setItemClickListener_grid(new ItemClickListener_grid() {
//            @Override
//            public void onItemClick(int pos) {
//
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView textview;
        ImageView imageview;
        LinearLayout grid_linearlayout;
        ItemClickListener_grid itemClickListener;


        public MyViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.row_text);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            grid_linearlayout=(LinearLayout)itemView.findViewById(R.id.grid_linear);
          //  itemView.setOnClickListener(this);

        }

//        public void setItemClickListener_grid(ItemClickListener_grid itemClickListener){
//
//            this.itemClickListener=itemClickListener;
//        }


        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());

        }
    }
}