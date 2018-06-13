package com.bawaaba.rninja4.rookie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.activity.Subcategory;
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
    public void onBindViewHolder(MyViewHolder myViewholder, final int position) {

        myViewholder.textview.setText(data.get(position).titile);
        myViewholder.imageview.setImageResource(data.get(position).imageID);
//        if(position==0||position==1||position==2) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#298FC2"));
//
//        }
//
//        if(position==3||position==4||position==5) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#05C3DE"));
//
//        }
//
//        if(position==6||position==7||position==8) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#009F4D"));
//
//        }
//
//        if(position==9||position==10||position==11) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#78BE20"));
//
//        }
//        if(position==12||position==13||position==14) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#F6BE00"));
//
//        }
//        if(position==15||position==16||position==17) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#E87722"));
//
//        }
//        if(position==18||position==19||position==20) {
//
//            myViewholder.grid_linearlayout.setBackgroundColor(Color.parseColor("#EE2737"));
//
//        }


//        myViewholder.imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent to_subcategory = new Intent(context, Subcategory.class);
//                to_subcategory.putExtra("id", position);
//                context.startActivity(to_subcategory);
//                // finish();
//            }
//        });

        myViewholder.setItemClickListener_grid(new ItemClickListener_grid() {
            @Override
            public void onItemClick(int pos) {
                Intent to_subcategory = new Intent(context, Subcategory.class);
                to_subcategory.putExtra("id", position);
                context.startActivity(to_subcategory);
            }
        });

//        myViewholder.setItemClickListener_grid(new ItemClickListener_grid() {
//            @Override
//            public void onItemClick(int pos) {
//                Intent to_skilledit = new Intent(context, SkillEditActivity.class);
//                to_skilledit.putExtra("id", position);
//                context.startActivity(to_skilledit);
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
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener_grid(ItemClickListener_grid itemClickListener){

            this.itemClickListener=itemClickListener;
        }


        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());

        }
    }
}