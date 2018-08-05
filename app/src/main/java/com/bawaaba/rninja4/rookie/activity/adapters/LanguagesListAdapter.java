package com.bawaaba.rninja4.rookie.activity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
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

public class LanguagesListAdapter extends RecyclerView.Adapter<LanguagesListAdapter.MainViewHolder> {
    FragmentActivity activity;
    RecyclerViewClickListener clickListener;
    List<String> checkedItems = new ArrayList<>();
    List<String> items = new ArrayList<>();
    private List<Skill> dataArrayList;
    private Context mContext;
    int count=0,
     max_count=10;


    public LanguagesListAdapter(Context context, List<Skill> dataArrayList) {
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        this.mContext = context;
    }
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_registerskills, parent, false);
        return new MainViewHolder(adapterView);

    }
    public void setOnClickListener(LanguagesListAdapter.RecyclerViewClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if (dataArrayList.size() > 0) {
            items = ObjectFactory.getInstance().getNetworkManager(mContext).getCheckedLangauges();
            holder.checkboxSkills.setText(dataArrayList.get(position).getName());
            if (items.contains(dataArrayList.get(position).getId())) {
                holder.checkboxSkills.setChecked(true);
                holder.checkboxSkills.setTag(dataArrayList.get(position).getId().toString());
            } else {
                holder.checkboxSkills.setChecked(false);
                holder.checkboxSkills.setTag("null");

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
                            checkedItems = ObjectFactory.getInstance().getNetworkManager(mContext).getCheckedLangauges();
                            if (checkedItems.size() == 0) {
                                checkedItems.add(dataArrayList.get(getAdapterPosition()).getId().toString());
                                checkboxSkills.setTag(dataArrayList.get(getAdapterPosition()).getId().toString());
                                checkboxSkills.setChecked(true);
                            } else {
                                if (checkedItems.contains(checkboxSkills.getTag().toString())) {
                                    checkboxSkills.setChecked(false);
                                    checkedItems.remove(dataArrayList.get(getAdapterPosition()).getId().toString());
                                } else {
                                    checkboxSkills.setChecked(true);
                                    checkedItems.add(dataArrayList.get(getAdapterPosition()).getId().toString());
                                    checkboxSkills.setTag(dataArrayList.get(getAdapterPosition()).getId().toString());
                                }
                            }
                            if(checkedItems.size()>10){
                                checkboxSkills.setChecked(false);
                                checkedItems.remove(dataArrayList.get(getAdapterPosition()).getId().toString());
                                Toast.makeText(checkboxSkills.getContext(), "Maximum 10 languages are allowed.", Toast.LENGTH_LONG).show();
                                return;
                            }


                            ObjectFactory.getInstance().getNetworkManager(mContext).setCheckedLangauges(checkedItems);
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
//
//                            if (count>max_count) {
//                                buttonView.setChecked(false);
//                                checkboxSkills.setChecked(false);
//                                count=max_count;//count will not exceed the limit.
//                                Toast.makeText(checkboxSkills.getContext(), "Maximum 10 languages are allowed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
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

