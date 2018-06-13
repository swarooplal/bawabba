package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 10/18/17.
 */

public class RegisterSkillsRecyclerviewAdapter extends RecyclerView.Adapter<RegisterSkillsRecyclerviewAdapter.MainViewHolder> {

    FragmentActivity activity;
    RecyclerViewClickListener clickListener;
    List<String> checkedItems = new ArrayList<>();
    List<String> items = new ArrayList<>();
    private List<Skill> dataArrayList;
    private Context mContext;


    public RegisterSkillsRecyclerviewAdapter(Context context, List<Skill> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        this.mContext = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_registerskills, parent, false);
        return new MainViewHolder(adapterView);


    }

    public void setOnClickListener(RegisterSkillsRecyclerviewAdapter.RecyclerViewClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (dataArrayList.size() > 0) {
            items = ObjectFactory.getInstance().getNetworkManager(mContext).getCheckedItems();
            holder.checkboxSkills.setText(dataArrayList.get(position).getName());
            if (items.contains(holder.checkboxSkills.getText())) {
                holder.checkboxSkills.setChecked(true);
            } else {
                holder.checkboxSkills.setChecked(false);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        if (dataArrayList.size() > 0) {
            return dataArrayList.size();
        } else {
            return position;
        }
    }
    @Override
    public int getItemCount() {
        if (dataArrayList.size() > 0) {
            return dataArrayList.size();
        } else {
            return 1;
        }
    }

    public interface RecyclerViewClickListener {
        public void onClicked(int position, View v);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkboxSkills;
        private TextView tvProductName;

        public MainViewHolder(View itemView) {
            super(itemView);
            if (dataArrayList.size() > 0) {
                checkboxSkills = (CheckBox) itemView.findViewById(R.id.checkboxSkills);
                try {
                    itemView.setOnClickListener(this);
                    checkboxSkills.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkedItems=ObjectFactory.getInstance().getNetworkManager(mContext).getCheckedItems();
                            if (checkedItems.size() ==0) {
                                checkedItems.add(checkboxSkills.getText().toString());
                                checkboxSkills.setChecked(true);
                                Log.e("checkhedskillitems", String.valueOf(checkedItems));
                            } else {
                                if (checkedItems.contains(checkboxSkills.getText().toString())) {
                                    checkboxSkills.setChecked(false);
                                    checkedItems.remove(checkboxSkills.getText().toString());
                                    Log.e("checkhedskilremove", String.valueOf(checkedItems));
                                    Log.e("checkdskilllremovesize", String.valueOf(checkedItems.size()));
                                } else {
                                    checkboxSkills.setChecked(true);
                                    checkedItems.add(checkboxSkills.getText().toString());
                                    Log.e("checkhedskilll", String.valueOf(checkedItems));
                                    Log.e("checkhedskilllsize", String.valueOf(checkedItems.size()));
                                }
                            }
                         if(checkedItems.size()>10){
                             checkboxSkills.setChecked(false);
                             checkedItems.remove(checkboxSkills.getText().toString());
                             Toast.makeText(checkboxSkills.getContext(), "Maximum 10 skills are allowed.", Toast.LENGTH_LONG).show();
                             return;
                         }

                            ObjectFactory.getInstance().getNetworkManager(mContext).setCheckedItems(checkedItems);
                        }
                    });
//                    checkboxSkills.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            // TODO Auto-generated method stub
//                            checkboxSkills.setChecked(isChecked);
//                            if (isChecked) {
//                                count++;
//                            } else {
//                                count--;
//                            }
//                            Log.d("ttt", "c="+count+" mc="+max_count);
//                            if (count>max_count) {
//                                buttonView.setChecked(false);
//                                checkboxSkills.setChecked(false);
//                                count=max_count;//count will not exceed the limit.
//                                Toast.makeText(checkboxSkills.getContext(), "Maximum 10 skills are allowed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void bindData(MainViewHolder holder, final int position) {
            try {


            /*    if (items != null && items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).matches(holder.checkboxSkills.getText().toString())) {
                            holder.checkboxSkills.setChecked(true);
                        }
                    }
                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            try {
                clickListener.onClicked(getAdapterPosition(), v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
