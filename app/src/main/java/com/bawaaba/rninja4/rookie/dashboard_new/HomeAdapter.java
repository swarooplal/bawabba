package com.bawaaba.rninja4.rookie.dashboard_new;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.Information;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_grid;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public HomeAdapter(List<Information> models,ItemClickListener_grid itemClickListener) {
        this.models .addAll(models);
        this.itemClickListener=itemClickListener;
    }

    public void setList(List<Information> models) {
        if(models!=null) {
            this.models.clear();
            this.models.addAll(models);
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            model=models.get(position);
            holder.txtTitle.setText(model.getTitile());
            holder.image.setImageResource(model.getImageID());
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.row_text);
            image = (ImageView) itemView.findViewById(R.id.img_row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null)
                        try {
                            itemClickListener.onItemClick(getAdapterPosition());
                        } catch (Exception e) {
                        }
                }
            });
        }
    }

    private ItemClickListener_grid itemClickListener;
    private List<Information> models=new ArrayList<>();
    private Information model;
}
