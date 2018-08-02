package com.bawaaba.rninja4.rookie.dashboard_new;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.activity.CircleTransform;
import com.bawaaba.rninja4.rookie.activity.ProfileView;
import com.bawaaba.rninja4.rookie.activity.portfolioTab.ItemClickListener_search;
import com.bawaaba.rninja4.rookie.model.searchResult.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 6/13/17.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyHolder> {


    public SearchResultAdapter( List<User> data) {
        if(data!=null)
            this.models.addAll(data);
    }

    public void setList(List<User> data) {
        if(data!=null) {
            this.models.clear();
            this.models.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public SearchResultAdapter.MyHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context==null)
            context=parent.getContext();
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false));
    }
    @Override
    public void onBindViewHolder(SearchResultAdapter.MyHolder holder, int position) {

        try {
            model=models.get(position);
            holder.textprofileName.setText(model.getCurrentName());
           /* String skills ="";

            for (int i = 0; i < model.getSkills().size(); i++) {
                // skills =skills + ", " + data.get(position).getSkills().get(i);
                skills = skills + data.get(position).getSkills().get(i);
                if(i < (data.get(position).getSkills().size()-1)){
                    skills = skills + ", ";
                }
            }*/

            holder.textskill.setText(TextUtils.join(", ",model.getSkills()));
            holder.textBio.setText(model.getExperience());

            holder.count_review.setText(model.getReviewCount() +' '+"Reviews");

            if(model.getRating()!="null"){
                String serverresponse = (model.getRating() == null)?"0.0":model.getRating();
                holder.count_rating.setText(serverresponse);
            }
            try {
                holder.textRating.setRating(Float.parseFloat(model.getRating()));
                holder.textRating.setFocusable(false);
            } catch (Exception e) {
            }
            Glide.with(context).load(model.getProfileImg())
                    .transform(new CircleTransform(context))
                    .into(holder.ivprofile);

            if(model.getVerify().matches("0")){
                holder.verify_button.setVisibility(View.VISIBLE);
            }else{
                holder.verify_button.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }







        /*myHolder.setItemClickListener_serch(new ItemClickListener_search() {
            @Override
            public void onItemClick(int pos) {
                Intent to_profile = new Intent(context, ProfileView.class);
                to_profile.putExtra("reg_id", data.get(pos).getRegistration());
                context.startActivity(to_profile);
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return models.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {

        TextView textprofileName;
        ImageView ivprofile;
        TextView textskill;
        TextView textBio;
        RatingBar textRating;
        TextView count_rating;
        TextView count_review;
        ImageButton verify_button;




        public MyHolder(final View itemView) {
            super(itemView);

            textprofileName= (TextView) itemView.findViewById(R.id.textProfileName);
            ivprofile= (ImageView) itemView.findViewById(R.id.profile_img);
            textskill = (TextView) itemView.findViewById(R.id.profile_skill);
            textBio = (TextView) itemView.findViewById(R.id.profile_bio);
            textRating=(RatingBar)itemView.findViewById(R.id.ratings);
            count_rating=(TextView)itemView.findViewById(R.id.count_review);
            count_review=(TextView)itemView.findViewById(R.id.Review_count);
            verify_button=(ImageButton)itemView.findViewById(R.id.verify_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((BaseBottomHelperActivity)context).changeFragment(ProfileViewFragment.newInstance(models.get(getAdapterPosition()).getRegistration(),models.get(getAdapterPosition()).getCurrentName()),false);
                    } catch (Exception e) {}
                }
            });
        }

    }


    private List<User> models=new ArrayList<>();
    private User model;
    private Context context;
    String reg_id;
}
