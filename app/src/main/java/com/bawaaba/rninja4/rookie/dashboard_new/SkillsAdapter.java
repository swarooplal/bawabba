package com.bawaaba.rninja4.rookie.dashboard_new;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    public SkillsAdapter(List<String> list) {
        if(list!=null) {
            this.models.addAll(list);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(ctx==null)
            ctx=parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ada_skill, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtSkill.setText(models.get(position));
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSkill;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSkill=itemView.findViewById(R.id.skill);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((BaseBottomHelperActivity)ctx).searchUser("",models.get(getAdapterPosition()),"");
                    } catch (Exception e) {}
                }
            });
        }
    }

    private final List<String> models = new ArrayList<>();
    private Context ctx;

}
